package com.example.bbcheadlines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.bbcheadlines.navigation.AppNavGraph
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            SetStatusBarColor()
            NewsHeadlinesTheme(dynamicColor = false) {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    windowSizeClass = windowSizeClass,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @Composable
    private fun SetStatusBarColor() {
        val color = colorResource(R.color.brand_primary)

        val useLightIcons = color.luminance() > 0.5f

        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = useLightIcons

    }
}
