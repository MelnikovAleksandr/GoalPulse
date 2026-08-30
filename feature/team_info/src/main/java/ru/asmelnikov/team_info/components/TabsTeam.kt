package ru.asmelnikov.team_info.components

import androidx.annotation.StringRes
import ru.asmelnikov.utils.R

enum class TabsTeam(@StringRes val stringResId: Int) {
    Squad(R.string.team_tab_squad),
    Info(R.string.team_tab_info),
    Matches(R.string.team_tab_matches),
}
