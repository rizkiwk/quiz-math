package com.avos.mathsharp

import android.app.Application
import com.avos.mathsharp.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MathSharpApp : Application() {

    lateinit var container: AppContainer
        private set

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        // Seed katalog pencapaian sekali (idempoten).
        appScope.launch { container.statsRepository.ensureSeeded() }
    }
}
