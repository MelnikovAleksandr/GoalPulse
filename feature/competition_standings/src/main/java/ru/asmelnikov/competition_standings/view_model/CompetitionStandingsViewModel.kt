package ru.asmelnikov.competition_standings.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.getErrorMessage

class CompetitionStandingsViewModel(
    private val standingsRepository: CompetitionStandingsRepository,
    private val stringResourceProvider: StringResourceProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ContainerHost<CompetitionStandingsState, CompetitionStandingSideEffects> {

    override val container = container<CompetitionStandingsState, CompetitionStandingSideEffects>(
        initialState = CompetitionStandingsState(),
        savedStateHandle = savedStateHandle
    ) {
        val compId = savedStateHandle.get<String>("compId")
        reduce { state.copy(compId = compId ?: "") }
        collectStandingsFlowFromLocal()
        collectScorersFlowFromLocal()
        collectMatchesFlowFromLocal()
        updateStandingsFromRemoteToLocal()
        updateScorersFromRemoteToLocal()
        updateMatchesFromRemoteToLocal()
    }

    fun matchItemClick(itemId: Int) = intent {
        if (state.expandedItem == itemId) {
            reduce { state.copy(expandedItem = -1) }
            return@intent
        }
        if (state.head2head.id == itemId) {
            reduce { state.copy(expandedItem = itemId) }
            return@intent
        }
        reduce { state.copy(expandedItem = itemId, isHead2headLoading = true) }
        when (val head2head =
            standingsRepository.getHead2headById(
                itemId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        head2head = head2head.data ?: Head2head(),
                        isHead2headLoading = false,
                    )
                }
            }

            is Resource.Error -> {
                handleError(head2head.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun onPersonClick(personId: Int) = intent {
        postSideEffect(CompetitionStandingSideEffects.OnPersonInfoNavigate(personId = personId.toString()))
    }

    fun onBackClick() = intent {
        postSideEffect(CompetitionStandingSideEffects.BackClick)
    }

    fun updateScorersFromRemoteToLocal() = intent {
        reduce { state.copy(isLoadingScorers = true) }
        when (val compsFromRemote =
            standingsRepository.getCompetitionTopScorersBySeason(
                state.compId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoadingScorers = false
                    )
                }
            }

            is Resource.Error -> {
                handleError(compsFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun updateStandingsFromRemoteToLocal() = intent {
        reduce { state.copy(isLoadingStandings = true) }
        when (val compsFromRemote =
            standingsRepository.getCompetitionStandingsFromRemoteToLocalById(
                state.compId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoadingStandings = false
                    )
                }
            }

            is Resource.Error -> {
                handleError(compsFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun updateMatchesFromRemoteToLocal() = intent {
        reduce { state.copy(isLoadingMatches = true) }
        when (val matchesFromRemote =
            standingsRepository.getAllMatchesFromRemoteToLocal(
                state.compId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isLoadingMatches = false
                    )
                }
            }

            is Resource.Error -> {
                handleError(matchesFromRemote.httpErrors ?: ErrorsTypesHttp.UnknownError())
            }
        }
    }

    fun onTeamClick(teamId: Int) = intent {
        postSideEffect(CompetitionStandingSideEffects.OnTeamInfoNavigate(teamId = teamId.toString()))
    }


    private fun collectStandingsFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            standingsRepository.getStandingsFlowFromLocalById(state.compId).collect { standings ->
                reduce {
                    state.copy(
                        competitionStandings = standings
                    )
                }
            }
        }
    }

    private fun collectScorersFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            standingsRepository.getScorersFlowFromLocal(state.compId).collect { scorers ->
                reduce {
                    state.copy(
                        scorers = scorers?.scorers ?: emptyList(),
                        currentSeasonScorers = scorers?.season?.startDateEndDate ?: ""
                    )
                }
            }
        }
    }

    private fun collectMatchesFlowFromLocal() = intent(registerIdling = false) {
        repeatOnSubscription {
            standingsRepository.getAllMatchesFlowFromLocal(state.compId).collect { matches ->
                reduce {
                    state.copy(
                        matchesCompleted = matches?.matchesByTourCompleted ?: emptyList(),
                        matchesAhead = matches?.matchesByTourAhead ?: emptyList()
                    )
                }
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp) = intent {
        reduce {
            state.copy(
                isLoadingStandings = false,
                isLoadingScorers = false,
                isLoadingMatches = false,
                isHead2headLoading = false
            )
        }

        postSideEffect(
            CompetitionStandingSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }
}