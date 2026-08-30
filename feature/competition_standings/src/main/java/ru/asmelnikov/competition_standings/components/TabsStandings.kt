package ru.asmelnikov.competition_standings.components

import androidx.annotation.StringRes
import ru.asmelnikov.utils.R

enum class TabsStandings(@StringRes val stringResId: Int) {
    Standings(R.string.tab_standings),
    Scorers(R.string.tab_scorers),
    Matches(R.string.tab_matches),
}
