package ru.asmelnikov.team_info.view_model

import android.content.Intent
import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.News
import ru.asmelnikov.domain.models.TeamInfo

@Immutable
@Parcelize
data class TeamInfoState(
    val teamId: String = "",
    val teamInfo: TeamInfo = TeamInfo(),
    val isInfoLoading: Boolean = true,
    val isMatchesLoading: Boolean = true,
    val matchesComplete: List<Match> = emptyList(),
    val matchesAhead: List<Match> = emptyList(),
    val expandedItem: Int = -1,
    val head2head: Head2head = Head2head(),
    val isHead2headLoading: Boolean = false,
    val isNewsLoading: Boolean = false,
    val news: News = News(),
    val calendarMatchIds: Set<Int> = emptySet(),
    val calendarBusyMatchIds: Set<Int> = emptySet(),
    val pendingCalendarMatch: Match? = null
) : Parcelable

sealed class TeamInfoSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        TeamInfoSideEffects()

    data object BackClick : TeamInfoSideEffects()

    data class OnPersonInfoNavigate(val personId: String) :
        TeamInfoSideEffects()

    data object RequestCalendarPermission : TeamInfoSideEffects()

    data class OpenCalendar(val intent: Intent) : TeamInfoSideEffects()
}
