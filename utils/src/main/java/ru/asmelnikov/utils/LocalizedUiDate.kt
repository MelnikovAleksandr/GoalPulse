package ru.asmelnikov.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun String.toLocalizedUiDate(
    locale: Locale = Locale.getDefault()
): String {
    val date = runCatching { LocalDate.parse(this,  DateTimeFormatter.ISO_LOCAL_DATE) }.getOrNull()
        ?: return this
    return date.format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale)
    )
}