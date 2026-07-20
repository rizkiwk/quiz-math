package id.quiz.mathblow.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import id.quiz.mathblow.data.local.WorkoutSessionEntity
import id.quiz.mathblow.data.repository.StatsRepository
import id.quiz.mathblow.di.AppContainer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class StatsUiState(
    val sessions: List<WorkoutSessionEntity> = emptyList(),
    val totalCorrect: Int = 0,
    val bestScore: Int = 0,
    val totalSessions: Int = 0,
    val accuracy: Float = 0f
)

class StatsViewModel(stats: StatsRepository) : ViewModel() {

    val ui: StateFlow<StatsUiState> = combine(
        stats.recentSessions,
        stats.totalCorrect,
        stats.bestScore,
        stats.totalSessions,
        stats.avgAccuracy
    ) { sessions, correct, best, total, acc ->
        StatsUiState(sessions, correct, best, total, acc.toFloat())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StatsUiState())

    companion object {
        fun factory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer { StatsViewModel(container.statsRepository) }
        }
    }
}
