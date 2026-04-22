package com.example.bbcheadlines.feature.headlines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bbcheadlines.R
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.feature.detail.DetailContent
import com.example.bbcheadlines.ui.components.HeadlineItem
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesScreen(
    uiState: HeadlinesUiState,
    onRetry: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    Scaffold(
        topBar = {
            HeadlinesTopAppBar()
        }
    ) { innerPadding ->
        HeadlinesContent(
            uiState = uiState,
            onRetry = onRetry,
            onArticleClick = onArticleClick,
            useHorizontalLayout = false,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AdaptiveHeadlinesScreen(
    uiStateFlow: StateFlow<HeadlinesUiState>,
    onRetry: () -> Unit,
    selectedArticle: Article?,
    onArticleClick: (Article) -> Unit,
    onCloseDetail: () -> Unit,
    isMedium: Boolean = false
) {
    val uiState by uiStateFlow.collectAsState()

    Scaffold(
        topBar = {
            HeadlinesTopAppBar()
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // List Pane
            val listWeight = if (selectedArticle == null) 1f else (if (isMedium) 1.2f else 1f)
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
                val detailWeight = if (isMedium) 1.8f else 2f
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
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                    
                    DetailContent(
                        article = selectedArticle,
                        useHorizontalLayout = false
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            AsyncImage(
                model = R.drawable.logo,
                contentDescription = null,
                modifier = Modifier.height(32.dp)
            )
        }
    )
}

@Composable
fun HeadlinesContent(
    uiState: HeadlinesUiState,
    onRetry: () -> Unit,
    onArticleClick: (Article) -> Unit,
    useHorizontalLayout: Boolean,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }

        uiState.articles.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
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
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = uiState.articles,
                    key = { article -> article.title }
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
                        )
                    )
                ),
                onRetry = {},
                onArticleClick = {}
            )
        }
    }
}
