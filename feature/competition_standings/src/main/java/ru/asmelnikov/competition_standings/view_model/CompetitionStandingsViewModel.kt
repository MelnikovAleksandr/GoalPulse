package ru.asmelnikov.competition_standings.view_model

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.domain.repository.MatchCalendarRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.getErrorMessage

class CompetitionStandingsViewModel(
    private val standingsRepository: CompetitionStandingsRepository,
    private val matchCalendarRepository: MatchCalendarRepository,
    private val stringResourceProvider: StringResourceProvider,
    private val compId: String,
    private val compUrl: String
) : ViewModel(),
    ContainerHost<CompetitionStandingsState, CompetitionStandingSideEffects> {

    override val container = container<CompetitionStandingsState, CompetitionStandingSideEffects>(
        initialState = CompetitionStandingsState()
    ) {
        reduce { state.copy(compId = compId, compUrl = compUrl) }
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
                        isHead2headLoading = false
                    )
                }
            }

            is Resource.Error -> {
                reduce { state.copy(isHead2headLoading = false) }
                handleError(head2head.httpErrors)
            }
        }
    }

    fun onPersonClick(personId: Int) = intent {
        postSideEffect(CompetitionStandingSideEffects.OnPersonInfoNavigate(personId = personId.toString()))
    }

    fun onCalendarClick(match: Match) = intent {
        if (state.calendarBusyMatchIds.contains(match.id)) return@intent
        if (!matchCalendarRepository.hasCalendarPermission()) {
            reduce { state.copy(pendingCalendarMatch = match) }
            postSideEffect(CompetitionStandingSideEffects.RequestCalendarPermission)
            return@intent
        }
        if (!state.calendarMatchIds.contains(match.id)) {
            val intent = matchCalendarRepository.insertIntent(match)
            if (intent == null) {
                postSideEffect(
                    CompetitionStandingSideEffects.Snackbar(
                        stringResourceProvider.getString(R.string.calendar_event_failed)
                    )
                )
                return@intent
            }
            postSideEffect(CompetitionStandingSideEffects.OpenCalendar(intent))
            return@intent
        }
        if (matchCalendarRepository.findEventId(match.id) == null) {
            val scheduledIds = scheduledMatchIds(aheadMatchIds(state.matchesAhead))
            reduce { state.copy(calendarMatchIds = scheduledIds) }
            postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(R.string.calendar_event_failed)
                )
            )
            return@intent
        }
        val intent = matchCalendarRepository.viewIntent(match)
        if (intent == null) {
            postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(R.string.calendar_event_failed)
                )
            )
            return@intent
        }
        postSideEffect(CompetitionStandingSideEffects.OpenCalendar(intent))
    }

    fun onCalendarPermissionResult(granted: Boolean) = intent {
        val match = state.pendingCalendarMatch
        if (!granted) {
            reduce { state.copy(pendingCalendarMatch = null) }
            return@intent
        }
        val scheduledIds = scheduledMatchIds(aheadMatchIds(state.matchesAhead))
        reduce {
            state.copy(
                pendingCalendarMatch = null,
                calendarMatchIds = scheduledIds
            )
        }
        if (match == null) return@intent
        if (!scheduledIds.contains(match.id)) {
            val intent = matchCalendarRepository.insertIntent(match)
            if (intent == null) {
                postSideEffect(
                    CompetitionStandingSideEffects.Snackbar(
                        stringResourceProvider.getString(R.string.calendar_event_failed)
                    )
                )
                return@intent
            }
            postSideEffect(CompetitionStandingSideEffects.OpenCalendar(intent))
            return@intent
        }
        if (matchCalendarRepository.findEventId(match.id) == null) {
            postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(R.string.calendar_event_failed)
                )
            )
            return@intent
        }
        val intent = matchCalendarRepository.viewIntent(match)
        if (intent == null) {
            postSideEffect(
                CompetitionStandingSideEffects.Snackbar(
                    stringResourceProvider.getString(R.string.calendar_event_failed)
                )
            )
            return@intent
        }
        postSideEffect(CompetitionStandingSideEffects.OpenCalendar(intent))
    }

    fun onCalendarInsertFailed() = intent {
        postSideEffect(
            CompetitionStandingSideEffects.Snackbar(
                stringResourceProvider.getString(R.string.calendar_event_failed)
            )
        )
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
                reduce {
                    state.copy(
                        isLoadingScorers = false
                    )
                }
                handleError(compsFromRemote.httpErrors)
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
                reduce {
                    state.copy(
                        isLoadingStandings = false
                    )
                }
                handleError(compsFromRemote.httpErrors)
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
                reduce {
                    state.copy(
                        isLoadingMatches = false
                    )
                }
                handleError(matchesFromRemote.httpErrors)
            }
        }
    }

    fun onTeamClick(teamId: Int) = intent {
        postSideEffect(CompetitionStandingSideEffects.OnTeamInfoNavigate(teamId = teamId.toString()))
    }


    private fun collectStandingsFlowFromLocal() = intent {
        standingsRepository.getStandingsFlowFromLocalById(state.compId).collect { standings ->
            if (standings != null) {
                reduce {
                    state.copy(
                        competitionStandings = standings
                    )
                }
            }
        }
    }

    private fun collectScorersFlowFromLocal() = intent {
        standingsRepository.getScorersFlowFromLocal(state.compId).collect { scorers ->
            reduce {
                state.copy(
                    scorers = scorers?.scorers ?: emptyList()
                )
            }
        }
    }

    private fun collectMatchesFlowFromLocal() = intent {
        repeatOnSubscription {
            standingsRepository.getAllMatchesFlowFromLocal(state.compId).collect { matches ->
                val ahead = matches?.matchesByTourAhead ?: emptyList()
                val scheduledIds = scheduledMatchIds(aheadMatchIds(ahead))
                reduce {
                    state.copy(
                        matchesCompleted = matches?.matchesByTourCompleted ?: emptyList(),
                        matchesAhead = ahead,
                        calendarMatchIds = scheduledIds
                    )
                }
            }
        }
    }

    private suspend fun scheduledMatchIds(matchIds: Collection<Int>): Set<Int> {
        if (!matchCalendarRepository.hasCalendarPermission()) return emptySet()
        return matchCalendarRepository.findScheduledMatchIds(matchIds)
    }

    private fun aheadMatchIds(tours: List<MatchesByTour>): List<Int> {
        return tours.flatMap { tour -> tour.matches.map { it.id } }
    }

    private fun handleError(error: ErrorsTypesHttp?) = intent {
        postSideEffect(
            CompetitionStandingSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }
}