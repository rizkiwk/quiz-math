package com.avos.mathsharp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.avos.mathsharp.data.repository.SettingsRepository
import com.avos.mathsharp.data.repository.StatsRepository
import com.avos.mathsharp.di.AppContainer
import com.avos.mathsharp.domain.model.ThemeMode
import com.avos.mathsharp.domain.model.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val stats: StatsRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    val ui: StateFlow<UserSettings> =
        settings.settings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    fun setSfx(enabled: Boolean) = viewModelScope.launch { settings.setSfxEnabled(enabled) }
    fun setMusic(enabled: Boolean) = viewModelScope.launch { settings.setMusicEnabled(enabled) }
    fun setTheme(mode: ThemeMode) = viewModelScope.launch { settings.setThemeMode(mode) }
    fun resetProgress() = viewModelScope.launch { stats.resetProgress() }

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { SettingsViewModel(container.statsRepository, container.settingsRepository) }
        }
    }
}
