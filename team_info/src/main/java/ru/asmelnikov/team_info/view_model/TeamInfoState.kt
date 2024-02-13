package ru.asmelnikov.team_info.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.Match
import ru.asmelnikov.domain.models.TeamInfo
import ru.asmelnikov.domain.models.TeamMatches

@Immutable
@Parcelize
data class TeamInfoState(
    val teamId: String = "",
    val teamInfo: TeamInfo = TeamInfo(),
    val isInfoLoading: Boolean = false,
    val isMatchesLoading: Boolean = false,
    val colorPalette: Map<String, String> = mapOf(),
    val matchesComplete: List<Match> = emptyList(),
    val matchesAhead: List<Match> = emptyList(),
    val expandedItem: Int = -1,
    val head2head: Head2head = Head2head(),
    val isHead2headLoading: Boolean = false
) : Parcelable

sealed class TeamInfoSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        TeamInfoSideEffects()

    object BackClick : TeamInfoSideEffects()

}
