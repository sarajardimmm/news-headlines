package com.example.bbcheadlines.feature.headlines

sealed class HeadlinesEvent {
    object ShowRefreshError : HeadlinesEvent()
}
