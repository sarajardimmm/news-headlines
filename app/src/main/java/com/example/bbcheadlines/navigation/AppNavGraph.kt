package com.example.bbcheadlines.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bbcheadlines.domain.model.Article
import com.example.bbcheadlines.feature.detail.DetailScreen
import com.example.bbcheadlines.feature.headlines.HeadlinesScreenRoute
import com.example.bbcheadlines.feature.headlines.HeadlinesViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.HEADLINES,
        modifier = modifier
    ) {
        composable(Destinations.HEADLINES) {
            val viewModel: HeadlinesViewModel = hiltViewModel()
            val uiState = viewModel.uiState

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
