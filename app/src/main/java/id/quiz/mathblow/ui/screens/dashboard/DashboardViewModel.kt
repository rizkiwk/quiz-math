package id.quiz.mathblow.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import id.quiz.mathblow.data.repository.SettingsRepository
import id.quiz.mathblow.data.repository.StatsRepository
import id.quiz.mathblow.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val streak: Int = 0,
    val totalCorrect: Int = 0,
    val bestScore: Int = 0,
    val totalSessions: Int = 0,
    val accuracy: Float = 0f,
    val onboarded: Boolean? = null,   // null = belum termuat (cegah flash onboarding)
    val playerName: String = ""
)

class DashboardViewModel(
    stats: StatsRepository,
    settings: SettingsRepository
) : ViewModel() {

    val ui: StateFlow<DashboardUiState> = combine(
        settings.settings,
        stats.totalCorrect,
        stats.bestScore,
        stats.totalSessions,
        stats.avgAccuracy
    ) { s, correct, best, sessions, acc ->
        DashboardUiState(
            streak = s.dailyStreak,
            totalCorrect = correct,
            bestScore = best,
            totalSessions = sessions,
            accuracy = acc.toFloat(),
            onboarded = s.onboarded,
            playerName = s.playerName
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardUiState())

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { DashboardViewModel(container.statsRepository, container.settingsRepository) }
        }
    }
}
