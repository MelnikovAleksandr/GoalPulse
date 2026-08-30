package ru.asmelnikov.data.mappers

fun createYearRange(startDate: String, endDate: String): String {
    val startYear: String
    val endYear: String
    return try {
        startYear = startDate.substring(0, 4)
        endYear = endDate.substring(0, 4)
        "$startYear/$endYear"
    } catch (e: Exception) {
        ""
    }
}
