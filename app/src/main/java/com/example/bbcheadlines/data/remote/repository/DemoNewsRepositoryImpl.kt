package com.example.bbcheadlines.data.remote.repository

import com.example.bbcheadlines.domain.model.Article
import kotlinx.coroutines.delay
import javax.inject.Inject

class DemoNewsRepositoryImpl @Inject constructor() : NewsRepository {
    override suspend fun getTopHeadlines(): List<Article> {
        delay(1000) // Simulate network delay
        return listOf(
            Article(
                title = "Demo Mode: No API Key Found",
                description = "The app is currently running with mock data because no NewsAPI key was provided in local.properties.",
                content = "To see live headlines, add your API key to local.properties as NEWS_API_KEY=your_key_here and rebuild the project.",
                imageUrl = "https://images.unsplash.com/photo-1504711434969-e33886168f5c?q=80&w=1000&auto=format&fit=crop",
                url = "https://newsapi.org/",
                publishedAt = "Just now",
                publishedAtRaw = "2024-04-20T21:23:26Z"
            ),
            Article(
                title = "How to fix this: Adding your own API Key",
                description = "Step-by-step guide to getting this app running with real data from the BBC News API.",
                content = "1. Go to newsapi.org and register for a free key. 2. Open local.properties in the project root. 3. Add NEWS_API_KEY=... 4. Sync Gradle and Run.",
                imageUrl = "https://images.unsplash.com/photo-1495020689067-958852a7765e?q=80&w=1000&auto=format&fit=crop",
                url = "https://newsapi.org/docs/endpoints/top-headlines",
                publishedAt = "1 hour ago",
                publishedAtRaw = "2024-04-20T20:23:26Z"
            ),
            Article(
                title = "Clean Architecture & Adaptive UI",
                description = "This challenge demonstrates modern Android practices including Hilt, Compose, and WindowSizeClass.",
                content = "Even in demo mode, you can test the adaptive layout by rotating the device or running on a tablet/foldable emulator.",
                imageUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?q=80&w=1000&auto=format&fit=crop",
                url = "https://developer.android.com/jetpack/compose",
                publishedAt = "2 hours ago",
                publishedAtRaw = "2024-04-20T19:23:26Z"
            )
        )
    }
}
