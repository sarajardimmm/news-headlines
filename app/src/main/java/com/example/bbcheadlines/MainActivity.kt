package com.example.bbcheadlines

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.bbcheadlines.navigation.AppNavGraph
import com.example.bbcheadlines.ui.theme.NewsHeadlinesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            var isAuthenticated by rememberSaveable { mutableStateOf(false) }
            var isLocked by rememberSaveable { mutableStateOf(false) }

            // Trigger biometric check only once or when locked
            SideEffect {
                if (!isAuthenticated && !isLocked) {
                    checkBiometricAvailability(
                        onAuthenticated = { isAuthenticated = true },
                        onLocked = { isLocked = true }
                    )
                }
            }

            NewsHeadlinesTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isAuthenticated) {
                        val windowSizeClass = calculateWindowSizeClass(this)
                        SetStatusBarColor()
                        val navController = rememberNavController()

                        AppNavGraph(
                            navController = navController,
                            windowSizeClass = windowSizeClass,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (isLocked) {
                        LockScreen(onRetry = {
                            showBiometricPrompt(
                                onAuthenticated = {
                                    isAuthenticated = true
                                    isLocked = false
                                },
                                onLocked = { isLocked = true }
                            )
                        })
                    }
                }
            }
        }
    }

    private fun checkBiometricAvailability(
        onAuthenticated: () -> Unit,
        onLocked: () -> Unit
    ) {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                showBiometricPrompt(onAuthenticated, onLocked)
            }

            else -> {
                // If the device doesn’t have fingerprint scanner or it’s not configured, then it should open normally
                onAuthenticated()
            }
        }
    }

    private fun showBiometricPrompt(
        onAuthenticated: () -> Unit,
        onLocked: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(
            this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onLocked()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onAuthenticated()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Authentication failed (e.g. wrong finger), the system UI stays open
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_negative_button))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    @Composable
    private fun LockScreen(onRetry: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.app_locked),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.authenticate_to_unlock),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }

    @Composable
    private fun SetStatusBarColor() {
        val color = colorResource(R.color.brand_primary)
        val useLightIcons = color.luminance() > 0.5f

        SideEffect {
            val window = this@MainActivity.window
            WindowCompat.getInsetsController(window, window.decorView)
                .isAppearanceLightStatusBars = useLightIcons
        }
    }
}
