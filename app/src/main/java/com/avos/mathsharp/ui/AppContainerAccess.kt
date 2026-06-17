package com.avos.mathsharp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.avos.mathsharp.MathSharpApp
import com.avos.mathsharp.di.AppContainer

/** Ambil graf DI dari Application di dalam composable. */
@Composable
fun rememberAppContainer(): AppContainer {
    val context = LocalContext.current
    return (context.applicationContext as MathSharpApp).container
}
