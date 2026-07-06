package ru.asmelnikov.domain.models

import androidx.annotation.StringRes
import ru.asmelnikov.domain.models.Group.NON
import ru.asmelnikov.utils.R

enum class Stage(@StringRes val stringResId: Int) {
    LEAGUE_STAGE(R.string.stage_league),
    FINAL(R.string.stage_final),
    THIRD_PLACE(R.string.stage_third_place),
    SEMI_FINALS(R.string.stage_semi_finals),
    QUARTER_FINALS(R.string.stage_quarter_finals),
    LAST_16(R.string.stage_last_16),
    LAST_32(R.string.stage_last_32),
    LAST_64(R.string.stage_last_64),
    ROUND_4(R.string.stage_round_4),
    ROUND_3(R.string.stage_round_3),
    ROUND_2(R.string.stage_round_2),
    ROUND_1(R.string.stage_round_1),
    GROUP_STAGE(R.string.stage_group_stage),
    PRELIMINARY_ROUND(R.string.stage_preliminary_round),
    QUALIFICATION(R.string.stage_qualification),
    QUALIFICATION_ROUND_1(R.string.stage_qualification_round_1),
    QUALIFICATION_ROUND_2(R.string.stage_qualification_round_2),
    QUALIFICATION_ROUND_3(R.string.stage_qualification_round_3),
    PLAYOFF_ROUND_1(R.string.stage_playoff_round_1),
    PLAYOFF_ROUND_2(R.string.stage_playoff_round_2),
    PLAYOFFS(R.string.stage_playoffs),
    PLAY_OFFS(R.string.stage_playoffs),
    REGULAR_SEASON(R.string.stage_regular_season),
    CLAUSURA(R.string.stage_clausura),
    APERTURA(R.string.stage_apertura),
    CHAMPIONSHIP(R.string.stage_championship),
    RELEGATION(R.string.stage_relegation),
    RELEGATION_ROUND(R.string.stage_relegation_round),
    NON(R.string.non);

    companion object {
        fun safeValueOf(value: String): Stage {
            return runCatching { valueOf(value) }.getOrDefault(NON)
        }
    }
}

enum class MatchStatus(@StringRes val stringResId: Int) {
    SCHEDULED(R.string.match_status_scheduled),
    TIMED(R.string.match_status_scheduled),
    LIVE(R.string.match_status_live),
    IN_PLAY(R.string.match_status_in_play),
    PAUSED(R.string.match_status_paused),
    FINISHED(R.string.match_status_finished),
    POSTPONED(R.string.match_status_postponed),
    SUSPENDED(R.string.match_status_suspended),
    CANCELLED(R.string.match_status_cancelled),
    NON(R.string.non);

    companion object {
        fun safeValueOf(value: String): MatchStatus {
            return runCatching { valueOf(value) }.getOrDefault(NON)
        }
    }
}

enum class PlayerPosition(@StringRes val stringResId: Int, val value: String) {
    GOALKEEPER(R.string.position_goalkeeper, "Goalkeeper"),
    DEFENCE(R.string.position_defence, "Defence"),
    CENTER_BACK(R.string.position_center_back, "Centre-Back"),
    LEFT_BACK(R.string.position_left_back, "Left-Back"),
    RIGHT_BACK(R.string.position_right_back, "Right-Back"),
    DEFENSIVE_MIDFIELD(R.string.position_defensive_midfield, "Defensive Midfield"),
    CENTRAL_MIDFIELD(R.string.position_central_midfield, "Central Midfield"),
    ATTACKING_MIDFIELD(R.string.position_attacking_midfield, "Attacking Midfield"),
    RIGHT_MIDFIELD(R.string.position_right_midfield, "Right Midfield"),
    LEFT_MIDFIELD(R.string.position_left_midfield, "Left Midfield"),
    MIDFIELD(R.string.position_midfield, "Midfield"),
    LEFT_WINGER(R.string.position_left_winger, "Left Winger"),
    RIGHT_WINGER(R.string.position_right_winger, "Right Winger"),
    OFFENCE(R.string.position_offence, "Offence"),
    CENTRE_FORWARD(R.string.position_centre_forward, "Centre-Forward"),
    NON(R.string.non, "Non");

    companion object {
        fun fromValue(value: String?): PlayerPosition {
            return entries.find { it.value == value } ?: NON
        }
    }
}

enum class TournamentType(@StringRes val stringResId: Int) {
    LEAGUE(R.string.tournament_type_league),
    LEAGUE_CUP(R.string.tournament_type_league_cup),
    CUP(R.string.tournament_type_cup),
    PLAYOFFS(R.string.tournament_type_playoffs),
    NON(R.string.non);

    companion object {
        fun safeValueOf(value: String): TournamentType {
            return runCatching { valueOf(value) }.getOrDefault(NON)
        }
    }
}

enum class Group(@StringRes val stringResId: Int) {
    GROUP_A(R.string.group_a),
    GROUP_B(R.string.group_b),
    GROUP_C(R.string.group_c),
    GROUP_D(R.string.group_d),
    GROUP_E(R.string.group_e),
    GROUP_F(R.string.group_f),
    GROUP_G(R.string.group_g),
    GROUP_H(R.string.group_h),
    GROUP_I(R.string.group_i),
    GROUP_J(R.string.group_j),
    GROUP_K(R.string.group_k),
    GROUP_L(R.string.group_l),
    NON(R.string.header_team);

    companion object {
        fun safeValueOf(value: String): Group {
            return runCatching { valueOf(value) }.getOrDefault(NON)
        }
    }
}

enum class Winner(@StringRes val stringResId: Int) {
    HOME_TEAM(R.string.winner_home_team),
    AWAY_TEAM(R.string.winner_away_team),
    DRAW(R.string.winner_draw),
    NON(R.string.non);

    companion object {
        fun safeValueOf(value: String): Winner {
            return runCatching { valueOf(value) }.getOrDefault(NON)
        }
    }
}