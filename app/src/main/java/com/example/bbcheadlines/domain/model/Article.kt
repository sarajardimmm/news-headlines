package com.example.bbcheadlines.domain.model

data class Article(
    val title: String,
    val description: String?,
    val content: String?,
    val imageUrl: String?,
    val publishedAt: String?
)