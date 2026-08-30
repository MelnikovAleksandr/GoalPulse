package ru.asmelnikov.person_info.view_model

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.orbitmvi.orbit.test.test
import ru.asmelnikov.domain.models.Person
import ru.asmelnikov.domain.repository.PersonRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider

class PersonViewModelTest {

    @Test
    fun onOpen_showsPlayerForOpenedId() = runTest {
        val repository = FakePersonRepository(personById = mapOf("44" to saka))

        viewModel(repository).test(this) {
            runOnCreate()

            var screen = awaitState()
            while (screen.person.name != "Bukayo Saka") {
                screen = awaitState()
            }

            assertEquals("Bukayo Saka", screen.person.name)
            assertEquals(44, screen.person.id)
            assertEquals(false, screen.isLoading)
        }
    }

    @Test
    fun loadFails_hidesSpinner_showsError() = runTest {
        val repository = FakePersonRepository(
            error = ErrorsTypesHttp.Https400Errors(errorCode = 429),
        )

        viewModel(repository).test(this, PersonState(personId = "44")) {
            containerHost.getPersonFromRemote()

            expectState { copy(isLoading = true) }
            expectState { copy(isLoading = false) }
            expectSideEffect(PersonSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    @Test
    fun reloadFails_keepsAlreadyLoadedPlayer_showsError() = runTest {
        val repository = FakePersonRepository(
            error = ErrorsTypesHttp.Https400Errors(errorCode = 429),
        )

        viewModel(repository).test(
            this,
            PersonState(personId = "44", person = saka, isLoading = false),
        ) {
            containerHost.getPersonFromRemote()

            expectState { copy(isLoading = true) }
            expectState { copy(isLoading = false) }
            expectSideEffect(PersonSideEffects.Snackbar(RATE_LIMIT_MESSAGE))
        }
    }

    // region Helpers

    private fun viewModel(repository: FakePersonRepository) = PersonViewModel(
        repository = repository,
        stringResourceProvider = FakeStringResourceProvider(),
        personId = "44",
        savedStateHandle = SavedStateHandle(),
    )

    // endregion
}

// region Fakes

private class FakePersonRepository(
    private val personById: Map<String, Person> = emptyMap(),
    private val error: ErrorsTypesHttp? = null,
) : PersonRepository {

    override suspend fun getPersonInfo(personId: String): Resource<Person> {
        if (error != null) {
            return Resource.Error(httpErrors = error)
        }
        val person = personById[personId]
        return if (person != null) {
            Resource.Success(person)
        } else {
            Resource.Success(Person(id = 0, name = ""))
        }
    }
}

private class FakeStringResourceProvider : StringResourceProvider {
    override fun getString(resourceId: Int): String = when (resourceId) {
        R.string.http_429_errors -> RATE_LIMIT_MESSAGE
        else -> error("unexpected string resource $resourceId")
    }

    override fun getString(resourceId: Int, vararg arguments: Any): String {
        error("unexpected formatted string resource $resourceId")
    }
}

// endregion

// region Test data

private const val RATE_LIMIT_MESSAGE = "too many requests"

private val saka = Person(
    id = 44,
    name = "Bukayo Saka",
)

// endregion
