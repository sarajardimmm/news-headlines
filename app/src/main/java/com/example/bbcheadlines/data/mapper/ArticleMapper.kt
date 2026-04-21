package com.example.bbcheadlines.data.mapper

import com.example.bbcheadlines.core.util.cleanTruncatedContent
import com.example.bbcheadlines.core.util.toReadableDate
import com.example.bbcheadlines.data.remote.dto.ArticleDto
import com.example.bbcheadlines.domain.model.Article

fun ArticleDto.toDomain(): Article {
    return Article(
        title = title.orEmpty(),
        description = description,
        content = content.orEmpty().cleanTruncatedContent(),
        imageUrl = urlToImage,
        url = url,
        publishedAt = publishedAt?.toReadableDate(),
        publishedAtRaw = publishedAt
    )
}
