package ru.asmelnikov.person_info.view_model

import android.os.Parcelable
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import ru.asmelnikov.domain.models.Person

@Immutable
@Parcelize
data class PersonState(
    val isLoading: Boolean = false,
    val person: Person = Person(),
    val personId: String = ""
) : Parcelable

sealed class PersonSideEffects {
    data class Snackbar(val text: String, val duration: SnackbarDuration = SnackbarDuration.Short) :
        PersonSideEffects()

    object BackClick : PersonSideEffects()
}
