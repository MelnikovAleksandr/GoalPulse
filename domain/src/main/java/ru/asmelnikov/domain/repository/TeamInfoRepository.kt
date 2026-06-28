package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.Matches
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.TeamMatches
import ru.asmelnikov.utils.Resource

interface TeamInfoRepository {

    suspend fun getTeamInfoById(
        teamId: String
    ): Resource<Boolean>

    suspend fun getTeamInfoByIdFlowFromLocal(
        teamId: String
    ): Flow<TeamInfo?>

    suspend fun getTeamMatchesFromRemoteToLocal(
        teamId: String
    ): Resource<Boolean>

    suspend fun getTeamMatchesFlowFromLocal(
        teamId: String
    ): Flow<TeamMatches?>
}