package com.example.bbcheadlines.feature.headlines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.bbcheadlines.domain.model.Article

@Composable
fun HeadlinesRoute(
    showTwoPane: Boolean,
    isMedium: Boolean,
    onOpenArticle: (Article) -> Unit,
    viewModel: HeadlinesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedArticle by viewModel.selectedArticle.collectAsState()

    if (showTwoPane) {
        AdaptiveHeadlinesScreen(
            uiState = uiState,
            events = viewModel.events,
            onRetry = viewModel::loadHeadlines,
            selectedArticle = selectedArticle,
            onArticleClick = viewModel::onArticleSelected,
            onCloseDetail = viewModel::clearSelectedArticle,
            isMediumWidth = isMedium
        )
    } else {
        HeadlinesScreen(
            uiState = uiState,
            events = viewModel.events,
            onRetry = viewModel::loadHeadlines,
            onArticleClick = onOpenArticle
        )
    }
}