package com.example.bbcheadlines.feature.headlines

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bbcheadlines.BuildConfig
import com.example.bbcheadlines.R
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.feature.detail.DetailContent
import com.example.bbcheadlines.ui.components.HeadlineItem
import com.example.bbcheadlines.ui.components.NewsHeadlinesTopAppBar
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesScreen(
    uiState: HeadlinesUiState,
    events: Flow<HeadlinesEvent>,
    onRetry: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val snackbarHostState = rememberHeadlinesSnackbarHostState(events)


    Scaffold(
        topBar = {
            NewsHeadlinesTopAppBar()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        HeadlinesContent(
            uiState = uiState,
            onRetry = onRetry,
            onArticleClick = onArticleClick,
            useHorizontalLayout = isLandscape,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveHeadlinesScreen(
    uiState: HeadlinesUiState,
    events: Flow<HeadlinesEvent>,
    onRetry: () -> Unit,
    selectedArticle: Article?,
    onArticleClick: (Article) -> Unit,
    onCloseDetail: () -> Unit,
    isMediumWidth: Boolean = false
) {
    val snackbarHostState = rememberHeadlinesSnackbarHostState(events)

    Scaffold(
        topBar = {
            NewsHeadlinesTopAppBar()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // List Pane
            val listWeight = if (selectedArticle == null) 1f else (if (isMediumWidth) 1.2f else 1f)
            val useHorizontalInList = selectedArticle == null

            Box(modifier = Modifier.weight(listWeight)) {
                HeadlinesContent(
                    uiState = uiState,
                    onRetry = onRetry,
                    onArticleClick = onArticleClick,
                    useHorizontalLayout = useHorizontalInList
                )
            }

            // Detail Pane
            if (selectedArticle != null) {
                val detailWeight = if (isMediumWidth) 1.8f else 2f
                Column(
                    modifier = Modifier
                        .weight(detailWeight)
                        .fillMaxHeight()
                ) {
                    IconButton(
                        onClick = onCloseDetail,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close article detail"
                        )
                    }
                    DetailContent(
                        article = selectedArticle,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}
@Composable
private fun rememberHeadlinesSnackbarHostState(
    events: Flow<HeadlinesEvent>
): SnackbarHostState {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                HeadlinesEvent.ShowRefreshError -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.unable_to_refresh)
                    )
                }
            }
        }
    }

    return snackbarHostState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesContent(
    uiState: HeadlinesUiState,
    onRetry: () -> Unit,
    onArticleClick: (Article) -> Unit,
    useHorizontalLayout: Boolean,
    modifier: Modifier = Modifier
) {
    val state = rememberPullToRefreshState()
    val isRefreshing = uiState.isLoading && uiState.articles.isNotEmpty()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRetry,
        state = state,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = state,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                color = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading && uiState.articles.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null && uiState.articles.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(onClick = onRetry) {
                        Text(
                            text = stringResource(R.string.retry),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            uiState.articles.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_headlines_available),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.articles,
                        key = { it.url ?: "${it.title}-${it.publishedAtRaw}" }
                    ) { article ->
                        HeadlineItem(
                            article = article,
                            onClick = { onArticleClick(article) },
                            useHorizontalLayout = useHorizontalLayout
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Success State - Single Pane")
@Composable
fun HeadlinesScreenSuccessPreview() {
    NewsHeadlinesTheme {
        Surface {
            HeadlinesScreen(
                uiState = HeadlinesUiState(
                    articles = listOf(
                        Article(
                            title = "The latest news from the BBC",
                            description = "Follow the latest news from the BBC with our live updates.",
                            content = "Full content here...",
                            imageUrl = null,
                            publishedAt = "21 Apr 2024",
                            publishedAtRaw = "2024-04-20T21:23:26Z",
                            url = "https://www.bbc.com"
                        ),
                        Article(
                            title = "Another latest news from the BBC",
                            description = "Follow the latest news from the BBC with our live updates.",
                            content = "Full content here...",
                            imageUrl = null,
                            publishedAt = "21 Apr 2024",
                            publishedAtRaw = "2024-04-20T21:23:26Z",
                            url = "https://www.bbc-mews.com"
                        )
                    )
                ),
                events = emptyFlow(),
                onRetry = {},
                onArticleClick = {}
            )
        }
    }
}
