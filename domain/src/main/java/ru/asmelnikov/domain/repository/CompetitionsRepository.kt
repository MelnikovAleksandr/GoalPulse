package ru.asmelnikov.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.utils.Resource

interface CompetitionsRepository {

    suspend fun getAllCompetitionsFromRemoteToLocal(): Resource<Boolean>

    suspend fun getAllCompetitionsFlowFromLocal(): Flow<List<Competition>>
}
