package id.quiz.mathblow.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import id.quiz.mathblow.MathBlowApp
import id.quiz.mathblow.di.AppContainer

/** Ambil graf DI dari Application di dalam composable. */
@Composable
fun rememberAppContainer(): AppContainer {
    val context = LocalContext.current
    return (context.applicationContext as MathBlowApp).container
}
