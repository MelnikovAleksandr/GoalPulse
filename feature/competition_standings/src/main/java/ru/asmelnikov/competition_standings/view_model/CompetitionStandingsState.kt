package ru.asmelnikov.competition_standings.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Scorer

@Immutable
@Parcelize
data class CompetitionStandingsState(
    val compId: String = "",
    val compUrl: String = "",
    val competitionStandings: CompetitionStandings? = null,
    val matchesCompleted: List<MatchesByTour> = emptyList(),
    val matchesAhead: List<MatchesByTour> = emptyList(),
    val isLoadingStandings: Boolean = true,
    val isLoadingScorers: Boolean = true,
    val isLoadingMatches: Boolean = true,
    val scorers: List<Scorer> = emptyList(),
    val expandedItem: Int = -1,
    val head2head: Head2head = Head2head(),
    val isHead2headLoading: Boolean = false
) : Parcelable

sealed class CompetitionStandingSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        CompetitionStandingSideEffects()

    data object BackClick : CompetitionStandingSideEffects()

    data class OnTeamInfoNavigate(val teamId: String) :
        CompetitionStandingSideEffects()

    data class OnPersonInfoNavigate(val personId: String) :
        CompetitionStandingSideEffects()

}
