package com.example.bbcheadlines.di

import com.example.bbcheadlines.BuildConfig
import com.example.bbcheadlines.data.remote.repository.DemoNewsRepositoryImpl
import com.example.bbcheadlines.data.remote.repository.NewsRepository
import com.example.bbcheadlines.data.remote.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl,
        demoNewsRepositoryImpl: DemoNewsRepositoryImpl
    ): NewsRepository {
        return if (BuildConfig.NEWS_API_KEY.isBlank() || BuildConfig.NEWS_API_KEY == "null") {
            demoNewsRepositoryImpl
        } else {
            newsRepositoryImpl
        }
    }
}
