package ru.asmelnikov.competitions_main.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.repository.CompetitionsRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.getErrorMessage

class CompetitionsScreenViewModel(
    private val footballRepository: CompetitionsRepository,
    private val stringResourceProvider: StringResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ContainerHost<CompetitionsScreenState, CompetitionsScreenSideEffects> {

    override val container = container<CompetitionsScreenState, CompetitionsScreenSideEffects>(
        initialState = CompetitionsScreenState(),
        savedStateHandle = savedStateHandle
    ) {
        collectCompetitionsFlowFromLocal()
        updateCompetitionsFromRemoteToLocal()
    }

    fun updateCompetitionsFromRemoteToLocal() = intent {
        reduce { state.copy(isLoading = true) }
        when (val compsFromRemote = footballRepository.getAllCompetitionsFromRemoteToLocal()) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoading = false
                    )
                }
            }

            is Resource.Error -> {
                handleError(compsFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun onCompClick(compId: String) = intent {
        postSideEffect(CompetitionsScreenSideEffects.OnCompetitionNavigate(compId = compId))
    }

    private fun collectCompetitionsFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            footballRepository.getAllCompetitionsFlowFromLocal().collect { comps ->
                if (comps.isNotEmpty())
                    reduce { state.copy(comps = comps) }
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce { state.copy(isLoading = false) }

        postSideEffect(
            CompetitionsScreenSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }

}