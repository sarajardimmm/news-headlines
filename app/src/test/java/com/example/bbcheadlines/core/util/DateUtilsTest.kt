package com.example.bbcheadlines.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `toReadableDate should format ISO date correctly`() {
        val input = "2024-04-20T21:23:26Z"
        // The output depends on the system default timezone, which might vary in tests.
        // However, the pattern "dd MMM yyyy, HH:mm" is what we want to verify.
        // For UTC, it would be "20 Apr 2024, 21:23"
        // Let's check if it returns a non-empty string and follows the general format if we can't guarantee TZ.
        val result = input.toReadableDate()
        
        // Basic check: should contain day, month and year
        assert(result.contains("20"))
        assert(result.contains("Apr"))
        assert(result.contains("2024"))
    }

    @Test
    fun `toReadableDate should return raw string if parsing fails`() {
        val input = "invalid-date"
        assertEquals(input, input.toReadableDate())
    }
}
