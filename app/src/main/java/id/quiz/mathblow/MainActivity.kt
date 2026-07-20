package id.quiz.mathblow

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.quiz.mathblow.domain.model.ThemeMode
import id.quiz.mathblow.domain.model.UserSettings
import id.quiz.mathblow.ui.navigation.MathBlowNavHost
import id.quiz.mathblow.ui.theme.MathBlowTheme

class MainActivity : ComponentActivity() {

    private val musicManager get() = (application as MathBlowApp).container.musicManager

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

        // Edge-to-edge SETUP MANUAL — sengaja TIDAK pakai androidx.activity.enableEdgeToEdge(),
        // karena EdgeToEdgeApi28-nya menulis cutout SHORT_EDGES (deprecated di Android 15 →
        // memicu warning Play "deprecated edge-to-edge API", muncul ter-obfuscate sebagai b.o.k).
        // Cutout pakai ALWAYS (masih legal di API 35). Inset konten & kontras ikon di-handle
        // di dalam setContent (safeDrawingPadding + SideEffect).
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            }
        }

        val container = (application as MathBlowApp).container

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

            // Kontras ikon system bar mengikuti tema (pengganti SystemBarStyle.auto dari
            // enableEdgeToEdge yang sengaja tidak dipakai): gelap → ikon terang, terang → ikon gelap.
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val controller = WindowCompat.getInsetsController(window, view)
                    controller.isAppearanceLightStatusBars = !dark
                    controller.isAppearanceLightNavigationBars = !dark
                }
            }

            MathBlowTheme(darkTheme = dark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // safeDrawingPadding di satu titik: menjamin SEMUA layar (yang sebelumnya
                    // nol penanganan inset) tidak tertimpa status bar / nav bar / display cutout.
                    Box(modifier = Modifier.safeDrawingPadding()) {
                        MathBlowNavHost()
                    }
                }
            }
        }
    }
}
