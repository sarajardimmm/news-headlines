package com.example.bbcheadlines.core.util

import org.junit.Assert.assertEquals
import org.junit.Test
class StringUtilsTest {

    @Test
    fun `should remove truncation marker with space`() {
        val input = "This is a news story... [+1234 chars]"
        val expected = "This is a news story..."
        assertEquals(expected, input.cleanTruncatedContent())
    }

    @Test
    fun `should remove truncation marker without space`() {
        val input = "This is a news story...[+1234 chars]"
        val expected = "This is a news story..."
        assertEquals(expected, input.cleanTruncatedContent())
    }

    @Test
    fun `should handle different numbers`() {
        val input = "This is a news story... [+42 chars]"
        val expected = "This is a news story..."
        assertEquals(expected, input.cleanTruncatedContent())
    }

    @Test
    fun `should keep string unchanged when no marker present`() {
        val input = "This is a full story."
        assertEquals(input, input.cleanTruncatedContent())
    }

    @Test
    fun `should handle empty string`() {
        val input = ""
        assertEquals("", input.cleanTruncatedContent())
    }
}