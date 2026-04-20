package com.example.bbcheadlines.data.remote.repository

import com.example.bbcheadlines.BuildConfig
import com.example.bbcheadlines.data.mapper.toDomain
import com.example.bbcheadlines.data.remote.api.NewsApi
import javax.inject.Inject
import com.example.bbcheadlines.domain.model.Article

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {

    override suspend fun getTopHeadlines(): List<Article> {
        return newsApi.getTopHeadlines(
            source = BuildConfig.NEWS_SOURCE,
            apiKey = BuildConfig.NEWS_API_KEY
        ).articles.map { it.toDomain() }
    }
}