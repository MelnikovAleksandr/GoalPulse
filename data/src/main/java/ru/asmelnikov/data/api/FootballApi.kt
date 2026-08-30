package ru.asmelnikov.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.data.models.CompetitionScorersModelDTO
import ru.asmelnikov.data.models.CompetitionStandingsModelDTO
import ru.asmelnikov.data.models.Head2headDTO
import ru.asmelnikov.data.models.MatchesDTO
import ru.asmelnikov.data.models.PersonInfoDTO
import ru.asmelnikov.data.models.TeamInfoDTO
import ru.asmelnikov.utils.Constants.FOOTBALL_API_KEY

interface FootballApi {

    @GET("competitions/")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getAllFootballCompetitions(): Response<CompetitionModelDTO>

    @GET("competitions/{competitionId}/standings")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getCompetitionStandingById(
        @Path("competitionId") competitionId: String,
    ): Response<CompetitionStandingsModelDTO>

    @GET("competitions/{competitionId}/scorers")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getCompetitionTopScorers(
        @Path("competitionId") competitionId: String,
        @Query("limit") limit: Int = 20,
    ): Response<CompetitionScorersModelDTO>

    @GET("competitions/{competitionId}/matches")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getCompetitionMatches(
        @Path("competitionId") competitionId: String,
    ): Response<MatchesDTO>

    @GET("teams/{teamId}/matches/")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getTeamMatches(
        @Path("teamId") teamId: String,
    ): Response<MatchesDTO>

    @GET("matches/{matchId}/head2head")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getHead2headById(
        @Path("matchId") matchId: String,
    ): Response<Head2headDTO>

    @GET("teams/{teamId}")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getTeamInfoById(
        @Path("teamId") teamId: String,
    ): Response<TeamInfoDTO>

    @GET("persons/{personId}")
    @Headers("X-Auth-Token: $FOOTBALL_API_KEY")
    suspend fun getPersonInfo(
        @Path("personId") personId: String,
    ): Response<PersonInfoDTO>
}
