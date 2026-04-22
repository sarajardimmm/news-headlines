package com.example.bbcheadlines.feature.headlines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bbcheadlines.BuildConfig
import com.example.bbcheadlines.R
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.ui.components.HeadlineItem
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeadlinesScreen(
    uiState: HeadlinesUiState,
    onRetry: () -> Unit,
    onArticleClick: (Article) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = BuildConfig.NEWS_PROVIDER_NAME,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = onRetry) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            uiState.articles.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = uiState.articles,
                        key = { article -> article.title }
                    ) { article ->
                        HeadlineItem(
                            article = article,
                            onClick = { onArticleClick(article) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun HeadlinesScreenLoadingPreview() {
    NewsHeadlinesTheme {
        Surface {
            HeadlinesScreen(
                uiState = HeadlinesUiState(isLoading = true),
                onRetry = {},
                onArticleClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun HeadlinesScreenErrorPreview() {
    NewsHeadlinesTheme {
        Surface {
            HeadlinesScreen(
                uiState = HeadlinesUiState(errorMessage = "An unexpected error occurred."),
                onRetry = {},
                onArticleClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
fun HeadlinesScreenEmptyPreview() {
    NewsHeadlinesTheme {
        Surface {
            HeadlinesScreen(
                uiState = HeadlinesUiState(articles = emptyList()),
                onRetry = {},
                onArticleClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Success State")
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
                            title = "Another breaking news story",
                            description = "Important details about a global event.",
                            content = "More content here...",
                            imageUrl = null,
                            publishedAt = "22 Apr 2024",
                            publishedAtRaw = "2024-04-20T22:23:26Z",
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
