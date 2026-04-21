package com.example.bbcheadlines.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


fun String.toReadableDate(): String {
    return try {
        val instant = Instant.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.ENGLISH)
            .withZone(ZoneId.of("UTC")) // Fixed zone for consistent testing/display

        formatter.format(instant)
    } catch (e: Exception) {
        this // fallback to raw if parsing fails
    }
}
