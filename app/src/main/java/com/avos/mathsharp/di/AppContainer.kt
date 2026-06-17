package com.avos.mathsharp.di

import android.content.Context
import com.avos.mathsharp.audio.MusicManager
import com.avos.mathsharp.audio.SoundManager
import com.avos.mathsharp.data.local.AppDatabase
import com.avos.mathsharp.data.repository.SettingsRepository
import com.avos.mathsharp.data.repository.StatsRepository
import com.avos.mathsharp.domain.ProceduralQuestionGenerator
import com.avos.mathsharp.domain.QuestionGenerator

/** DI manual — satu graf dependensi, dibangun di Application.onCreate(). */
class AppContainer(context: Context) {
    private val database: AppDatabase = AppDatabase.build(context)

    val statsRepository: StatsRepository = StatsRepository(database.statsDao())
    val settingsRepository: SettingsRepository = SettingsRepository(context)
    val questionGenerator: QuestionGenerator = ProceduralQuestionGenerator()
    val soundManager: SoundManager = SoundManager(context)
    val musicManager: MusicManager = MusicManager(context)
}
