package com.example.bbcheadlines.data.remote.repository

import com.example.bbcheadlines.domain.model.Article

interface NewsRepository {
    suspend fun getTopHeadlines(): List<Article>
}