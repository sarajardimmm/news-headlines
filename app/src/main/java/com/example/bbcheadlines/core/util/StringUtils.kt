package com.example.bbcheadlines.core.util

fun String.cleanTruncatedContent(): String {
    val regex = Regex("\\s*\\[\\+\\d+ chars]$", RegexOption.IGNORE_CASE)
    return this.replace(regex, "").trim()
}