package com.example.bbcheadlines.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun String.toReadableDate(): String {
    return try {
        val instant = Instant.parse(this)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm") //this matches the editorial style of BBC, NYT, etc.
            .withZone(ZoneId.systemDefault())

        formatter.format(instant)
    } catch (e: Exception) {
        this // fallback to raw if parsing fails
    }
}