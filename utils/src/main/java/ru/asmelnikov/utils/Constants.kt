package ru.asmelnikov.utils

import androidx.compose.ui.graphics.Color
import ru.asmelnikov.utils.ui.theme.lastRed
import ru.asmelnikov.utils.ui.theme.secondGreen
import ru.asmelnikov.utils.ui.theme.topGreen

object Constants {

    const val FOOTBALL_API_KEY = BuildConfig.FOOTBALL_API_KEY
    const val NEW_API_KEY = BuildConfig.NEW_API_KEY
}

enum class CompetitionType(val type: String, val top: Int = 0, val second: Int = 0, val last: Int = 0) {
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
    WC("WC", top = 1),
}

fun CompetitionType.getColor(index: Int, listSize: Int): Color = when {
    this.type == CompetitionType.BSA.type -> Color.Transparent
    index <= top -> topGreen
    index in (top + 1)..second -> secondGreen
    index >= listSize - last -> lastRed
    else -> Color.Transparent
}

fun String.getCompColor(index: Int, listSize: Int): Color = when (
    val compType = when (this) {
        "BSA" -> CompetitionType.BSA
        "ELC" -> CompetitionType.ELC
        "PL" -> CompetitionType.PL
        "CL" -> CompetitionType.CL
        "EC" -> CompetitionType.EC
        "FL1" -> CompetitionType.FL1
        "BL1" -> CompetitionType.BL1
        "SA" -> CompetitionType.SA
        "DED" -> CompetitionType.DED
        "PPL" -> CompetitionType.PPL
        "CLI" -> CompetitionType.CLI
        "PD" -> CompetitionType.PD
        "WC" -> CompetitionType.WC
        else -> CompetitionType.BSA
    }
) {
    CompetitionType.BSA -> Color.Transparent
    else -> compType.getColor(index, listSize)
}
