package ru.asmelnikov.domain.repository

import ru.asmelnikov.domain.models.Person
import ru.asmelnikov.utils.Resource

interface PersonRepository {

    suspend fun getPersonInfo(personId: String): Resource<Person>
}
