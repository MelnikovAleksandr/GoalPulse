package ru.asmelnikov.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.TeamInfoRealmOptions
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.mappers.toMatchesEntity
import ru.asmelnikov.data.mappers.toTeamInfo
import ru.asmelnikov.data.mappers.toTeamInfoEntity
import ru.asmelnikov.data.mappers.toTeamMatches
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.TeamMatches
import ru.asmelnikov.domain.repository.TeamInfoRepository
import ru.asmelnikov.utils.Resource

class TeamInfoRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: TeamInfoRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler,
) : TeamInfoRepository {
    override suspend fun getTeamInfoById(teamId: String): Resource<Boolean> = retrofitErrorsHandler.executeSafely {
        val response =
            footballApi.getTeamInfoById(teamId)
        if (response.isSuccessful && response.code() == 200) {
            val team = response.body()?.toTeamInfoEntity()
            realmOptions.upsertTeamInfoFromRemoteToLocal(
                team ?: TeamInfoEntity(),
            )
            Resource.Success(true)
        } else {
            retrofitErrorsHandler.responseFailureHandler(response)
        }
    }

    override suspend fun getTeamInfoByIdFlowFromLocal(teamId: String): Flow<TeamInfo?> =
        realmOptions.getTeamInfoFlowById(
            teamId = teamId,
        ).map {
            it?.toTeamInfo()
        }

    override suspend fun getTeamMatchesFromRemoteToLocal(
        teamId: String,
    ): Resource<Boolean> = retrofitErrorsHandler.executeSafely {
        val response =
            footballApi.getTeamMatches(teamId)
        if (response.isSuccessful && response.code() == 200) {
            response.body()?.toMatchesEntity(teamId = teamId)?.let { matches ->
                realmOptions.upsertMatchesFromRemoteToLocal(matches = matches)
            }
            Resource.Success(true)
        } else {
            retrofitErrorsHandler.responseFailureHandler(response)
        }
    }

    override suspend fun getTeamMatchesFlowFromLocal(teamId: String): Flow<TeamMatches?> =
        realmOptions.getMatchesFlowById(
            teamId,
        ).map {
            it?.toTeamMatches()
        }
}
