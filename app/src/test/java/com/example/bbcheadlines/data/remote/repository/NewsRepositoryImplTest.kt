package com.example.bbcheadlines.data.remote.repository

import com.example.bbcheadlines.data.remote.api.NewsApi
import com.example.bbcheadlines.data.remote.dto.ArticleDto
import com.example.bbcheadlines.data.remote.dto.TopHeadlinesResponseDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NewsRepositoryImplTest {

    private val newsApi: NewsApi = mockk()
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setup() {
        repository = NewsRepositoryImpl(newsApi)
    }

    @Test
    fun `getTopHeadlines should return articles sorted by date descending`() = runTest {
        // Given
        val articles = listOf(
            ArticleDto("Title 1", "Desc 1", "Content 1", null, "2024-04-20T10:00:00Z"),
            ArticleDto("Title 2", "Desc 2", "Content 2", null, "2024-04-20T12:00:00Z"),
            ArticleDto("Title 3", "Desc 3", "Content 3", null, "2024-04-20T08:00:00Z")
        )
        val response = TopHeadlinesResponseDto(articles = articles, status = "ok", totalResults = 3)
        
        coEvery { newsApi.getTopHeadlines(any(), any()) } returns response

        // When
        val result = repository.getTopHeadlines()

        // Then
        assertEquals(3, result.size)
        assertEquals("2024-04-20T12:00:00Z", result[0].publishedAtRaw)
        assertEquals("2024-04-20T10:00:00Z", result[1].publishedAtRaw)
        assertEquals("2024-04-20T08:00:00Z", result[2].publishedAtRaw)
    }
}
