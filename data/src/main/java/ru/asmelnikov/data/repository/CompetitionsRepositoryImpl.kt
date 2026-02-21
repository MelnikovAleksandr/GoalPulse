package ru.asmelnikov.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.local.CompetitionsRealmOptions
import ru.asmelnikov.data.mappers.toCompetition
import ru.asmelnikov.data.mappers.toCompetitionEntity
import ru.asmelnikov.data.models.CompetitionModelDTO
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.Competition
import ru.asmelnikov.domain.repository.CompetitionsRepository
import ru.asmelnikov.utils.Resource

class CompetitionsRepositoryImpl(
    private val footballApi: FootballApi,
    private val realmOptions: CompetitionsRealmOptions,
    private val retrofitErrorsHandler: RetrofitErrorsHandler
) : CompetitionsRepository {

    override suspend fun getAllCompetitionsFromRemoteToLocal(): Resource<Boolean> {
        return retrofitErrorsHandler.executeSafely {
            val response: Response<CompetitionModelDTO> = footballApi.getAllFootballCompetitions()
            if (response.isSuccessful && response.code() == 200) {
                val competitions = response.body()?.competitions?.map {
                    it.toCompetitionEntity()
                } ?: emptyList()
                realmOptions.upsertCompetitionsDataFromRemoteToLocal(competitions)
                Resource.Success(true)
            } else {
                retrofitErrorsHandler.responseFailureHandler(response)
            }
        }
    }

    override suspend fun getAllCompetitionsFlowFromLocal(): Flow<List<Competition>> {
        return realmOptions.getCompetitionsFlowFromLocal()
            .map { comps -> comps.map { comp -> comp.toCompetition() } }
    }
}