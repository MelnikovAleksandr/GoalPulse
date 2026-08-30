package ru.asmelnikov.data.repository

import java.net.ConnectException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import ru.asmelnikov.data.api.FootballApi
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

class PersonRepositoryImplTest {

    private lateinit var api: FakeFootballApi
    private lateinit var repository: PersonRepositoryImpl

    @Before
    fun setUp() {
        api = FakeFootballApi()
        repository = PersonRepositoryImpl(
            footballApi = api,
            retrofitErrorsHandler = RetrofitErrorsHandler.RetrofitErrorsHandlerImpl(),
        )
    }

    @Test
    fun success_returnsPersonForRequestedId() = runBlocking {
        api.personResponse = Response.success(
            PersonInfoDTO(
                id = 44,
                currentTeam = null,
                dateOfBirth = null,
                firstName = null,
                lastName = null,
                lastUpdated = null,
                name = "Bukayo Saka",
                nationality = null,
                position = null,
                section = null,
                shirtNumber = null,
                contract = null,
            ),
        )

        val result = repository.getPersonInfo("44")

        assertTrue(result is Resource.Success)
        assertEquals("44", api.requestedPersonId)
        assertEquals(44, result.data?.id)
        assertEquals("Bukayo Saka", result.data?.name)
    }

    @Test
    fun successWithNullBody_returnsEmptyPerson() = runBlocking {
        api.personResponse = Response.success(null)

        val result = repository.getPersonInfo("1")

        assertTrue(result is Resource.Success)
        assertEquals("", result.data?.name)
        assertEquals("", result.data?.firstName)
    }

    @Test
    fun notFound_returnsError() = runBlocking {
        api.personResponse = Response.error(404, ByteArray(0).toResponseBody(null))

        val result = repository.getPersonInfo("2234")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.Https400Errors(errorCode = 404), result.httpErrors)
    }

    @Test
    fun noNetwork_returnsMissingConnection() = runBlocking {
        api.exception = ConnectException("failed to connect")

        val result = repository.getPersonInfo("7788")

        assertTrue(result is Resource.Error)
        assertEquals(ErrorsTypesHttp.MissingConnection(), result.httpErrors)
    }

    // region Fakes

    private class FakeFootballApi : FootballApi {
        var personResponse: Response<PersonInfoDTO> = Response.success(null)
        var exception: Exception? = null
        var requestedPersonId: String? = null

        override suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO> {
            error("not used")
        }

        override suspend fun getCompetitionStandingById(
            competitionId: String,
        ): Response<CompetitionStandingsModelDTO> {
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
            exception?.let { throw it }
            requestedPersonId = personId
            return personResponse
        }
    }

    // endregion
}
