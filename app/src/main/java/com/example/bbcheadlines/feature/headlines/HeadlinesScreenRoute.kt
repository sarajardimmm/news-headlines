package com.example.bbcheadlines.feature.headlines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.bbcheadlines.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HeadlinesScreenRoute(
    uiStateFlow: StateFlow<HeadlinesUiState>,
    events: Flow<HeadlinesEvent>,
    onRetry: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    val uiState by uiStateFlow.collectAsState()

    HeadlinesScreen(
        uiState = uiState,
        events = events,
        onRetry = onRetry,
        onArticleClick = onArticleClick
    )
}