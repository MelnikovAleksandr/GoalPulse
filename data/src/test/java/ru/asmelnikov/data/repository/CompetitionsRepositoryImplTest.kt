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
import ru.asmelnikov.data.local.CompetitionsRealmOptions
import ru.asmelnikov.data.local.models.CompetitionEntity
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

class CompetitionsRepositoryImplTest {

    private lateinit var api: FakeFootballApi
    private lateinit var realmOptions: FakeCompetitionsRealmOptions
    private lateinit var repository: CompetitionsRepositoryImpl

    @Before
    fun setUp() {
        api = FakeFootballApi()
        realmOptions = FakeCompetitionsRealmOptions()
        repository = CompetitionsRepositoryImpl(
            footballApi = api,
            realmOptions = realmOptions,
            retrofitErrorsHandler = RetrofitErrorsHandler.RetrofitErrorsHandlerImpl(),
        )
    }

    @Test
    fun remoteSuccess_upsertsMappedCompetitions() = runBlocking {
        api.competitionsResponse = Response.success(
            CompetitionModelDTO(
                competitions = listOf(
                    competitionDto(id = 2021, name = "Premier League"),
                    competitionDto(id = 2014, name = "La Liga"),
                ),
            ),
        )

        val result = repository.getAllCompetitionsFromRemoteToLocal()

        assertTrue(result is Resource.Success)
        assertEquals(true, result.data)
        assertEquals(
            listOf(2021 to "Premier League", 2014 to "La Liga"),
            realmOptions.upserted?.map { it.id to it.name },
        )
    }

    @Test
    fun remoteSuccessWithNullCompetitions_upsertsEmptyList() = runBlocking {
        api.competitionsResponse = Response.success(CompetitionModelDTO(competitions = null))

        val result = repository.getAllCompetitionsFromRemoteToLocal()

        assertTrue(result is Resource.Success)
        assertEquals(emptyList<CompetitionEntity>(), realmOptions.upserted)
    }

    @Test
    fun remoteNotFound_returnsError_andDoesNotUpsert() = runBlocking {
        api.competitionsResponse = Response.error(404, ByteArray(0).toResponseBody(null))

        val result = repository.getAllCompetitionsFromRemoteToLocal()

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.Https400Errors(errorCode = 404), result.httpErrors)
        assertNull(realmOptions.upserted)
    }

    @Test
    fun remoteConnectException_returnsMissingConnection_andDoesNotUpsert() = runBlocking {
        api.exception = ConnectException("failed to connect")

        val result = repository.getAllCompetitionsFromRemoteToLocal()

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.MissingConnection(), result.httpErrors)
        assertNull(realmOptions.upserted)
    }

    @Test
    fun localFlow_mapsEntitiesToDomain() = runBlocking {
        realmOptions.localCompetitions = listOf(
            competitionEntity(id = 2021, name = "Premier League"),
            competitionEntity(id = 2014, name = "La Liga"),
        )

        val competitions = repository.getAllCompetitionsFlowFromLocal().first()

        assertEquals(
            listOf(2021 to "Premier League", 2014 to "La Liga"),
            competitions.map { it.id to it.name },
        )
    }

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

    private fun competitionEntity(id: Int, name: String): CompetitionEntity {
        val entity = CompetitionEntity()
        entity.id = id
        entity.name = name
        return entity
    }

    // endregion

    // region Fakes

    private class FakeFootballApi : FootballApi {
        var competitionsResponse: Response<CompetitionModelDTO> =
            Response.success(CompetitionModelDTO(competitions = emptyList()))
        var exception: Exception? = null

        override suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO> {
            exception?.let { throw it }
            return competitionsResponse
        }

        override suspend fun getCompetitionStandingById(competitionId: String): Response<CompetitionStandingsModelDTO> {
            error("not used")
        }

        override suspend fun getCompetitionTopScorers(
            competitionId: String,
            limit: Int,
        ): Response<CompetitionScorersModelDTO> {
            error("not used")
        }

        override suspend fun getCompetitionMatches(competitionId: String): Response<MatchesDTO> {
            error("not used")
        }

        override suspend fun getTeamMatches(teamId: String): Response<MatchesDTO> {
            error("not used")
        }

        override suspend fun getHead2headById(matchId: String): Response<Head2headDTO> {
            error("not used")
        }

        override suspend fun getTeamInfoById(teamId: String): Response<TeamInfoDTO> {
            error("not used")
        }

        override suspend fun getPersonInfo(personId: String): Response<PersonInfoDTO> {
            error("not used")
        }
    }

    private class FakeCompetitionsRealmOptions : CompetitionsRealmOptions {
        var upserted: List<CompetitionEntity>? = null
        var localCompetitions: List<CompetitionEntity> = emptyList()

        override suspend fun upsertCompetitionsDataFromRemoteToLocal(
            competitions: List<CompetitionEntity>,
        ) {
            upserted = competitions
        }

        override fun getCompetitionsFlowFromLocal(): Flow<List<CompetitionEntity>> = flowOf(localCompetitions)
    }

    // endregion
}
