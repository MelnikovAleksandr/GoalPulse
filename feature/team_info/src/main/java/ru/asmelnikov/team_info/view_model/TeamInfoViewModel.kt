package ru.asmelnikov.team_info.view_model

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.repository.CompetitionStandingsRepository
import ru.asmelnikov.domain.repository.MatchCalendarRepository
import ru.asmelnikov.domain.repository.NewsRepository
import ru.asmelnikov.domain.repository.TeamInfoRepository
import ru.asmelnikov.utils.ErrorsTypesHttp
import ru.asmelnikov.utils.R
import ru.asmelnikov.utils.Resource
import ru.asmelnikov.utils.StringResourceProvider
import ru.asmelnikov.utils.getErrorMessage

class TeamInfoViewModel(
    private val teamRepository: TeamInfoRepository,
    private val stringResourceProvider: StringResourceProvider,
    private val standingsRepository: CompetitionStandingsRepository,
    private val newsRepository: NewsRepository,
    private val matchCalendarRepository: MatchCalendarRepository,
    private val teamId: String
) : ViewModel(),
    ContainerHost<TeamInfoState, TeamInfoSideEffects> {

    override val container = container<TeamInfoState, TeamInfoSideEffects>(
        initialState = TeamInfoState()
    ) {
        reduce { state.copy(teamId = teamId) }
        collectTeamInfoFlowFromLocal()
        getTeamInfoFromRemoteToLocal()
        collectTeamMatchesFlowFromLocal()
        getTeamMatchesFromRemoteToLocal()
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
                reduce {
                    state.copy(
                        isHead2headLoading = false
                    )
                }
                handleError(head2head.httpErrors)
            }
        }
    }

    fun onPersonClick(personId: Int) = intent {
        postSideEffect(TeamInfoSideEffects.OnPersonInfoNavigate(personId.toString()))
    }

    fun onCalendarClick(match: Match) = intent {
        if (state.calendarBusyMatchIds.contains(match.id)) return@intent
        if (matchCalendarRepository.hasCalendarPermission()) {
            toggleCalendarEvent(match)
            return@intent
        }
        reduce { state.copy(pendingCalendarMatch = match) }
        postSideEffect(TeamInfoSideEffects.RequestCalendarPermission)
    }

    fun onCalendarPermissionResult(granted: Boolean) = intent {
        val match = state.pendingCalendarMatch
        reduce { state.copy(pendingCalendarMatch = null) }
        if (!granted) return@intent
        if (match != null) {
            toggleCalendarEvent(match)
        } else {
            val scheduledIds = scheduledMatchIds(state.matchesAhead.map { it.id })
            reduce { state.copy(calendarMatchIds = scheduledIds) }
        }
    }

    fun syncCalendarEvents() = intent {
        val scheduledIds = scheduledMatchIds(state.matchesAhead.map { it.id })
        reduce { state.copy(calendarMatchIds = scheduledIds) }
    }

    fun getTeamInfoFromRemoteToLocal() = intent {
        reduce { state.copy(isInfoLoading = true) }
        when (val team =
            teamRepository.getTeamInfoById(
                state.teamId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isInfoLoading = false
                    )
                }
            }

            is Resource.Error -> {
                reduce {
                    state.copy(
                        isInfoLoading = false
                    )
                }
                handleError(team.httpErrors)
            }
        }
    }

    fun getTeamMatchesFromRemoteToLocal() = intent {
        reduce { state.copy(isMatchesLoading = true) }
        when (val matches =
            teamRepository.getTeamMatchesFromRemoteToLocal(
                teamId = state.teamId
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isMatchesLoading = false
                    )
                }
            }

            is Resource.Error -> {
                reduce {
                    state.copy(
                        isMatchesLoading = false
                    )
                }
                handleError(matches.httpErrors)
            }
        }
    }

    fun onBackClick() = intent {
        postSideEffect(TeamInfoSideEffects.BackClick)
    }

    private fun collectTeamInfoFlowFromLocal() = intent {
        teamRepository.getTeamInfoByIdFlowFromLocal(state.teamId).collect { teamInfo ->
            reduce {
                state.copy(
                    teamInfo = teamInfo ?: TeamInfo()
                )
            }
            if (!teamInfo?.name.isNullOrEmpty()) getNews()
        }
    }

    private fun collectTeamMatchesFlowFromLocal() = intent {
        teamRepository.getTeamMatchesFlowFromLocal(state.teamId).collect { matches ->
            val ahead = matches?.matchesAhead ?: emptyList()
            val scheduledIds = scheduledMatchIds(ahead.map { it.id })
            reduce {
                state.copy(
                    matchesComplete = matches?.matchesCompleted ?: emptyList(),
                    matchesAhead = ahead,
                    calendarMatchIds = scheduledIds
                )
            }
        }
    }

    private fun toggleCalendarEvent(match: Match) = intent {
        reduce { state.copy(calendarBusyMatchIds = state.calendarBusyMatchIds + match.id) }
        val result = if (state.calendarMatchIds.contains(match.id)) {
            matchCalendarRepository.removeMatch(match.id)
        } else {
            matchCalendarRepository.addMatch(match)
        }
        val scheduledIds = scheduledMatchIds(state.matchesAhead.map { it.id })
        reduce {
            state.copy(
                calendarBusyMatchIds = state.calendarBusyMatchIds - match.id,
                calendarMatchIds = scheduledIds
            )
        }
        if (result is Resource.Error) {
            postSideEffect(
                TeamInfoSideEffects.Snackbar(
                    stringResourceProvider.getString(R.string.calendar_event_failed)
                )
            )
        }
    }

    private suspend fun scheduledMatchIds(matchIds: Collection<Int>): Set<Int> {
        if (!matchCalendarRepository.hasCalendarPermission()) return emptySet()
        return matchCalendarRepository.findScheduledMatchIds(matchIds)
    }

    private fun getNews() = intent {
        reduce { state.copy(isNewsLoading = true) }
        when (val news =
            newsRepository.getNews(
                state.teamInfo.name
            )) {
            is Resource.Success -> {
                reduce {
                    state.copy(
                        isNewsLoading = false,
                        news = news.data ?: News()
                    )
                }
            }

            is Resource.Error -> {
                reduce {
                    state.copy(
                        isNewsLoading = false
                    )
                }
                handleError(news.httpErrors)
            }
        }
    }

    private fun handleError(error: ErrorsTypesHttp?) = intent {
        postSideEffect(
            TeamInfoSideEffects.Snackbar(
                error.getErrorMessage(
                    stringResourceProvider
                )
            )
        )
    }

}