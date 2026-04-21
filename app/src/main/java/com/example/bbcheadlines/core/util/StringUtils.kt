package com.example.bbcheadlines.core.util

fun String.cleanTruncatedContent(): String {
    // Regex to match "[+" and everything after.
    val regex = Regex("\\s*\\[\\+.*$", RegexOption.IGNORE_CASE)
    return this.replace(regex, "").trim()
}