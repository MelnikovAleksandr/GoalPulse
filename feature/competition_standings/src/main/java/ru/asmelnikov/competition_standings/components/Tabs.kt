package ru.asmelnikov.competition_standings.components

import androidx.annotation.StringRes
import ru.asmelnikov.utils.R

enum class TabsStandings(@StringRes val stringResId: Int) {
    Standings(R.string.tab_standings),
    Scorers(R.string.tab_scorers),
    Matches(R.string.tab_matches)
}

enum class TabsMatches(@StringRes val stringResId: Int) {
    Completed(R.string.tab_matches_completed),
    Ahead(R.string.tab_matches_ahead)
}