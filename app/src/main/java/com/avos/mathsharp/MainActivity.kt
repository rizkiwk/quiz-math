package com.avos.mathsharp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.avos.mathsharp.domain.model.ThemeMode
import com.avos.mathsharp.domain.model.UserSettings
import com.avos.mathsharp.ui.navigation.MathSharpNavHost
import com.avos.mathsharp.ui.theme.QuizMathTheme

class MainActivity : ComponentActivity() {

    private val musicManager get() = (application as MathSharpApp).container.musicManager

    override fun onStart() {
        super.onStart()
        musicManager.onForeground()
    }

    override fun onStop() {
        super.onStop()
        musicManager.onBackground()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as MathSharpApp).container

        setContent {
            val settings by container.settingsRepository.settings
                .collectAsStateWithLifecycle(initialValue = UserSettings())

            LaunchedEffect(settings.musicEnabled) {
                container.musicManager.setEnabled(settings.musicEnabled)
            }

            val dark = when (settings.themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            QuizMathTheme(darkTheme = dark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MathSharpNavHost()
                }
            }
        }
    }
}
