package com.example.bbcheadlines.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    onClick: () -> Unit,
    useHorizontalLayout: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        if (useHorizontalLayout) {
            //Landscape/Tablet
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                HeadlineImage(
                    imageUrl = article.imageUrl,
                    title = article.title,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.5f) // Responsive height based on width
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 2.dp)
                ) {
                    HeadlineText(
                        title = article.title,
                        publishedAt = article.publishedAt
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = article.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3
                    )
                }

            }
        } else {
            // IMAGE ON TOP (Portrait Phone)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HeadlineImage(
                    imageUrl = article.imageUrl,
                    title = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )

                HeadlineText(
                    title = article.title,
                    publishedAt = article.publishedAt,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun HeadlineImage(
    imageUrl: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.placeholder_grey)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun HeadlineText(
    title: String,
    publishedAt: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2
        )

        publishedAt?.let { date ->
            Text(
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "Horizontal Layout - Landscape",
    device = "spec:parent=pixel_4,orientation=landscape",
    showSystemUi = true
)
@Composable
fun HeadlineItemHorizontalPreview() {
    NewsHeadlinesTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            HeadlineItem(
                article = Article(
                    title = "The latest news from the BBC and some extra text to see how it wraps",
                    description = "Follow the latest news from the BBC with our live updates and detailed coverage of world events.",
                    content = null,
                    imageUrl = null,
                    publishedAt = "20 Apr 2024",
                    publishedAtRaw = null,
                    url = null
                ),
                onClick = {},
                useHorizontalLayout = true
            )
        }
    }
}

@Preview(showBackground = true, name = "Vertical Layout")
@Composable
fun HeadlineItemVerticalPreview() {
    NewsHeadlinesTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            HeadlineItem(
                article = Article(
                    title = "The latest news from the BBC",
                    description = "Follow the latest news from the BBC.",
                    content = null,
                    imageUrl = null,
                    publishedAt = "20 Apr 2024",
                    publishedAtRaw = null,
                    url = null
                ),
                onClick = {},
                useHorizontalLayout = false
            )
        }
    }
}
