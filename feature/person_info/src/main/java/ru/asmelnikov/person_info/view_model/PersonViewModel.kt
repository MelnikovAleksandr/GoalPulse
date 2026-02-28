package ru.asmelnikov.person_info.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.models.Person
import ru.asmelnikov.domain.repository.PersonRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.getErrorMessage

class PersonViewModel(
    private val repository: PersonRepository,
    private val stringResourceProvider: StringResourceProvider,
    private val personId: String,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<PersonState, PersonSideEffects> {

    override val container = container<PersonState, PersonSideEffects>(
        initialState = PersonState(),
        savedStateHandle = savedStateHandle
    ) {
        reduce { state.copy(personId = personId) }
        getPersonFromRemote()
    }

    fun getPersonFromRemote() = intent {
        reduce { state.copy(isLoading = true) }
        when (val person =
            repository.getPersonInfo(
                personId = state.personId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoading = false,
                        person = person.data ?: Person(),
                    )
                }
            }

            is Resource.Error -> {
                handleError(person.httpErrors)
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp?) = intent {
        reduce {
            state.copy(
                isLoading = false
            )
        }

        postSideEffect(
            PersonSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }

}