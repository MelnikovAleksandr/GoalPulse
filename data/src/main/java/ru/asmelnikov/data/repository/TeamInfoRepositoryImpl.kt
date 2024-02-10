package ru.asmelnikov.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.TeamInfoRealmOptions
import ru.asmelnikov.data.local.models.CompetitionStandingsEntity
import ru.asmelnikov.data.local.models.TeamInfoEntity
import ru.asmelnikov.data.mappers.toCompetitionStandings
import ru.asmelnikov.data.mappers.toCompetitionStandingsEntity
import ru.asmelnikov.data.mappers.toTeamInfo
import ru.asmelnikov.data.mappers.toTeamInfoEntity
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.repository.TeamInfoRepository
import ru.asmelnikov.utils.Resource

class TeamInfoRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: TeamInfoRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : TeamInfoRepository {
    override suspend fun getTeamInfoById(teamId: String): Resource<TeamInfo> {
        return retrofitErrorsHandler.executeSafely {
            val response =
                footballApi.getTeamInfoById(teamId)
            if (response.isSuccessful && response.code() == 200) {
                val team = response.body()?.toTeamInfoEntity()
                realmOptions.upsertTeamInfoFromRemoteToLocal(
                    team ?: TeamInfoEntity()
                )
                Resource.Success(team?.toTeamInfo() ?: TeamInfo())
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getTeamInfoByIdFlowFromLocal(teamId: String): Flow<TeamInfo> {
        return realmOptions.getTeamInfoFlowById(
            teamId = teamId
        ).map {
            it.toTeamInfo()
        }
    }
}