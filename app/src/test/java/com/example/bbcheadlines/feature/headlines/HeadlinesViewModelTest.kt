package com.example.bbcheadlines.feature.headlines

import app.cash.turbine.test
import com.example.bbcheadlines.data.remote.repository.NewsRepository
import com.example.bbcheadlines.domain.model.Article
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HeadlinesViewModelTest {

    private val repository: NewsRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadHeadlines success should update uiState from loading to success`() = runTest {
        // Given
        val articles = listOf(
            Article("Title 1", "Desc 1", "Content 1", null, "url 1", "Date 1", "Raw Date 1")
        )
        coEvery { repository.getTopHeadlines() } returns articles

        // When
        val viewModel = HeadlinesViewModel(repository)

        // Then
        assertTrue(viewModel.uiState.value.isLoading)

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(articles, viewModel.uiState.value.articles)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loadHeadlines error should update uiState from loading to error`() = runTest {
        // Given
        coEvery { repository.getTopHeadlines() } throws IOException()

        // When
        val viewModel = HeadlinesViewModel(repository)

        // Then
        viewModel.uiState.test {
            assertTrue(awaitItem().isLoading)

            testDispatcher.scheduler.advanceUntilIdle()

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertTrue(errorState.articles.isEmpty())
            assertEquals("Network error. Please check your internet connection.", errorState.errorMessage)
        }
    }

    @Test
    fun `onArticleSelected should update selectedArticle`() = runTest {
        val viewModel = HeadlinesViewModel(repository)
        val article = Article("Title 1", "Desc 1", "Content 1", null, "url 1", "Date 1", "Raw Date 1")

        viewModel.onArticleSelected(article)

        assertEquals(article, viewModel.selectedArticle.value)
    }

    @Test
    fun `loadHeadlines retry after error should clear error and load articles`() = runTest {
        coEvery { repository.getTopHeadlines() } throws IOException() andThen listOf(
            Article("Title 1", "Desc 1", "Content 1", null, "url 1", "Date 1", "Raw Date 1")
        )

        val viewModel = HeadlinesViewModel(repository)

        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Network error. Please check your internet connection.", viewModel.uiState.value.errorMessage)

        viewModel.loadHeadlines()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(1, viewModel.uiState.value.articles.size)
        assertNull(viewModel.uiState.value.errorMessage)
    }
}
