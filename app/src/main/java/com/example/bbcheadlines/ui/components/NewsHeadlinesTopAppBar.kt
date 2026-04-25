package com.example.bbcheadlines.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.bbcheadlines.BuildConfig
import com.example.bbcheadlines.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsHeadlinesTopAppBar(navigationIcon: @Composable () -> Unit = {}) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = BuildConfig.NEWS_PROVIDER_NAME,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.brand_primary),
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = navigationIcon
    )
}