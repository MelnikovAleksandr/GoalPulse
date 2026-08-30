package ru.asmelnikov.data.repository

import ru.asmelnikov.data.api.FootballApi
import ru.asmelnikov.data.mappers.toPerson
import ru.asmelnikov.data.retrofit_errors_handler.RetrofitErrorsHandler
import ru.asmelnikov.domain.models.Person
import ru.asmelnikov.domain.repository.PersonRepository
import ru.asmelnikov.utils.Resource

class PersonRepositoryImpl(
    private val footballApi: FootballApi,
    private val retrofitErrorsHandler: RetrofitErrorsHandler,
) : PersonRepository {
    override suspend fun getPersonInfo(personId: String): Resource<Person> = retrofitErrorsHandler.executeSafely {
        val response = footballApi.getPersonInfo(personId)
        if (response.isSuccessful && response.code() == 200) {
            Resource.Success(response.body()?.toPerson() ?: Person())
        } else {
            retrofitErrorsHandler.responseFailureHandler(response)
        }
    }
}
