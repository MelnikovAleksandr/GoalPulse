package ru.asmelnikov.utils

import androidx.compose.ui.graphics.Color
import ru.asmelnikov.utils.CompetitionType.*
import ru.asmelnikov.utils.ui.theme.lastRed
import ru.asmelnikov.utils.ui.theme.secondGreen
import ru.asmelnikov.utils.ui.theme.topGreen

object Constants {

    const val FOOTBALL_API_KEY = "8c02a856ac284753b67eacd7ab31d010"
    const val NEW_API_KEY = "30902f38d91440588b9c1a0b43035e83"
}

enum class CompetitionType(
    val type: String,
    val top: Int = 0,
    val second: Int = 0,
    val last: Int = 0,
) {
    BSA("BSA"), // todo
    ELC("ELC", top = 1, second = 5, last = 3),
    PL("PL", top = 3, second = 4, last = 3),
    CL("CL", top = 7, second = 23, last = 12),
    EC("EC", top = 1, second = 2),
    FL1("FL1", top = 3, second = 4, last = 3),
    BL1("BL1", top = 3, second = 4, last = 3),
    SA("SA", top = 3, second = 4, last = 3),
    DED("DED", top = 2, second = 3, last = 3),
    PPL("PPL", top = 1, second = 2, last = 3),
    CLI("CLI", top = 1, second = 2),
    PD("PD", top = 3, second = 4, last = 3),
    WC("WC", top = 1)
}

fun CompetitionType.getColor(index: Int, listSize: Int): Color {
    return when {
        this.type == BSA.type -> Color.Transparent
        index <= top -> topGreen
        index in (top + 1)..second -> secondGreen
        index >= listSize - last -> lastRed
        else -> Color.Transparent
    }
}

fun String.getCompColor(index: Int, listSize: Int): Color {
    return when (val compType = when (this) {
        "BSA" -> BSA
        "ELC" -> ELC
        "PL" -> PL
        "CL" -> CL
        "EC" -> EC
        "FL1" -> FL1
        "BL1" -> BL1
        "SA" -> SA
        "DED" -> DED
        "PPL" -> PPL
        "CLI" -> CLI
        "PD" -> PD
        "WC" -> WC
        else -> BSA
    }) {
        BSA -> Color.Transparent
        else -> compType.getColor(index, listSize)
    }
}

fun getDescription(stage: String?): String {
    return when (stage) {
        "FINAL" -> "Final"
        "THIRD_PLACE" -> "Third Place"
        "SEMI_FINALS" -> "Semi Finals"
        "QUARTER_FINALS" -> "Quarter Finals"
        "LAST_16" -> "Round of 16"
        "LAST_32" -> "Round of 32"
        "LAST_64" -> "Round of 64"
        "ROUND_4" -> "Round 4"
        "ROUND_3" -> "Round 3"
        "ROUND_2" -> "Round 2"
        "ROUND_1" -> "Round 1"
        "GROUP_STAGE" -> "Group Stage"
        "PRELIMINARY_ROUND" -> "Preliminary Round"
        "QUALIFICATION" -> "Qualification"
        "QUALIFICATION_ROUND_1" -> "Qualification Round 1"
        "QUALIFICATION_ROUND_2" -> "Qualification Round 2"
        "QUALIFICATION_ROUND_3" -> "Qualification Round 3"
        "PLAYOFF_ROUND_1" -> "Playoff Round 1"
        "PLAYOFF_ROUND_2" -> "Playoff Round 2"
        "PLAYOFFS" -> "Playoffs"
        "REGULAR_SEASON" -> "Regular Season"
        "CLAUSURA" -> "Clausura"
        "APERTURA" -> "Apertura"
        "CHAMPIONSHIP" -> "Championship"
        "RELEGATION" -> "Relegation"
        "RELEGATION_ROUND" -> "Relegation Round"
        else -> stage ?: ""
    }
}

