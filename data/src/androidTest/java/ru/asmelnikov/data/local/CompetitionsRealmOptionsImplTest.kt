package ru.asmelnikov.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import java.util.UUID
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.asmelnikov.data.local.models.AreaEntity
import ru.asmelnikov.data.local.models.CompetitionEntity
import ru.asmelnikov.data.local.models.CurrentSeasonEntity
import ru.asmelnikov.data.local.models.TeamEmbeddedEntity

@RunWith(AndroidJUnit4::class)
class CompetitionsRealmOptionsImplTest {

    private var realm: Realm? = null
    private lateinit var realmOptions: CompetitionsRealmOptions

    @Before
    fun setUp() {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                CompetitionEntity::class,
                AreaEntity::class,
                CurrentSeasonEntity::class,
                TeamEmbeddedEntity::class,
            ),
        )
            .inMemory()
            .name("competitions-test-${UUID.randomUUID()}")
            .build()

        realm = Realm.open(config)
        realmOptions = CompetitionsRealmOptions.RealmOptionsImpl(realm!!)
    }

    @After
    fun tearDown() {
        realm?.close()
        realm = null
    }

    @Test
    fun syncUpdatesExistingAddsNewRemovesMissing() = runBlocking {
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(
                competition(id = 2021, name = "Premier League", code = "PL"),
                competition(id = 2014, name = "La Liga", code = "PD"),
                competition(id = 2002, name = "Bundesliga", code = "BL1"),
            ),
        )

        val remoteSync = listOf(
            competition(id = 2021, name = "English Premier League", code = "PL"),
            competition(id = 2015, name = "Ligue 1", code = "FL1"),
        )

        realmOptions.upsertCompetitionsDataFromRemoteToLocal(remoteSync)

        assertEquals(remoteSync.toIdNameSet(), localCompetitions())
    }

    @Test
    fun updatesAllFieldsWhenSameIdComesAgain() = runBlocking {
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(
                competition(
                    id = 2021,
                    name = "Premier League",
                    code = "PL",
                    type = "LEAGUE",
                    emblem = "https://old",
                ),
            ),
        )

        val updated = listOf(
            competition(
                id = 2021,
                name = "English Premier League",
                code = "EPL",
                type = "CUP",
                emblem = "https://new",
            ),
        )

        realmOptions.upsertCompetitionsDataFromRemoteToLocal(updated)

        assertEquals(updated.toFieldSet(), localCompetitionFields())
    }

    @Test
    fun emptyRemoteListClearsLocalDb() = runBlocking {
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(
                competition(id = 2021, name = "Premier League", code = "PL"),
                competition(id = 2014, name = "La Liga", code = "PD"),
            ),
        )

        val emptyRemote = emptyList<CompetitionEntity>()
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(emptyRemote)

        assertEquals(emptyRemote.toIdNameSet(), localCompetitions())
    }

    @Test
    fun savesAreaAndSeasonInsideCompetition() = runBlocking {
        val expected = EmbeddedSnapshot(
            areaId = 2072,
            areaName = "England",
            areaCode = "ENG",
            areaFlag = "https://flag",
            seasonId = 1,
            startDate = "2025-08-15",
            endDate = "2026-05-24",
            matchDay = 1,
            winnerId = 57,
            winnerName = "Arsenal",
        )

        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(
                competition(id = 2021, name = "Premier League", code = "PL")
                    .withEmbedded(expected),
            ),
        )

        assertEquals(expected, localEmbedded(competitionId = 2021))
    }

    @Test
    fun replacesAreaAndSeasonOnNextSync() = runBlocking {
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(
                competition(id = 2021, name = "Premier League", code = "PL")
                    .withEmbedded(
                        EmbeddedSnapshot(
                            areaId = 2072,
                            areaName = "England",
                            areaCode = "ENG",
                            areaFlag = "https://flag",
                            seasonId = 1,
                            startDate = "2024-08-16",
                            endDate = "2025-05-25",
                            matchDay = 38,
                            winnerId = 65,
                            winnerName = "Manchester City",
                        ),
                    ),
            ),
        )

        val updated = EmbeddedSnapshot(
            areaId = 2072,
            areaName = "England",
            areaCode = "ENG",
            areaFlag = "https://new-flag",
            seasonId = 2,
            startDate = "2025-08-15",
            endDate = "2026-05-24",
            matchDay = 1,
            winnerId = null,
            winnerName = null,
        )

        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(
                competition(id = 2021, name = "Premier League", code = "PL")
                    .withEmbedded(updated),
            ),
        )

        assertEquals(updated, localEmbedded(competitionId = 2021))
    }

    @Test
    fun clearsAreaAndSeasonWhenRemoteSendsNulls() = runBlocking {
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(
                competition(id = 2021, name = "Premier League", code = "PL")
                    .withEmbedded(
                        EmbeddedSnapshot(
                            areaId = 2072,
                            areaName = "England",
                            areaCode = "ENG",
                            areaFlag = "https://flag",
                            seasonId = 1,
                            startDate = "2025-08-15",
                            endDate = "2026-05-24",
                            matchDay = 1,
                            winnerId = null,
                            winnerName = null,
                        ),
                    ),
            ),
        )

        val cleared = EmbeddedSnapshot(
            areaId = null,
            areaName = null,
            areaCode = null,
            areaFlag = null,
            seasonId = null,
            startDate = null,
            endDate = null,
            matchDay = null,
            winnerId = null,
            winnerName = null,
        )

        realmOptions.upsertCompetitionsDataFromRemoteToLocal(
            listOf(competition(id = 2021, name = "Premier League", code = "PL")),
        )

        assertEquals(cleared, localEmbedded(competitionId = 2021))
    }

    @Test
    fun flowEmitsNewListAfterEachUpsert() = runBlocking {
        val emissions = Channel<Set<Pair<Int, String>>>(Channel.BUFFERED)
        val collectJob = launch {
            realmOptions.getCompetitionsFlowFromLocal().collect { list ->
                emissions.send(list.toIdNameSet())
            }
        }

        assertEquals(emptySet<Pair<Int, String>>(), emissions.receive())

        val first = listOf(competition(id = 2021, name = "Premier League", code = "PL"))
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(first)
        assertEquals(first.toIdNameSet(), emissions.receive())

        val second = listOf(
            competition(id = 2021, name = "Premier League", code = "PL"),
            competition(id = 2014, name = "La Liga", code = "PD"),
        )
        realmOptions.upsertCompetitionsDataFromRemoteToLocal(second)
        assertEquals(second.toIdNameSet(), emissions.receive())

        collectJob.cancel()
    }

    private suspend fun localCompetitions(): Set<Pair<Int, String>> =
        realmOptions.getCompetitionsFlowFromLocal().first().toIdNameSet()

    private suspend fun localCompetitionFields(): Set<CompetitionFields> =
        realmOptions.getCompetitionsFlowFromLocal().first().toFieldSet()

    private suspend fun localEmbedded(competitionId: Int): EmbeddedSnapshot {
        val competition = realmOptions.getCompetitionsFlowFromLocal()
            .first()
            .single { it.id == competitionId }

        return EmbeddedSnapshot(
            areaId = competition.area?.id,
            areaName = competition.area?.name,
            areaCode = competition.area?.code,
            areaFlag = competition.area?.flag,
            seasonId = competition.currentSeason?.id,
            startDate = competition.currentSeason?.startDate,
            endDate = competition.currentSeason?.endDate,
            matchDay = competition.currentSeason?.currentMatchDay,
            winnerId = competition.currentSeason?.winner?.id,
            winnerName = competition.currentSeason?.winner?.name,
        )
    }

    private fun List<CompetitionEntity>.toIdNameSet(): Set<Pair<Int, String>> = map { it.id to it.name }.toSet()

    private fun List<CompetitionEntity>.toFieldSet(): Set<CompetitionFields> = map {
        CompetitionFields(
            id = it.id,
            name = it.name,
            code = it.code,
            type = it.type,
            emblem = it.emblem,
        )
    }.toSet()

    private fun CompetitionEntity.withEmbedded(snapshot: EmbeddedSnapshot): CompetitionEntity {
        area = snapshot.areaId?.let { areaId ->
            area(
                id = areaId,
                name = snapshot.areaName.orEmpty(),
                code = snapshot.areaCode.orEmpty(),
                flag = snapshot.areaFlag.orEmpty(),
            )
        }
        currentSeason = snapshot.seasonId?.let { seasonId ->
            season(
                id = seasonId,
                startDate = snapshot.startDate.orEmpty(),
                endDate = snapshot.endDate.orEmpty(),
                currentMatchDay = snapshot.matchDay ?: -1,
                winnerId = snapshot.winnerId,
                winnerName = snapshot.winnerName,
            )
        }
        return this
    }

    private fun competition(
        id: Int,
        name: String,
        code: String,
        type: String = "LEAGUE",
        emblem: String = "https://crest/$code",
    ): CompetitionEntity = CompetitionEntity().apply {
        this.id = id
        this.name = name
        this.code = code
        this.type = type
        this.emblem = emblem
    }

    private fun area(
        id: Int,
        name: String,
        code: String,
        flag: String,
    ): AreaEntity = AreaEntity().apply {
        this.id = id
        this.name = name
        this.code = code
        this.flag = flag
    }

    private fun season(
        id: Int,
        startDate: String,
        endDate: String,
        currentMatchDay: Int,
        winnerId: Int? = null,
        winnerName: String? = null,
    ): CurrentSeasonEntity = CurrentSeasonEntity().apply {
        this.id = id
        this.startDate = startDate
        this.endDate = endDate
        this.currentMatchDay = currentMatchDay
        this.winner = if (winnerId != null && winnerName != null) {
            TeamEmbeddedEntity().apply {
                this.id = winnerId
                this.name = winnerName
            }
        } else {
            null
        }
    }

    private data class CompetitionFields(
        val id: Int,
        val name: String,
        val code: String,
        val type: String,
        val emblem: String,
    )

    private data class EmbeddedSnapshot(
        val areaId: Int?,
        val areaName: String?,
        val areaCode: String?,
        val areaFlag: String?,
        val seasonId: Int?,
        val startDate: String?,
        val endDate: String?,
        val matchDay: Int?,
        val winnerId: Int?,
        val winnerName: String?,
    )
}
