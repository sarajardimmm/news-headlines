package com.example.bbcheadlines.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.bbcheadlines.R


@Composable
fun NewsHeadlinesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val brandPrimary = colorResource(R.color.brand_primary)

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = brandPrimary,
            secondary = AppPrimaryDark,
            onPrimary = Color.White,
            background = AppBackgroundDark,
            surface = AppSurfaceDark,
            onSurface = AppOnSurfaceDark,
            surfaceVariant = AppSurfaceVariantDark,
            onSurfaceVariant = AppOnSurfaceVariantDark
        )
        else -> lightColorScheme(
            primary = brandPrimary,
            secondary = brandPrimary,
            onPrimary = Color.White,
            background = AppBackgroundLight,
            surface = AppSurfaceLight,
            onSurface = AppOnSurfaceLight,
            surfaceVariant = AppSurfaceVariantLight,
            onSurfaceVariant = AppOnSurfaceVariantLight
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
