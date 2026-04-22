package com.example.bbcheadlines.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bbcheadlines.R
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme

@Composable
fun HeadlineItem(
    article: Article,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(R.color.placeholder_grey)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineSmall
        )

        article.publishedAt?.let { publishedAt ->
            Text(
                text = publishedAt,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlineItemPreview() {
    NewsHeadlinesTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            HeadlineItem(
                article = Article(
                    title = "The latest news from the BBC",
                    description = "Follow the latest news from the BBC with our live updates.",
                    content = "Full content here...",
                    imageUrl = "https://example.com/image.jpg",
                    publishedAt = "20 Apr 2024",
                    publishedAtRaw = "2024-04-20T21:23:26Z",
                    url = "https://www.bbc.com"
                ),
                onClick = {}
            )
        }
    }
}
