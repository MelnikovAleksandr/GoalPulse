package ru.asmelnikov.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.StandingsRealmOptions
import ru.asmelnikov.data.local.models.MatchesEntity
import ru.asmelnikov.data.local.models.CompetitionScorersEntity
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.mappers.toCompetitionMatches
import ru.asmelnikov.data.mappers.toMatchesEntity
import ru.asmelnikov.data.mappers.toCompetitionScorers
import ru.asmelnikov.data.mappers.toCompetitionScorersEntity
import ru.asmelnikov.data.mappers.toCompetitionStandings
import ru.asmelnikov.data.mappers.toCompetitionStandingsEntity
import ru.asmelnikov.data.mappers.toHead2head
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.Matches
import ru.asmelnikov.domain.models.CompetitionScorers
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.utils.Resource

class CompetitionStandingsRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: StandingsRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : CompetitionStandingsRepository {

    override suspend fun getCompetitionStandingsFromRemoteToLocalById(
        compId: String
    ): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            retrofitErrorsHandler.executeSafely {
                val response =
                    footballApi.getCompetitionStandingById(compId)
                if (response.isSuccessful && response.code() == 200) {
                    val standings = response.body()?.toCompetitionStandingsEntity()
                    realmOptions.upsertStandingsFromRemoteToLocal(
                        standings ?: CompetitionStandingsEntity()
                    )
                    Resource.Success(true)
                } else {
                    retrofitErrorsHandler.responseFailureHandler(response)
                }
            }
        }
    }

    override suspend fun getStandingsFlowFromLocalById(compId: String): Flow<CompetitionStandings?> {
        return withContext(Dispatchers.IO) {
            realmOptions.getStandingsFlowById(compId).map { it.toCompetitionStandings() }
        }
    }

    override suspend fun getCompetitionTopScorersBySeason(
        compId: String
    ): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            retrofitErrorsHandler.executeSafely {
                val response =
                    footballApi.getCompetitionTopScorers(compId)
                if (response.isSuccessful && response.code() == 200) {
                    val comp = response.body()?.toCompetitionScorersEntity()
                    realmOptions.upsertScorersFromRemoteToLocal(
                        comp ?: CompetitionScorersEntity()
                    )
                    Resource.Success(true)
                } else {
                    retrofitErrorsHandler.responseFailureHandler(response)
                }
            }
        }
    }

    override suspend fun getScorersFlowFromLocal(compId: String): Flow<CompetitionScorers?> {
        return withContext(Dispatchers.IO) {
            realmOptions.getScorersFlowById(compId).map { it?.toCompetitionScorers() }
        }
    }

    override suspend fun getAllMatchesFromRemoteToLocal(
        compId: String
    ): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            retrofitErrorsHandler.executeSafely {
                val response =
                    footballApi.getCompetitionMatches(compId)
                if (response.isSuccessful && response.code() == 200) {
                    val matches = response.body()?.toMatchesEntity()
                    realmOptions.upsertMatchesFromRemoteToLocal(
                        matches ?: MatchesEntity()
                    )
                    Resource.Success(true)
                } else {
                    retrofitErrorsHandler.responseFailureHandler(response)
                }
            }
        }
    }

    override suspend fun getAllMatchesFlowFromLocal(compId: String): Flow<Matches?> {
        return withContext(Dispatchers.IO) {
            realmOptions.getMatchesFlowById(compId).map { it?.toCompetitionMatches() }
        }
    }

    override suspend fun getHead2headById(matchId: Int): Resource<Head2head> {
        return withContext(Dispatchers.IO) {
            retrofitErrorsHandler.executeSafely {
                val response =
                    footballApi.getHead2headById(matchId.toString())
                if (response.isSuccessful && response.code() == 200) {
                    val head2head = response.body()?.toHead2head(matchId)
                    Resource.Success(head2head ?: Head2head())
                } else {
                    retrofitErrorsHandler.responseFailureHandler(response)
                }
            }
        }
    }
}