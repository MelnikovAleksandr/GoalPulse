package ru.asmelnikov.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.realmListOf
import java.util.UUID
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.CompetitionEmbeddedEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.PlayerEntity
import ru.asmelnikov.data.local.models.RefereeEntity
import ru.asmelnikov.data.local.models.ScoreEntity
import ru.asmelnikov.data.local.models.ScorerEntity
import ru.asmelnikov.data.local.models.StandingEntity
import ru.asmelnikov.data.local.models.TableEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity
import ru.asmelnikov.data.local.models.TimeEntity

@RunWith(AndroidJUnit4::class)
class StandingsRealmOptionsImplTest {

    private var realm: Realm? = null
    private lateinit var realmOptions: StandingsRealmOptions

    @Before
    fun setUp() {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                CompetitionStandingsEntity::class,
                StandingEntity::class,
                TableEntity::class,
                CompetitionEmbeddedEntity::class,
                CompetitionScorersEntity::class,
                ScorerEntity::class,
                PlayerEntity::class,
                MatchesEntity::class,
                MatchEntity::class,
                RefereeEntity::class,
                ScoreEntity::class,
                TimeEntity::class,
                AreaEntity::class,
                CurrentSeasonEntity::class,
                TeamEmbeddedEntity::class,
            ),
        )
            .inMemory()
            .name("standings-test-${UUID.randomUUID()}")
            .build()

        realm = Realm.open(config)
        realmOptions = StandingsRealmOptions.RealmOptionsImpl(realm!!)
    }

    @After
    fun tearDown() {
        realm?.close()
        realm = null
    }

    // region Standings

    @Test
    fun standingsUpdateReplacesTableAndFields() = runBlocking {
        realmOptions.upsertStandingsFromRemoteToLocal(
            standingsOf(
                id = "2021",
                competitionName = "Premier League",
                areaName = "England",
                table = listOf(
                    tableRow(1, 57, "Arsenal", 90),
                    tableRow(2, 61, "Chelsea", 80),
                ),
            ),
        )

        val updated = StandingsSnapshot(
            id = "2021",
            competitionName = "English Premier League",
            areaName = "England",
            table = listOf(tableRow(1, 65, "Manchester City", 88)),
        )
        realmOptions.upsertStandingsFromRemoteToLocal(updated.toEntity())

        assertEquals(updated, localStandings("2021"))
    }

    @Test
    fun standingsEmptyTableClearsPreviousRows() = runBlocking {
        realmOptions.upsertStandingsFromRemoteToLocal(
            standingsOf(
                id = "2014",
                competitionName = "La Liga",
                areaName = "Spain",
                table = listOf(tableRow(1, 81, "Barcelona", 85)),
            ),
        )

        val cleared = StandingsSnapshot(
            id = "2014",
            competitionName = "La Liga",
            areaName = "Spain",
            table = emptyList(),
        )
        realmOptions.upsertStandingsFromRemoteToLocal(cleared.toEntity())

        assertEquals(cleared, localStandings("2014"))
    }

    @Test
    fun standingsQueryByIdDoesNotLeakOtherCompetitions() = runBlocking {
        val premier = StandingsSnapshot(
            id = "2021",
            competitionName = "Premier League",
            areaName = "England",
            table = listOf(tableRow(1, 57, "Arsenal", 90)),
        )
        val laLiga = StandingsSnapshot(
            id = "2014",
            competitionName = "La Liga",
            areaName = "Spain",
            table = listOf(tableRow(1, 81, "Barcelona", 85)),
        )

        realmOptions.upsertStandingsFromRemoteToLocal(premier.toEntity())
        realmOptions.upsertStandingsFromRemoteToLocal(laLiga.toEntity())

        assertEquals(premier, localStandings("2021"))
        assertEquals(laLiga, localStandings("2014"))
        assertNull(localStandings("9999"))
    }

    @Test
    fun standingsFlowEmitsAfterEachUpsert() = runBlocking {
        val emissions = Channel<StandingsSnapshot?>(Channel.BUFFERED)
        val job = launch {
            realmOptions.getStandingsFlowById("2021").collect { emissions.send(it?.toSnapshot()) }
        }

        assertNull(emissions.receive())

        val first = StandingsSnapshot(
            id = "2021",
            competitionName = "Premier League",
            areaName = "England",
            table = listOf(tableRow(1, 57, "Arsenal", 90)),
        )
        realmOptions.upsertStandingsFromRemoteToLocal(first.toEntity())
        assertEquals(first, emissions.receive())

        val second = StandingsSnapshot(
            id = "2021",
            competitionName = "Premier League",
            areaName = "England",
            table = listOf(tableRow(1, 65, "Manchester City", 88)),
        )
        realmOptions.upsertStandingsFromRemoteToLocal(second.toEntity())
        assertEquals(second, emissions.receive())

        job.cancel()
    }

    // endregion

    // region Scorers

    @Test
    fun scorersUpdateReplacesList() = runBlocking {
        realmOptions.upsertScorersFromRemoteToLocal(
            ScorersSnapshot(
                id = "2021",
                scorers = listOf(
                    scorerRow(1, "Haaland", 30, 5),
                    scorerRow(2, "Salah", 25, 10),
                ),
            ).toEntity(),
        )

        val updated = ScorersSnapshot(
            id = "2021",
            scorers = listOf(scorerRow(3, "Palmer", 22, 12)),
        )
        realmOptions.upsertScorersFromRemoteToLocal(updated.toEntity())

        assertEquals(updated, localScorers("2021"))
    }

    @Test
    fun scorersEmptyListClearsPreviousRows() = runBlocking {
        realmOptions.upsertScorersFromRemoteToLocal(
            ScorersSnapshot(
                id = "2021",
                scorers = listOf(scorerRow(1, "Haaland", 30, 5)),
            ).toEntity(),
        )

        val cleared = ScorersSnapshot(id = "2021", scorers = emptyList())
        realmOptions.upsertScorersFromRemoteToLocal(cleared.toEntity())

        assertEquals(cleared, localScorers("2021"))
    }

    @Test
    fun scorersQueryByIdDoesNotLeakOtherCompetitions() = runBlocking {
        val premier = ScorersSnapshot(
            id = "2021",
            scorers = listOf(scorerRow(1, "Haaland", 30, 5)),
        )
        val laLiga = ScorersSnapshot(
            id = "2014",
            scorers = listOf(scorerRow(10, "Lewandowski", 28, 4)),
        )

        realmOptions.upsertScorersFromRemoteToLocal(premier.toEntity())
        realmOptions.upsertScorersFromRemoteToLocal(laLiga.toEntity())

        assertEquals(premier, localScorers("2021"))
        assertEquals(laLiga, localScorers("2014"))
        assertNull(localScorers("9999"))
    }

    @Test
    fun scorersFlowEmitsAfterEachUpsert() = runBlocking {
        val emissions = Channel<ScorersSnapshot?>(Channel.BUFFERED)
        val job = launch {
            realmOptions.getScorersFlowById("2021").collect { emissions.send(it?.toSnapshot()) }
        }

        assertNull(emissions.receive())

        val first = ScorersSnapshot(
            id = "2021",
            scorers = listOf(scorerRow(1, "Haaland", 30, 5)),
        )
        realmOptions.upsertScorersFromRemoteToLocal(first.toEntity())
        assertEquals(first, emissions.receive())

        val second = ScorersSnapshot(
            id = "2021",
            scorers = listOf(scorerRow(2, "Salah", 25, 10)),
        )
        realmOptions.upsertScorersFromRemoteToLocal(second.toEntity())
        assertEquals(second, emissions.receive())

        job.cancel()
    }

    // endregion

    // region Matches

    @Test
    fun matchesUpdateReplacesListAndFields() = runBlocking {
        realmOptions.upsertMatchesFromRemoteToLocal(
            MatchesSnapshot(
                id = "2021",
                seasonType = "LEAGUE",
                matches = listOf(
                    matchRow(100, "Arsenal", "Chelsea", "FINISHED", 2, 1),
                    matchRow(101, "Liverpool", "City", "TIMED", -1, -1),
                ),
            ).toEntity(),
        )

        val updated = MatchesSnapshot(
            id = "2021",
            seasonType = "CUP",
            matches = listOf(matchRow(200, "Arsenal", "City", "FINISHED", 3, 0)),
        )
        realmOptions.upsertMatchesFromRemoteToLocal(updated.toEntity())

        assertEquals(updated, localMatches("2021"))
    }

    @Test
    fun matchesEmptyListClearsPreviousRows() = runBlocking {
        realmOptions.upsertMatchesFromRemoteToLocal(
            MatchesSnapshot(
                id = "2021",
                seasonType = "LEAGUE",
                matches = listOf(matchRow(100, "Arsenal", "Chelsea", "FINISHED", 2, 1)),
            ).toEntity(),
        )

        val cleared = MatchesSnapshot(id = "2021", seasonType = "LEAGUE", matches = emptyList())
        realmOptions.upsertMatchesFromRemoteToLocal(cleared.toEntity())

        assertEquals(cleared, localMatches("2021"))
    }

    @Test
    fun matchesQueryByIdDoesNotLeakOtherCompetitions() = runBlocking {
        val premier = MatchesSnapshot(
            id = "2021",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(100, "Arsenal", "Chelsea", "FINISHED", 2, 1)),
        )
        val laLiga = MatchesSnapshot(
            id = "2014",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(300, "Barcelona", "Madrid", "FINISHED", 1, 1)),
        )

        realmOptions.upsertMatchesFromRemoteToLocal(premier.toEntity())
        realmOptions.upsertMatchesFromRemoteToLocal(laLiga.toEntity())

        assertEquals(premier, localMatches("2021"))
        assertEquals(laLiga, localMatches("2014"))
        assertNull(localMatches("9999"))
    }

    @Test
    fun matchesFlowEmitsAfterEachUpsert() = runBlocking {
        val emissions = Channel<MatchesSnapshot?>(Channel.BUFFERED)
        val job = launch {
            realmOptions.getMatchesFlowById("2021").collect { emissions.send(it?.toSnapshot()) }
        }

        assertNull(emissions.receive())

        val first = MatchesSnapshot(
            id = "2021",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(100, "Arsenal", "Chelsea", "FINISHED", 2, 1)),
        )
        realmOptions.upsertMatchesFromRemoteToLocal(first.toEntity())
        assertEquals(first, emissions.receive())

        val second = MatchesSnapshot(
            id = "2021",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(101, "Liverpool", "City", "FINISHED", 0, 0)),
        )
        realmOptions.upsertMatchesFromRemoteToLocal(second.toEntity())
        assertEquals(second, emissions.receive())

        job.cancel()
    }

    // endregion

    // region Read helpers

    private suspend fun localStandings(id: String): StandingsSnapshot? =
        realmOptions.getStandingsFlowById(id).first()?.toSnapshot()

    private suspend fun localScorers(id: String): ScorersSnapshot? =
        realmOptions.getScorersFlowById(id).first()?.toSnapshot()

    private suspend fun localMatches(id: String): MatchesSnapshot? =
        realmOptions.getMatchesFlowById(id).first()?.toSnapshot()

    // endregion

    // region Factories

    private fun standingsOf(
        id: String,
        competitionName: String,
        areaName: String,
        table: List<TableRowSnapshot>,
    ): CompetitionStandingsEntity = StandingsSnapshot(
        id = id,
        competitionName = competitionName,
        areaName = areaName,
        table = table,
    ).toEntity()

    private fun tableRow(
        position: Int,
        teamId: Int,
        teamName: String,
        points: Int,
    ) = TableRowSnapshot(position, teamId, teamName, points)

    private fun scorerRow(
        playerId: Int,
        playerName: String,
        goals: Int,
        assists: Int,
    ) = ScorerRowSnapshot(playerId, playerName, goals, assists)

    private fun matchRow(
        matchId: Int,
        homeTeam: String,
        awayTeam: String,
        status: String,
        homeGoals: Int,
        awayGoals: Int,
    ) = MatchRowSnapshot(matchId, homeTeam, awayTeam, status, homeGoals, awayGoals)

    // endregion

    // region Entity <-> Snapshot

    private fun StandingsSnapshot.toEntity(): CompetitionStandingsEntity {
        val snapshotId = id
        val snapshotAreaName = areaName
        val snapshotCompetitionName = competitionName
        val tableRows = table

        val entity = CompetitionStandingsEntity()
        entity.id = snapshotId
        entity.area = snapshotAreaName?.let { name ->
            AreaEntity().also {
                it.id = 1
                it.name = name
                it.code = "AREA"
            }
        }
        entity.competition = snapshotCompetitionName?.let { name ->
            CompetitionEmbeddedEntity().also {
                it.id = snapshotId.toIntOrNull() ?: 0
                it.name = name
                it.code = "CODE"
                it.type = "LEAGUE"
            }
        }
        entity.standings = if (tableRows.isEmpty()) {
            realmListOf()
        } else {
            realmListOf(
                StandingEntity().also { standing ->
                    standing.stage = "REGULAR_SEASON"
                    standing.type = "TOTAL"
                    standing.group = ""
                    standing.table = realmListOf<TableEntity>().apply {
                        addAll(
                            tableRows.map { row ->
                                TableEntity().also { tableEntity ->
                                    tableEntity.position = row.position
                                    tableEntity.points = row.points
                                    tableEntity.team = TeamEmbeddedEntity().also { team ->
                                        team.id = row.teamId
                                        team.name = row.teamName
                                    }
                                }
                            },
                        )
                    }
                },
            )
        }
        return entity
    }

    private fun CompetitionStandingsEntity.toSnapshot(): StandingsSnapshot {
        val tableRows = standings
            ?.flatMap { standing -> standing.table.orEmpty() }
            ?.map {
                TableRowSnapshot(
                    position = it.position,
                    teamId = it.team?.id ?: 0,
                    teamName = it.team?.name.orEmpty(),
                    points = it.points,
                )
            }
            .orEmpty()

        return StandingsSnapshot(
            id = id,
            competitionName = competition?.name,
            areaName = area?.name,
            table = tableRows,
        )
    }

    private fun ScorersSnapshot.toEntity(): CompetitionScorersEntity {
        val snapshotId = id
        val scorerRows = scorers

        val entity = CompetitionScorersEntity()
        entity.id = snapshotId
        entity.scorers = realmListOf<ScorerEntity>().apply {
            addAll(
                scorerRows.map { row ->
                    ScorerEntity().also { scorer ->
                        scorer.id = row.playerId
                        scorer.goals = row.goals
                        scorer.assists = row.assists
                        scorer.player = PlayerEntity().also { player ->
                            player.id = row.playerId
                            player.name = row.playerName
                        }
                    }
                },
            )
        }
        return entity
    }

    private fun CompetitionScorersEntity.toSnapshot(): ScorersSnapshot = ScorersSnapshot(
        id = id,
        scorers = scorers.orEmpty().map {
            ScorerRowSnapshot(
                playerId = it.player?.id ?: it.id,
                playerName = it.player?.name.orEmpty(),
                goals = it.goals,
                assists = it.assists,
            )
        },
    )

    private fun MatchesSnapshot.toEntity(): MatchesEntity {
        val snapshotId = id
        val snapshotSeasonType = seasonType
        val matchRows = matches

        val entity = MatchesEntity()
        entity.id = snapshotId
        entity.seasonType = snapshotSeasonType
        entity.matches = realmListOf<MatchEntity>().apply {
            addAll(
                matchRows.map { row ->
                    MatchEntity().also { match ->
                        match.id = row.matchId
                        match.status = row.status
                        match.referees = realmListOf()
                        match.homeTeam = TeamEmbeddedEntity().also { it.name = row.homeTeam }
                        match.awayTeam = TeamEmbeddedEntity().also { it.name = row.awayTeam }
                        match.score = ScoreEntity().also { score ->
                            score.fullTime = TimeEntity().also { time ->
                                time.home = row.homeGoals
                                time.away = row.awayGoals
                            }
                        }
                    }
                },
            )
        }
        return entity
    }

    private fun MatchesEntity.toSnapshot(): MatchesSnapshot = MatchesSnapshot(
        id = id,
        seasonType = seasonType,
        matches = matches.orEmpty().map {
            MatchRowSnapshot(
                matchId = it.id,
                homeTeam = it.homeTeam?.name.orEmpty(),
                awayTeam = it.awayTeam?.name.orEmpty(),
                status = it.status,
                homeGoals = it.score?.fullTime?.home ?: -1,
                awayGoals = it.score?.fullTime?.away ?: -1,
            )
        },
    )

    // endregion

    // region Models

    private data class StandingsSnapshot(
        val id: String,
        val competitionName: String?,
        val areaName: String?,
        val table: List<TableRowSnapshot>,
    )

    private data class TableRowSnapshot(val position: Int, val teamId: Int, val teamName: String, val points: Int)

    private data class ScorersSnapshot(val id: String, val scorers: List<ScorerRowSnapshot>)

    private data class ScorerRowSnapshot(val playerId: Int, val playerName: String, val goals: Int, val assists: Int)

    private data class MatchesSnapshot(val id: String, val seasonType: String, val matches: List<MatchRowSnapshot>)

    private data class MatchRowSnapshot(
        val matchId: Int,
        val homeTeam: String,
        val awayTeam: String,
        val status: String,
        val homeGoals: Int,
        val awayGoals: Int,
    )

    // endregion
}
