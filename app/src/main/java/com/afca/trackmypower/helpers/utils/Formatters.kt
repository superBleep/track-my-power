package com.afca.trackmypower.helpers.utils

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object Formatters {
    @JvmStatic
    fun formatTime(time: LocalTime?): String {
        val hours = time?.hour
        val minutes = time?.minute

        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }

    @JvmStatic
    fun formatDuration(duration: Duration): String {
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        return String.format(Locale.getDefault(), "%1dh%2dm", hours, minutes)
    }

    @JvmStatic
    fun formatDate(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter
            .ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
        )
    }

    @JvmStatic
    fun calculateDuration(start: LocalTime, end: LocalTime): Duration {
        val startSeconds = start.toSecondOfDay().toLong()
        val endSeconds = end.toSecondOfDay().toLong()

        val durationSeconds = if (endSeconds >= startSeconds) {
            endSeconds - startSeconds
        } else {
            (24 * 60 * 60L - startSeconds) + endSeconds
        }

        return Duration.ofSeconds(durationSeconds)
    }
}