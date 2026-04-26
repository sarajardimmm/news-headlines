package com.example.bbcheadlines.core.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class DateUtilsTest {

    @Test
    fun `toReadableDate should format ISO date correctly for older dates`() {
        val now = Instant.parse("2024-04-22T21:23:26Z")
        val input = "2024-04-20T21:23:26Z"
        val expected = "20 Apr 2024, 21:23"

        val result = input.toReadableDate(now)

        assertEquals(expected, result)
    }

    @Test
    fun `toReadableDate should return Just now for very recent dates`() {
        val now = Instant.parse("2024-04-20T21:23:26Z")
        val input = "2024-04-20T21:23:00Z"
        val expected = "Just now"

        val result = input.toReadableDate(now)

        assertEquals(expected, result)
    }

    @Test
    fun `toReadableDate should return minutes ago for recent dates`() {
        val now = Instant.parse("2024-04-20T21:23:26Z")
        val input = "2024-04-20T21:13:26Z"
        val expected = "10m ago"

        val result = input.toReadableDate(now)

        assertEquals(expected, result)
    }

    @Test
    fun `toReadableDate should return hours ago for recent dates`() {
        val now = Instant.parse("2024-04-20T21:23:26Z")
        val input = "2024-04-20T19:23:26Z"
        val expected = "2h ago"

        val result = input.toReadableDate(now)

        assertEquals(expected, result)
    }

    @Test
    fun `toReadableDate should return empty string if parsing fails`() {
        val input = "invalid-date"
        assertEquals("", input.toReadableDate())
    }

    @Test
    fun `toReadableDate should handle empty string`() {
        val input = ""
        assertEquals("", input.toReadableDate())
    }
}
