package com.example.bbcheadlines.feature.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailScreen_showsArticleContent() {
        // Given an article
        val article = Article(
            title = "Article Detail Title",
            description = "This is a detailed description",
            content = "Full content of the news article is here.",
            imageUrl = null,
            publishedAt = "21 Apr 2024",
            publishedAtRaw = null,
            url = "https://bbc.com/detail"
        )

        // When the detail screen is loaded
        composeTestRule.setContent {
            NewsHeadlinesTheme {
                DetailScreen(
                    article = article,
                    onBackClick = {}
                )
            }
        }

        // Then all key information should be displayed
        composeTestRule.onNodeWithText("Article Detail Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a detailed description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Full content of the news article is here.").assertIsDisplayed()
        composeTestRule.onNodeWithText("21 Apr 2024").assertIsDisplayed()
    }

    @Test
    fun detailScreen_backButtonClick_triggersCallback() {
        var backClicked = false

        val article = Article(
            title = "Title",
            description = "Desc",
            content = "Content",
            imageUrl = null,
            publishedAt = "21 Apr 2024",
            publishedAtRaw = null,
            url = "https://bbc.com/detail"
        )

        composeTestRule.setContent {
            NewsHeadlinesTheme {
                DetailScreen(
                    article = article,
                    onBackClick = { backClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithTag("back_button").performClick()

        assertTrue(backClicked)
    }
}
