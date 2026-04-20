package com.example.bbcheadlines.feature.headlines

import com.example.bbcheadlines.domain.model.Article

data class HeadlinesUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val errorMessage: String? = null
)