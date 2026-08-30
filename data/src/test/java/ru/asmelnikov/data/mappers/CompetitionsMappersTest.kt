package ru.asmelnikov.data.mappers

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.asmelnikov.data.local.models.CurrentSeasonEntity

class CompetitionsMappersTest {

    @Test
    fun toCurrentSeason_buildsYearRangeFromStartAndEndDates() {
        val season = seasonEntity(
            startDate = "2025-08-15",
            endDate = "2026-05-24",
        )

        assertEquals("2025/2026", season.toCurrentSeason().startDateEndDate)
    }

    @Test
    fun toCurrentSeason_keepsSameYearWhenSeasonDoesNotCrossNewYear() {
        val season = seasonEntity(
            startDate = "2024-02-01",
            endDate = "2024-11-30",
        )

        assertEquals("2024/2024", season.toCurrentSeason().startDateEndDate)
    }

    @Test
    fun toCurrentSeason_returnsEmptyYearRange_whenDatesAreMissingOrTooShort() {
        val emptyDates = seasonEntity(startDate = "", endDate = "")
        val shortDates = seasonEntity(startDate = "25", endDate = "26")

        assertEquals("", emptyDates.toCurrentSeason().startDateEndDate)
        assertEquals("", shortDates.toCurrentSeason().startDateEndDate)
    }

    // region Factories

    private fun seasonEntity(startDate: String, endDate: String): CurrentSeasonEntity {
        val entity = CurrentSeasonEntity()
        entity.startDate = startDate
        entity.endDate = endDate
        return entity
    }

    // endregion
}
