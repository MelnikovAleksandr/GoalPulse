package ru.asmelnikov.competitions_main.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.Competition

@Immutable
@Parcelize
data class CompetitionsScreenState(
    val comps: List<Competition> = emptyList(),
    val isLoading: Boolean = false
) : Parcelable

sealed class CompetitionsScreenSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        CompetitionsScreenSideEffects()

    data class OnCompetitionNavigate(val compId: String) :
        CompetitionsScreenSideEffects()
}