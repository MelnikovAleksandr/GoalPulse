package ru.asmelnikov.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.realmListOf
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
import ru.asmelnikov.data.local.models.ContractEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.MatchEntity
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.PersonEntity
import ru.asmelnikov.data.local.models.RefereeEntity
import ru.asmelnikov.data.local.models.ScoreEntity
import ru.asmelnikov.data.local.models.SquadByPositionEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.local.models.TimeEntity
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class TeamInfoRealmOptionsImplTest {

    private var realm: Realm? = null
    private lateinit var realmOptions: TeamInfoRealmOptions

    @Before
    fun setUp() {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                TeamInfoEntity::class,
                PersonEntity::class,
                SquadByPositionEntity::class,
                ContractEntity::class,
                AreaEntity::class,
                MatchesEntity::class,
                MatchEntity::class,
                RefereeEntity::class,
                ScoreEntity::class,
                TimeEntity::class,
                TeamEmbeddedEntity::class,
                CompetitionEmbeddedEntity::class,
                CurrentSeasonEntity::class
            )
        )
            .inMemory()
            .name("team-info-test-${UUID.randomUUID()}")
            .build()

        realm = Realm.open(config)
        realmOptions = TeamInfoRealmOptions.RealmOptionsImpl(realm!!)
    }

    @After
    fun tearDown() {
        realm?.close()
        realm = null
    }

    // region TeamInfo

    @Test
    fun teamInfoUpdateReplacesSquadAndFields() = runBlocking {
        realmOptions.upsertTeamInfoFromRemoteToLocal(
            TeamInfoSnapshot(
                id = "57",
                name = "Arsenal FC",
                shortName = "Arsenal",
                tla = "ARS",
                venue = "Emirates",
                founded = 1886,
                areaName = "England",
                coachName = "Mikel Arteta",
                squad = listOf(
                    squadPosition("Goalkeeper", listOf("Raya")),
                    squadPosition("Offence", listOf("Saka", "Jesus"))
                )
            ).toEntity()
        )

        val updated = TeamInfoSnapshot(
            id = "57",
            name = "Arsenal",
            shortName = "Arsenal",
            tla = "ARS",
            venue = "Emirates Stadium",
            founded = 1886,
            areaName = "England",
            coachName = "Mikel Arteta",
            squad = listOf(
                squadPosition("Offence", listOf("Saka"))
            )
        )
        realmOptions.upsertTeamInfoFromRemoteToLocal(updated.toEntity())

        assertEquals(updated, localTeamInfo("57"))
    }

    @Test
    fun teamInfoEmptySquadClearsPreviousPlayers() = runBlocking {
        realmOptions.upsertTeamInfoFromRemoteToLocal(
            TeamInfoSnapshot(
                id = "81",
                name = "FC Barcelona",
                shortName = "Barça",
                tla = "BAR",
                venue = "Camp Nou",
                founded = 1899,
                areaName = "Spain",
                coachName = "Hansi Flick",
                squad = listOf(
                    squadPosition("Offence", listOf("Lewandowski", "Yamal"))
                )
            ).toEntity()
        )

        val cleared = TeamInfoSnapshot(
            id = "81",
            name = "FC Barcelona",
            shortName = "Barça",
            tla = "BAR",
            venue = "Camp Nou",
            founded = 1899,
            areaName = "Spain",
            coachName = "Hansi Flick",
            squad = emptyList()
        )
        realmOptions.upsertTeamInfoFromRemoteToLocal(cleared.toEntity())

        assertEquals(cleared, localTeamInfo("81"))
    }

    @Test
    fun teamInfoQueryByIdDoesNotLeakOtherTeams() = runBlocking {
        val arsenal = TeamInfoSnapshot(
            id = "57",
            name = "Arsenal FC",
            shortName = "Arsenal",
            tla = "ARS",
            venue = "Emirates",
            founded = 1886,
            areaName = "England",
            coachName = "Mikel Arteta",
            squad = listOf(squadPosition("Offence", listOf("Saka")))
        )
        val chelsea = TeamInfoSnapshot(
            id = "61",
            name = "Chelsea FC",
            shortName = "Chelsea",
            tla = "CHE",
            venue = "Stamford Bridge",
            founded = 1905,
            areaName = "England",
            coachName = "Enzo Maresca",
            squad = listOf(squadPosition("Offence", listOf("Palmer")))
        )

        realmOptions.upsertTeamInfoFromRemoteToLocal(arsenal.toEntity())
        realmOptions.upsertTeamInfoFromRemoteToLocal(chelsea.toEntity())

        assertEquals(arsenal, localTeamInfo("57"))
        assertEquals(chelsea, localTeamInfo("61"))
        assertNull(localTeamInfo("9999"))
    }

    @Test
    fun teamInfoFlowEmitsAfterEachUpsert() = runBlocking {
        val emissions = Channel<TeamInfoSnapshot?>(Channel.BUFFERED)
        val job = launch {
            realmOptions.getTeamInfoFlowById("65").collect { emissions.send(it?.toSnapshot()) }
        }

        assertNull(emissions.receive())

        val first = TeamInfoSnapshot(
            id = "65",
            name = "Manchester City FC",
            shortName = "Man City",
            tla = "MCI",
            venue = "Etihad",
            founded = 1880,
            areaName = "England",
            coachName = "Pep Guardiola",
            squad = listOf(squadPosition("Offence", listOf("Haaland")))
        )
        realmOptions.upsertTeamInfoFromRemoteToLocal(first.toEntity())
        assertEquals(first, emissions.receive())

        val second = TeamInfoSnapshot(
            id = "65",
            name = "Manchester City FC",
            shortName = "Man City",
            tla = "MCI",
            venue = "Etihad Stadium",
            founded = 1880,
            areaName = "England",
            coachName = "Pep Guardiola",
            squad = listOf(squadPosition("Offence", listOf("Haaland", "Foden")))
        )
        realmOptions.upsertTeamInfoFromRemoteToLocal(second.toEntity())
        assertEquals(second, emissions.receive())

        job.cancel()
    }

    // endregion

    // region Matches

    @Test
    fun matchesUpdateReplacesListAndFields() = runBlocking {
        realmOptions.upsertMatchesFromRemoteToLocal(
            MatchesSnapshot(
                id = "57",
                seasonType = "LEAGUE",
                matches = listOf(
                    matchRow(100, "Arsenal", "Chelsea", "FINISHED", 2, 1),
                    matchRow(101, "Arsenal", "City", "TIMED", -1, -1)
                )
            ).toEntity()
        )

        val updated = MatchesSnapshot(
            id = "57",
            seasonType = "CUP",
            matches = listOf(matchRow(200, "Arsenal", "Liverpool", "FINISHED", 1, 0))
        )
        realmOptions.upsertMatchesFromRemoteToLocal(updated.toEntity())

        assertEquals(updated, localMatches("57"))
    }

    @Test
    fun matchesEmptyListClearsPreviousRows() = runBlocking {
        realmOptions.upsertMatchesFromRemoteToLocal(
            MatchesSnapshot(
                id = "81",
                seasonType = "LEAGUE",
                matches = listOf(matchRow(300, "Barcelona", "Madrid", "FINISHED", 3, 1))
            ).toEntity()
        )

        val cleared = MatchesSnapshot(id = "81", seasonType = "LEAGUE", matches = emptyList())
        realmOptions.upsertMatchesFromRemoteToLocal(cleared.toEntity())

        assertEquals(cleared, localMatches("81"))
    }

    @Test
    fun matchesQueryByIdDoesNotLeakOtherTeams() = runBlocking {
        val arsenal = MatchesSnapshot(
            id = "57",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(100, "Arsenal", "Chelsea", "FINISHED", 2, 1))
        )
        val chelsea = MatchesSnapshot(
            id = "61",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(400, "Chelsea", "Arsenal", "FINISHED", 0, 1))
        )

        realmOptions.upsertMatchesFromRemoteToLocal(arsenal.toEntity())
        realmOptions.upsertMatchesFromRemoteToLocal(chelsea.toEntity())

        assertEquals(arsenal, localMatches("57"))
        assertEquals(chelsea, localMatches("61"))
        assertNull(localMatches("9999"))
    }

    @Test
    fun matchesFlowEmitsAfterEachUpsert() = runBlocking {
        val emissions = Channel<MatchesSnapshot?>(Channel.BUFFERED)
        val job = launch {
            realmOptions.getMatchesFlowById("65").collect { emissions.send(it?.toSnapshot()) }
        }

        assertNull(emissions.receive())

        val first = MatchesSnapshot(
            id = "65",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(500, "City", "Arsenal", "FINISHED", 2, 2))
        )
        realmOptions.upsertMatchesFromRemoteToLocal(first.toEntity())
        assertEquals(first, emissions.receive())

        val second = MatchesSnapshot(
            id = "65",
            seasonType = "LEAGUE",
            matches = listOf(matchRow(501, "City", "Liverpool", "FINISHED", 1, 0))
        )
        realmOptions.upsertMatchesFromRemoteToLocal(second.toEntity())
        assertEquals(second, emissions.receive())

        job.cancel()
    }

    // endregion

    // region Read helpers

    private suspend fun localTeamInfo(id: String): TeamInfoSnapshot? =
        realmOptions.getTeamInfoFlowById(id).first()?.toSnapshot()

    private suspend fun localMatches(id: String): MatchesSnapshot? =
        realmOptions.getMatchesFlowById(id).first()?.toSnapshot()

    // endregion

    // region Factories

    private fun squadPosition(
        position: String,
        players: List<String>
    ) = SquadPositionSnapshot(position, players)

    private fun matchRow(
        matchId: Int,
        homeTeam: String,
        awayTeam: String,
        status: String,
        homeGoals: Int,
        awayGoals: Int
    ) = MatchRowSnapshot(matchId, homeTeam, awayTeam, status, homeGoals, awayGoals)

    // endregion

    // region Entity <-> Snapshot

    private fun TeamInfoSnapshot.toEntity(): TeamInfoEntity {
        val snapshotId = id
        val snapshotName = name
        val snapshotShortName = shortName
        val snapshotTla = tla
        val snapshotVenue = venue
        val snapshotFounded = founded
        val snapshotAreaName = areaName
        val snapshotCoachName = coachName
        val squadRows = squad

        val entity = TeamInfoEntity()
        entity.id = snapshotId
        entity.name = snapshotName
        entity.shortName = snapshotShortName
        entity.tla = snapshotTla
        entity.venue = snapshotVenue
        entity.founded = snapshotFounded
        entity.address = ""
        entity.clubColors = ""
        entity.crest = ""
        entity.website = ""
        entity.area = snapshotAreaName?.let { area ->
            AreaEntity().also {
                it.id = snapshotId.toIntOrNull() ?: 0
                it.name = area
                it.code = snapshotTla
            }
        }
        entity.coach = snapshotCoachName?.let { coach ->
            PersonEntity().also {
                it.id = snapshotId.toIntOrNull() ?: 0
                it.name = coach
                it.contract = ContractEntity().also { contract ->
                    contract.start = "2024-07-01"
                    contract.until = "2027-06-30"
                }
            }
        }
        entity.squadByPosition = realmListOf<SquadByPositionEntity>().apply {
            addAll(
                squadRows.map { row ->
                    SquadByPositionEntity().also { group ->
                        group.position = row.position
                        group.squad = realmListOf<PersonEntity>().apply {
                            addAll(
                                row.players.mapIndexed { index, playerName ->
                                    PersonEntity().also { person ->
                                        person.id = index + 1
                                        person.name = playerName
                                    }
                                }
                            )
                        }
                    }
                }
            )
        }
        return entity
    }

    private fun TeamInfoEntity.toSnapshot(): TeamInfoSnapshot {
        return TeamInfoSnapshot(
            id = id,
            name = name,
            shortName = shortName,
            tla = tla,
            venue = venue,
            founded = founded,
            areaName = area?.name,
            coachName = coach?.name,
            squad = squadByPosition.orEmpty().map { group ->
                SquadPositionSnapshot(
                    position = group.position,
                    players = group.squad.orEmpty().map { it.name }
                )
            }
        )
    }

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
                }
            )
        }
        return entity
    }

    private fun MatchesEntity.toSnapshot(): MatchesSnapshot {
        return MatchesSnapshot(
            id = id,
            seasonType = seasonType,
            matches = matches.orEmpty().map {
                MatchRowSnapshot(
                    matchId = it.id,
                    homeTeam = it.homeTeam?.name.orEmpty(),
                    awayTeam = it.awayTeam?.name.orEmpty(),
                    status = it.status,
                    homeGoals = it.score?.fullTime?.home ?: -1,
                    awayGoals = it.score?.fullTime?.away ?: -1
                )
            }
        )
    }

    // endregion

    // region Models

    private data class TeamInfoSnapshot(
        val id: String,
        val name: String,
        val shortName: String,
        val tla: String,
        val venue: String,
        val founded: Int,
        val areaName: String?,
        val coachName: String?,
        val squad: List<SquadPositionSnapshot>
    )

    private data class SquadPositionSnapshot(
        val position: String,
        val players: List<String>
    )

    private data class MatchesSnapshot(
        val id: String,
        val seasonType: String,
        val matches: List<MatchRowSnapshot>
    )

    private data class MatchRowSnapshot(
        val matchId: Int,
        val homeTeam: String,
        val awayTeam: String,
        val status: String,
        val homeGoals: Int,
        val awayGoals: Int
    )

    // endregion
}
