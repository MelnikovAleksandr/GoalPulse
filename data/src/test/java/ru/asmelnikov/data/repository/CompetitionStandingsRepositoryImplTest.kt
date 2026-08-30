package ru.asmelnikov.data.repository

import java.net.ConnectException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.StandingsRealmOptions
import ru.asmelnikov.data.local.models.CompetitionEmbeddedEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.models.AggregatesDTO
import ru.asmelnikov.data.models.CompetitionDTO
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.data.models.CompetitionScorersModelDTO
import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
import ru.asmelnikov.data.models.Head2headDTO
import ru.asmelnikov.data.models.MatchesDTO
import ru.asmelnikov.data.models.PersonInfoDTO
import ru.asmelnikov.data.models.TeamH2HDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource

class CompetitionStandingsRepositoryImplTest {

    private lateinit var api: FakeFootballApi
    private lateinit var realmOptions: FakeStandingsRealmOptions
    private lateinit var repository: CompetitionStandingsRepositoryImpl

    @Before
    fun setUp() {
        api = FakeFootballApi()
        realmOptions = FakeStandingsRealmOptions()
        repository = CompetitionStandingsRepositoryImpl(
            footballApi = api,
            realmOptions = realmOptions,
            retrofitErrorsHandler = RetrofitErrorsHandler.RetrofitErrorsHandlerImpl(),
        )
    }

    // region Standings

    @Test
    fun standingsSuccess_savesTableForRequestedCompetition() = runBlocking {
        api.standingsResponse = Response.success(
            CompetitionStandingsModelDTO(
                area = null,
                competition = competitionDto(id = 2021, name = "Premier League"),
                season = null,
                standings = emptyList(),
            ),
        )

        val result = repository.getCompetitionStandingsFromRemoteToLocalById("2021")

        assertTrue(result is Resource.Success)
        assertEquals("2021", api.requestedStandingsId)
        assertEquals("2021", realmOptions.upsertedStandings?.id)
        assertNull(realmOptions.upsertedScorers)
        assertNull(realmOptions.upsertedMatches)
    }

    @Test
    fun standingsNotFound_doesNotSave() = runBlocking {
        api.standingsResponse = Response.error(404, ByteArray(0).toResponseBody(null))

        val result = repository.getCompetitionStandingsFromRemoteToLocalById("2021")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.Https400Errors(errorCode = 404), result.httpErrors)
        assertNull(realmOptions.upsertedStandings)
    }

    @Test
    fun standingsNoNetwork_doesNotSave() = runBlocking {
        api.exception = ConnectException("failed to connect")

        val result = repository.getCompetitionStandingsFromRemoteToLocalById("2021")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.MissingConnection(), result.httpErrors)
        assertNull(realmOptions.upsertedStandings)
    }

    @Test
    fun standingsFromDb_returnsRequestedCompetition() = runBlocking {
        realmOptions.localStandings = CompetitionStandingsEntity().also { entity ->
            entity.id = "2014"
            entity.competition = CompetitionEmbeddedEntity().also { competition ->
                competition.id = 2014
                competition.name = "La Liga"
            }
        }

        val standings = repository.getStandingsFlowFromLocalById("2014").first()

        assertEquals("2014", realmOptions.requestedStandingsId)
        assertEquals("2014", standings?.id)
        assertEquals("La Liga", standings?.competition?.name)
    }

    // endregion

    // region Scorers / Matches / Head2head wiring

    @Test
    fun scorersSuccess_savesScorersNotStandings() = runBlocking {
        api.scorersResponse = Response.success(
            CompetitionScorersModelDTO(
                competition = competitionDto(id = 2014, name = "La Liga"),
                season = null,
                scorers = emptyList(),
            ),
        )

        val result = repository.getCompetitionTopScorersBySeason("2014")

        assertTrue(result is Resource.Success)
        assertEquals("2014", api.requestedScorersId)
        assertEquals("2014", realmOptions.upsertedScorers?.id)
        assertNull(realmOptions.upsertedStandings)
    }

    @Test
    fun matchesSuccess_savesMatchesForCompetition() = runBlocking {
        api.matchesResponse = Response.success(
            MatchesDTO(
                competition = competitionDto(id = 2002, name = "Bundesliga"),
                matches = emptyList(),
            ),
        )

        val result = repository.getAllMatchesFromRemoteToLocal("2002")

        assertTrue(result is Resource.Success)
        assertEquals("2002", api.requestedMatchesId)
        assertEquals("2002", realmOptions.upsertedMatches?.id)
        assertNull(realmOptions.upsertedStandings)
    }

    @Test
    fun head2headSuccess_returnsMatchId_withoutSavingToDb() = runBlocking {
        api.head2headResponse = Response.success(
            Head2headDTO(
                aggregates = AggregatesDTO(
                    homeTeam = TeamH2HDTO(id = 57, name = "Arsenal", wins = 0, draws = 0, losses = 0),
                    awayTeam = TeamH2HDTO(id = 61, name = "Chelsea", wins = 0, draws = 0, losses = 0),
                    numberOfMatches = 0,
                    totalGoals = 0,
                ),
                matches = emptyList(),
            ),
        )

        val result = repository.getHead2headById(445)

        assertTrue(result is Resource.Success)
        assertEquals("445", api.requestedHead2headId)
        assertEquals(445, result.data?.id)
        assertNull(realmOptions.upsertedStandings)
        assertNull(realmOptions.upsertedScorers)
        assertNull(realmOptions.upsertedMatches)
    }

    @Test
    fun head2headNotFound_returnsError() = runBlocking {
        api.head2headResponse = Response.error(404, ByteArray(0).toResponseBody(null))

        val result = repository.getHead2headById(445)

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.Https400Errors(errorCode = 404), result.httpErrors)
    }

    // endregion

    // region Factories

    private fun competitionDto(id: Int, name: String): CompetitionDTO = CompetitionDTO(
        id = id,
        area = null,
        code = null,
        currentSeason = null,
        emblem = null,
        name = name,
        type = null,
    )

    // endregion

    // region Fakes

    private class FakeFootballApi : FootballApi {
        var standingsResponse: Response<CompetitionStandingsModelDTO> =
            Response.error(500, ByteArray(0).toResponseBody(null))
        var scorersResponse: Response<CompetitionScorersModelDTO> =
            Response.error(500, ByteArray(0).toResponseBody(null))
        var matchesResponse: Response<MatchesDTO> =
            Response.error(500, ByteArray(0).toResponseBody(null))
        var head2headResponse: Response<Head2headDTO> =
            Response.error(500, ByteArray(0).toResponseBody(null))
        var exception: Exception? = null

        var requestedStandingsId: String? = null
        var requestedScorersId: String? = null
        var requestedMatchesId: String? = null
        var requestedHead2headId: String? = null

        override suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO> {
            error("not used")
        }

        override suspend fun getCompetitionStandingById(
            competitionId: String,
        ): Response<CompetitionStandingsModelDTO> {
            exception?.let { throw it }
            requestedStandingsId = competitionId
            return standingsResponse
        }

        override suspend fun getCompetitionTopScorers(
            competitionId: String,
            limit: Int,
        ): Response<CompetitionScorersModelDTO> {
            exception?.let { throw it }
            requestedScorersId = competitionId
            return scorersResponse
        }

        override suspend fun getCompetitionMatches(competitionId: String): Response<MatchesDTO> {
            exception?.let { throw it }
            requestedMatchesId = competitionId
            return matchesResponse
        }

        override suspend fun getTeamMatches(teamId: String): Response<MatchesDTO> {
            error("not used")
        }

        override suspend fun getHead2headById(matchId: String): Response<Head2headDTO> {
            exception?.let { throw it }
            requestedHead2headId = matchId
            return head2headResponse
        }

        override suspend fun getTeamInfoById(teamId: String): Response<TeamInfoDTO> {
            error("not used")
        }

        override suspend fun getPersonInfo(personId: String): Response<PersonInfoDTO> {
            error("not used")
        }
    }

    private class FakeStandingsRealmOptions : StandingsRealmOptions {
        var upsertedStandings: CompetitionStandingsEntity? = null
        var upsertedScorers: CompetitionScorersEntity? = null
        var upsertedMatches: MatchesEntity? = null
        var localStandings: CompetitionStandingsEntity? = null
        var requestedStandingsId: String? = null

        override suspend fun upsertStandingsFromRemoteToLocal(standings: CompetitionStandingsEntity) {
            upsertedStandings = standings
        }

        override fun getStandingsFlowById(compId: String): Flow<CompetitionStandingsEntity?> {
            requestedStandingsId = compId
            return flowOf(localStandings)
        }

        override suspend fun upsertScorersFromRemoteToLocal(comp: CompetitionScorersEntity) {
            upsertedScorers = comp
        }

        override fun getScorersFlowById(compId: String): Flow<CompetitionScorersEntity?> {
            error("not used")
        }

        override suspend fun upsertMatchesFromRemoteToLocal(matches: MatchesEntity) {
            upsertedMatches = matches
        }

        override fun getMatchesFlowById(compId: String): Flow<MatchesEntity?> {
            error("not used")
        }
    }

    // endregion
}
