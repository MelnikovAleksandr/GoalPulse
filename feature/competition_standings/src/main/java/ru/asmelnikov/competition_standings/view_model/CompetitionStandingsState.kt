package ru.asmelnikov.competition_standings.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.CompetitionStandings
import ru.asmelnikov.domain.models.Head2head
import ru.asmelnikov.domain.models.MatchesByTour
import ru.asmelnikov.domain.models.Scorer
import ru.asmelnikov.domain.models.Season

@Immutable
@Parcelize
data class CompetitionStandingsState(
    val compId: String = "",
    val competitionStandings: CompetitionStandings? = null,
    val matchesCompleted: List<MatchesByTour> = emptyList(),
    val matchesAhead: List<MatchesByTour> = emptyList(),
    val isLoadingStandings: Boolean = false,
    val isLoadingScorers: Boolean = false,
    val isLoadingMatches: Boolean = false,
    val seasons: List<Season> = emptyList(),
    val currentSeasonStandings: String = "",
    val currentSeasonMatches: String = "",
    val scorers: List<Scorer> = emptyList(),
    val currentSeasonScorers: String = "",
    val expandedItem: Int = -1,
    val head2head: Head2head = Head2head(),
    val isHead2headLoading: Boolean = false
) : Parcelable

sealed class CompetitionStandingSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        CompetitionStandingSideEffects()

    object BackClick : CompetitionStandingSideEffects()

    data class OnTeamInfoNavigate(val teamId: String) :
        CompetitionStandingSideEffects()

    data class OnPersonInfoNavigate(val personId: String) :
        CompetitionStandingSideEffects()

}
