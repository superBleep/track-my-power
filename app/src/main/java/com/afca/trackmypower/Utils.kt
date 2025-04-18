package com.afca.trackmypower

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object Utils {
    fun formatTime(time: LocalTime): String {
        val hours = time.hour
        val minutes = time.minute

        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }

    fun formatDuration(duration: Duration): String {
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        return String.format(Locale.getDefault(), "%1dh%2dm", hours, minutes)
    }

    fun formatDate(date: LocalDate): String {
        return date.format(DateTimeFormatter
            .ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
        )
    }
}