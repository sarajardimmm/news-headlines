package com.example.bbcheadlines.feature.detail

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bbcheadlines.BuildConfig
import com.example.bbcheadlines.R
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: Article,
    onBackClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = BuildConfig.NEWS_PROVIDER_NAME,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        DetailContent(
            article = article,
            modifier = Modifier.padding(innerPadding),
            useHorizontalLayout = false,
            extraPadding = isLandscape
        )
    }
}

@Composable
fun DetailContent(
    article: Article,
    modifier: Modifier = Modifier,
    useHorizontalLayout: Boolean = false,
    extraPadding: Boolean = false
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(article) {
        scrollState.scrollTo(0)
    }
    
    if (useHorizontalLayout) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                article.imageUrl?.let {
                    DetailImage(
                        imageUrl = it,
                        title = article.title,
                        modifier = Modifier.aspectRatio(1f)
                    )
                }
                PublishedAtText(article.publishedAt)
            }

            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ArticleHeader(article.title)
                ArticleContent(article, context)
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            ArticleHeader(article.title)
            
            article.imageUrl?.let {
                Spacer(modifier = Modifier.height(20.dp))
                DetailImage(
                    imageUrl = it,
                    title = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (extraPadding) 64.dp else 0.dp)
                        .aspectRatio(16f / 9f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            PublishedAtText(
                date = article.publishedAt,
                modifier = Modifier.padding(horizontal = if (extraPadding) 64.dp else 0.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            ArticleContent(article, context)
        }
    }
}

@Composable
private fun DetailImage(
    imageUrl: String,
    title: String,
    modifier: Modifier = Modifier
) {
    val placeholderColor = colorResource(R.color.placeholder_grey)
    
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = title,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(placeholderColor),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PublishedAtText(date: String?, modifier: Modifier = Modifier) {
    date?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = modifier
        )
    }
}

@Composable
private fun ArticleHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium
    )
}

@Composable
private fun ArticleContent(article: Article, context: Context) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        article.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        article.content?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        article.url?.takeIf { it.isNotEmpty() }?.let { url ->
            Text(
                text = stringResource(R.string.read_full_article),
                style = MaterialTheme.typography.labelLarge.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    NewsHeadlinesTheme {
        Surface {
            DetailScreen(
                article = Article(
                    title = "The latest news from the BBC",
                    description = "Follow the latest news from the BBC with our live updates.",
                    content = "Full content here...",
                    imageUrl = null,
                    url = "https://www.bbc.com",
                    publishedAt = "20 Apr 2024",
                    publishedAtRaw = "2024-04-20T21:23:26Z"
                ),
                onBackClick = {}
            )
        }
    }
}
