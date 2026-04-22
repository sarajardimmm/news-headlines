package com.example.bbcheadlines.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val useNavRailOrSideBar = widthSizeClass != WindowWidthSizeClass.Compact

    NavHost(
        navController = navController,
        startDestination = Destinations.HEADLINES,
        modifier = modifier
    ) {
        composable(Destinations.HEADLINES) {
            val viewModel: HeadlinesViewModel = hiltViewModel()
            val uiState = viewModel.uiState
            
            var selectedArticle by remember { mutableStateOf<Article?>(null) }

            if (useNavRailOrSideBar) {
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
