package com.example.bbcheadlines.data.remote.dto

data class ArticleDto(
    val title: String?,
    val description: String?,
    val content: String?,
    val urlToImage: String?,
    val publishedAt: String?
)