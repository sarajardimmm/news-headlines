package com.example.bbcheadlines.feature.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bbcheadlines.data.remote.repository.NewsRepository
import com.example.bbcheadlines.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HeadlinesUiState(isLoading = true))
    val uiState: StateFlow<HeadlinesUiState> = _uiState.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    private val _events = Channel<HeadlinesEvent>()
    val events = _events.receiveAsFlow()

    private var loadJob: Job? = null

    fun onArticleSelected(article: Article) {
        _selectedArticle.value = article
    }

    fun clearSelectedArticle() {
        _selectedArticle.value = null
    }

    init {
        loadHeadlines()
    }

    fun loadHeadlines() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            delay(250)
            try {
                val articles = repository.getTopHeadlines()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        articles = articles
                    )
                }
            } catch (e: Exception) {
                if (uiState.value.articles.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load headlines.\n\n${e.message}"
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(HeadlinesEvent.ShowRefreshError)
                }
            }
        }
    }
}
