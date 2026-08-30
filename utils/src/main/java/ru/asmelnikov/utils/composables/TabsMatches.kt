package ru.asmelnikov.utils.composables

import androidx.annotation.StringRes
import ru.asmelnikov.utils.R

enum class TabsMatches(@StringRes val stringResId: Int) {
    Completed(R.string.tab_matches_completed),
    Ahead(R.string.tab_matches_ahead),
}
