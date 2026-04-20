package com.example.bbcheadlines

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.bbcheadlines.data.remote.api.NewsApi
import com.example.bbcheadlines.ui.theme.BBCHeadlinesTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val newsApi = retrofit.create(NewsApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BBCHeadlinesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = "Hello World!",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        lifecycleScope.launch {
            try {
                val response = newsApi.getTopHeadlines(
                    source = BuildConfig.NEWS_SOURCE,
                    apiKey = BuildConfig.NEWS_API_KEY
                )

                Log.d("API", "Articles: ${response.articles.size}")
            } catch (e: Exception) {
                Log.e("API", "Error", e)
            }
        }
    }
}