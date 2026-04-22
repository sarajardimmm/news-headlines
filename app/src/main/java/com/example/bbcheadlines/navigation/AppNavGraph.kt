package com.example.bbcheadlines.navigation

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.feature.detail.DetailScreen
import com.example.bbcheadlines.feature.headlines.HeadlinesScreenRoute
import com.example.bbcheadlines.feature.headlines.HeadlinesViewModel
import com.example.bbcheadlines.feature.headlines.AdaptiveHeadlinesScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val widthSizeClass = windowSizeClass.widthSizeClass
    val heightSizeClass = windowSizeClass.heightSizeClass

    // Strict Tablet Detection Logic:
    // A device is only considered a Tablet/Two-Pane-capable if NEITHER dimension is Compact.
    // - Phone Portrait: Width is Compact -> Single Pane
    // - Phone Landscape: Height is Compact -> Single Pane
    // - Tablet: Both Width and Height are Medium or Expanded -> Two Pane
    val showTwoPane = widthSizeClass != WindowWidthSizeClass.Compact && 
                      heightSizeClass != WindowHeightSizeClass.Compact

    NavHost(
        navController = navController,
        startDestination = Destinations.HEADLINES,
        modifier = modifier
    ) {
        composable(Destinations.HEADLINES) {
            val viewModel: HeadlinesViewModel = hiltViewModel()
            val uiState = viewModel.uiState
            
            // Detail Screen State: survives rotation
            var selectedArticle by rememberSaveable { mutableStateOf<Article?>(null) }

            if (showTwoPane) {
                AdaptiveHeadlinesScreen(
                    uiStateFlow = uiState,
                    onRetry = viewModel::loadHeadlines,
                    selectedArticle = selectedArticle,
                    onArticleClick = { article -> selectedArticle = article },
                    onCloseDetail = { selectedArticle = null },
                    isMedium = widthSizeClass == WindowWidthSizeClass.Medium
                )
            } else {
                HeadlinesScreenRoute(
                    uiStateFlow = uiState,
                    onRetry = viewModel::loadHeadlines,
                    onArticleClick = { article ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("article", article)

                        navController.navigate(Destinations.DETAIL)
                    }
                )
            }
        }

        composable(Destinations.DETAIL) {
            val article = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Article>("article")

            article?.let {
                DetailScreen(
                    article = it,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
