package com.example.bbcheadlines.feature.headlines

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HeadlinesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun headlinesList_showsArticles() {
        val articles = listOf(
            createFakeArticle("BBC News Title")
        )
        val uiState = HeadlinesUiState(articles = articles)

        composeTestRule.setContent {
            NewsHeadlinesTheme {
                HeadlinesScreen(
                    uiState = uiState,
                    events = emptyFlow(),
                    onRetry = {},
                    onArticleClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("BBC News Title").assertIsDisplayed()
    }

    @Test
    fun headlinesList_clickingArticle_triggersCallback() {
        var clickedArticle: Article? = null
        val article = createFakeArticle("Clickable News")
        val uiState = HeadlinesUiState(articles = listOf(article))

        composeTestRule.setContent {
            NewsHeadlinesTheme {
                HeadlinesScreen(
                    uiState = uiState,
                    events = emptyFlow(),
                    onRetry = {},
                    onArticleClick = { clickedArticle = it }
                )
            }
        }

        composeTestRule.onNodeWithText("Clickable News").performClick()

        assertEquals(article, clickedArticle)
    }

    @Test
    fun errorState_showsRetryButton() {
        val errorMessage = "Failed to connect"
        val uiState = HeadlinesUiState(errorMessage = errorMessage)

        composeTestRule.setContent {
            NewsHeadlinesTheme {
                HeadlinesScreen(
                    uiState = uiState,
                    events = emptyFlow(),
                    onRetry = {},
                    onArticleClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun loadingState_showsCircularProgress() {
        // Given a loading state
        val uiState = HeadlinesUiState(isLoading = true)

        composeTestRule.setContent {
            NewsHeadlinesTheme {
                HeadlinesScreen(
                    uiState = uiState,
                    events = emptyFlow(),
                    onRetry = {},
                    onArticleClick = {}
                )
            }
        }

        // Then loading indicator should be present
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    private fun createFakeArticle(title: String) = Article(
        title = title,
        description = "Description",
        content = "Content",
        imageUrl = null,
        publishedAt = "20 Apr 2024",
        publishedAtRaw = null,
        url = "https://bbc.com/${title.hashCode()}"
    )
}
