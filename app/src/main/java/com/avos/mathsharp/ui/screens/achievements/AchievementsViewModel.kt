package com.avos.mathsharp.ui.screens.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.avos.mathsharp.data.local.AchievementEntity
import com.avos.mathsharp.data.repository.StatsRepository
import com.avos.mathsharp.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AchievementsViewModel(stats: StatsRepository) : ViewModel() {

    val achievements: StateFlow<List<AchievementEntity>> =
        stats.achievements.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { AchievementsViewModel(container.statsRepository) }
        }
    }
}
