package com.example.bbcheadlines.core.util

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val JUST_NOW = "Just now"
private const val MINUTES_AGO = "m ago"
private const val HOURS_AGO = "h ago"
private const val DATE_PATTERN = "dd MMM yyyy, HH:mm"

fun String.toReadableDate(now: Instant = Instant.now()): String {
    return try {
        val past = Instant.parse(this)
        val duration = Duration.between(past, now)
        val seconds = duration.seconds

        when {
            seconds < 0 -> {
                // Future date, just format it
                formatDate(past)
            }
            seconds < 60 -> JUST_NOW
            seconds < 3600 -> "${seconds / 60}$MINUTES_AGO"
            seconds < 86400 -> "${seconds / 3600}$HOURS_AGO"
            else -> formatDate(past)
        }
    } catch (e: Exception) {
        ""
    }
}

private fun formatDate(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.ENGLISH)
        .withZone(ZoneId.of("UTC"))
    return formatter.format(instant)
}
