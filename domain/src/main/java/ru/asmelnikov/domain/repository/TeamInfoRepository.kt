package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.CompetitionMatches
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.TeamMatches
import ru.asmelnikov.utils.Resource

interface TeamInfoRepository {

    suspend fun getTeamInfoById(
        teamId: String
    ): Resource<TeamInfo>

    suspend fun getTeamInfoByIdFlowFromLocal(
        teamId: String
    ): Flow<TeamInfo>

    suspend fun getTeamMatchesFromRemoteToLocal(
        teamId: String,
        season: String? = null
    ): Resource<TeamMatches>

    suspend fun getTeamMatchesFlowFromLocal(
        teamId: String,
    ): Flow<TeamMatches>
}