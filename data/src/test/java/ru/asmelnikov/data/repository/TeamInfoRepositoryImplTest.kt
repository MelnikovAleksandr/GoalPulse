package ru.asmelnikov.data.repository

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
import ru.asmelnikov.data.local.TeamInfoRealmOptions
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.models.CompetitionDTO
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.data.models.CompetitionScorersModelDTO
import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
import ru.asmelnikov.data.models.Head2headDTO
import ru.asmelnikov.data.models.MatchesDTO
import ru.asmelnikov.data.models.PersonInfoDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import java.net.ConnectException

class TeamInfoRepositoryImplTest {

    private lateinit var api: FakeFootballApi
    private lateinit var realmOptions: FakeTeamInfoRealmOptions
    private lateinit var repository: TeamInfoRepositoryImpl

    @Before
    fun setUp() {
        api = FakeFootballApi()
        realmOptions = FakeTeamInfoRealmOptions()
        repository = TeamInfoRepositoryImpl(
            footballApi = api,
            realmOptions = realmOptions,
            retrofitErrorsHandler = RetrofitErrorsHandler.RetrofitErrorsHandlerImpl()
        )
    }

    // region Team info

    @Test
    fun teamInfoSuccess_savesRequestedTeam() = runBlocking {
        api.teamInfoResponse = Response.success(
            TeamInfoDTO(
                id = 57,
                address = null,
                area = null,
                clubColors = null,
                coach = null,
                crest = null,
                founded = null,
                name = "Arsenal FC",
                shortName = null,
                squad = null,
                tla = null,
                venue = null,
                website = null
            )
        )

        val result = repository.getTeamInfoById("57")

        assertTrue(result is Resource.Success)
        assertEquals("57", api.requestedTeamInfoId)
        assertEquals("57", realmOptions.upsertedTeamInfo?.id)
        assertEquals("Arsenal FC", realmOptions.upsertedTeamInfo?.name)
        assertNull(realmOptions.upsertedMatches)
    }

    @Test
    fun teamInfoNotFound_doesNotSave() = runBlocking {
        api.teamInfoResponse = Response.error(404, ByteArray(0).toResponseBody(null))

        val result = repository.getTeamInfoById("81")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.Https400Errors(errorCode = 404), result.httpErrors)
        assertNull(realmOptions.upsertedTeamInfo)
    }

    @Test
    fun teamInfoNoNetwork_doesNotSave() = runBlocking {
        api.exception = ConnectException("failed to connect")

        val result = repository.getTeamInfoById("11")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.MissingConnection(), result.httpErrors)
        assertNull(realmOptions.upsertedTeamInfo)
    }

    @Test
    fun teamInfoFromDb_returnsRequestedTeam() = runBlocking {
        realmOptions.localTeamInfo = TeamInfoEntity().also { entity ->
            entity.id = "61"
            entity.name = "Chelsea FC"
        }

        val team = repository.getTeamInfoByIdFlowFromLocal("61").first()

        assertEquals("61", realmOptions.requestedTeamInfoId)
        assertEquals("61", team?.id)
        assertEquals("Chelsea FC", team?.name)
    }

    // endregion

    // region Team matches

    @Test
    fun teamMatchesSuccess_savesMatchesUnderTeamIdNotCompetitionId() = runBlocking {
        api.teamMatchesResponse = Response.success(
            MatchesDTO(
                competition = CompetitionDTO(
                    id = 2021,
                    area = null,
                    code = null,
                    currentSeason = null,
                    emblem = null,
                    name = "Premier League",
                    type = null
                ),
                matches = emptyList()
            )
        )

        val result = repository.getTeamMatchesFromRemoteToLocal("65")

        assertTrue(result is Resource.Success)
        assertEquals("65", api.requestedTeamMatchesId)
        assertEquals("65", realmOptions.upsertedMatches?.id)
        assertNull(realmOptions.upsertedTeamInfo)
    }

    @Test
    fun teamMatchesNullBody_doesNotSave() = runBlocking {
        api.teamMatchesResponse = Response.success(null)

        val result = repository.getTeamMatchesFromRemoteToLocal("86")

        assertTrue(result is Resource.Success)
        assertNull(realmOptions.upsertedMatches)
    }

    @Test
    fun teamMatchesNotFound_doesNotSave() = runBlocking {
        api.teamMatchesResponse = Response.error(404, ByteArray(0).toResponseBody(null))

        val result = repository.getTeamMatchesFromRemoteToLocal("73")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.Https400Errors(errorCode = 404), result.httpErrors)
        assertNull(realmOptions.upsertedMatches)
    }

    // endregion

    // region Fakes

    private class FakeFootballApi : FootballApi {
        var teamInfoResponse: Response<TeamInfoDTO> = Response.success(null)
        var teamMatchesResponse: Response<MatchesDTO> = Response.success(null)
        var exception: Exception? = null
        var requestedTeamInfoId: String? = null
        var requestedTeamMatchesId: String? = null

        override suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO> {
            error("not used")
        }

        override suspend fun getCompetitionStandingById(
            competitionId: String
        ): Response<CompetitionStandingsModelDTO> {
            error("not used")
        }

        override suspend fun getCompetitionTopScorers(
            competitionId: String,
            limit: Int
        ): Response<CompetitionScorersModelDTO> {
            error("not used")
        }

        override suspend fun getCompetitionMatches(competitionId: String): Response<MatchesDTO> {
            error("not used")
        }

        override suspend fun getTeamMatches(teamId: String): Response<MatchesDTO> {
            exception?.let { throw it }
            requestedTeamMatchesId = teamId
            return teamMatchesResponse
        }

        override suspend fun getHead2headById(matchId: String): Response<Head2headDTO> {
            error("not used")
        }

        override suspend fun getTeamInfoById(teamId: String): Response<TeamInfoDTO> {
            exception?.let { throw it }
            requestedTeamInfoId = teamId
            return teamInfoResponse
        }

        override suspend fun getPersonInfo(personId: String): Response<PersonInfoDTO> {
            error("not used")
        }
    }

    private class FakeTeamInfoRealmOptions : TeamInfoRealmOptions {
        var upsertedTeamInfo: TeamInfoEntity? = null
        var upsertedMatches: MatchesEntity? = null
        var localTeamInfo: TeamInfoEntity? = null
        var requestedTeamInfoId: String? = null

        override suspend fun upsertTeamInfoFromRemoteToLocal(teamInfo: TeamInfoEntity) {
            upsertedTeamInfo = teamInfo
        }

        override fun getTeamInfoFlowById(teamId: String): Flow<TeamInfoEntity?> {
            requestedTeamInfoId = teamId
            return flowOf(localTeamInfo)
        }

        override suspend fun upsertMatchesFromRemoteToLocal(matches: MatchesEntity) {
            upsertedMatches = matches
        }

        override fun getMatchesFlowById(teamId: String): Flow<MatchesEntity?> {
            error("not used")
        }
    }

    // endregion
}
