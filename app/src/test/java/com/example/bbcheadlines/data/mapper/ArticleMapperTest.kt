package com.example.bbcheadlines.data.mapper

import com.example.bbcheadlines.data.remote.dto.ArticleDto
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleMapperTest {

    @Test
    fun `toDomain should correctly map ArticleDto to Article`() {
        // Given
        val dto = ArticleDto(
            title = "Title",
            description = "Description",
            content = "Content... [+123 chars]",
            urlToImage = "url",
            publishedAt = "2024-04-20T21:23:26Z"
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals("Title", domain.title)
        assertEquals("Description", domain.description)
        assertEquals("Content", domain.content)
        assertEquals("url", domain.imageUrl)
        assertEquals("2024-04-20T21:23:26Z", domain.publishedAtRaw)
        // publishedAt is formatted by toReadableDate, which we test separately in DateUtilsTest
    }

    @Test
    fun `toDomain should handle null values`() {
        // Given
        val dto = ArticleDto(
            title = null,
            description = null,
            content = null,
            urlToImage = null,
            publishedAt = null
        )

        // When
        val domain = dto.toDomain()

        // Then
        assertEquals("", domain.title)
        assertEquals(null, domain.description)
        assertEquals("", domain.content)
        assertEquals(null, domain.imageUrl)
        assertEquals(null, domain.publishedAtRaw)
        assertEquals(null, domain.publishedAt)
    }
}
