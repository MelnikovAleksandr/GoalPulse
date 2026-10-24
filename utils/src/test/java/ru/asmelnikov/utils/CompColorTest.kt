package ru.asmelnikov.utils

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.asmelnikov.utils.ui.theme.topGreen

class CompColorTest {

    @Test
    fun worldCupGroupTable_highlightsTopPlaces() {
        assertEquals(topGreen, "WC".getCompColor(index = 0, listSize = 4, isGroupTable = true))
    }

    @Test
    fun worldCupTableWithoutGroups_isNotHighlighted() {
        assertEquals(Color.Transparent, "WC".getCompColor(index = 0, listSize = 48, isGroupTable = false))
    }

    @Test
    fun leagueTableWithoutGroups_keepsHighlight() {
        assertEquals(topGreen, "PL".getCompColor(index = 0, listSize = 20, isGroupTable = false))
    }
}
