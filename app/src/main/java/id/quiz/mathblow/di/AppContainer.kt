package id.quiz.mathblow.di

import android.content.Context
import id.quiz.mathblow.audio.MusicManager
import id.quiz.mathblow.audio.SoundManager
import id.quiz.mathblow.data.local.AppDatabase
import id.quiz.mathblow.data.repository.SettingsRepository
import id.quiz.mathblow.data.repository.StatsRepository
import id.quiz.mathblow.domain.ProceduralQuestionGenerator
import id.quiz.mathblow.domain.QuestionGenerator

/** DI manual — satu graf dependensi, dibangun di Application.onCreate(). */
class AppContainer(context: Context) {
    private val database: AppDatabase = AppDatabase.build(context)

    val statsRepository: StatsRepository = StatsRepository(database.statsDao())
    val settingsRepository: SettingsRepository = SettingsRepository(context)
    val questionGenerator: QuestionGenerator = ProceduralQuestionGenerator()
    val soundManager: SoundManager = SoundManager(context)
    val musicManager: MusicManager = MusicManager(context)
}
