package com.example.bbcheadlines.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DateUtilsTest {

    @Test
    fun `toReadableDate should format ISO date correctly`() {
        val input = "2024-04-20T21:23:26Z"
        val expected = "20 Apr 2024, 21:23"

        val result = input.toReadableDate()

        assertEquals(expected, result)
    }

    @Test
    fun `toReadableDate should return raw string if parsing fails`() {
        val input = "invalid-date"
        assertEquals(input, input.toReadableDate())
    }

    @Test
    fun `toReadableDate should handle null-like empty string`() {
        val input = ""
        assertEquals("", input.toReadableDate())
    }
}
