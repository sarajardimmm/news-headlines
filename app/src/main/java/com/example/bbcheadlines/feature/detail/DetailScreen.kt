package com.example.bbcheadlines.feature.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bbcheadlines.R
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    article: Article,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.article_detail_title)) },
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
            useHorizontalLayout = false
        )
    }
}

@Composable
fun DetailContent(
    article: Article,
    modifier: Modifier = Modifier,
    useHorizontalLayout: Boolean = false
) {
    val context = LocalContext.current
    
    if (useHorizontalLayout) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailImage(
                imageUrl = article.imageUrl,
                title = article.title,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ArticleDetails(article, context)
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailImage(
                imageUrl = article.imageUrl,
                title = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            ArticleDetails(article, context)
        }
    }
}

@Composable
private fun DetailImage(
    imageUrl: String?,
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
private fun ArticleDetails(article: Article, context: Context) {
    Text(
        text = article.title,
        style = MaterialTheme.typography.headlineMedium
    )

    article.publishedAt?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }

    article.description?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    article.content?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    article.url?.let { url ->
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
                    // Handle cases where no browser is available or URL is malformed
                }
            }
        )
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
