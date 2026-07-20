package id.quiz.mathblow.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import id.quiz.mathblow.domain.model.ThemeMode
import id.quiz.mathblow.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/** Setting + state ringan single-row di DataStore (offline, nol izin). */
class SettingsRepository(private val context: Context) {

    private object Keys {
        val SFX = booleanPreferencesKey("sfx_enabled")
        val MUSIC = booleanPreferencesKey("music_enabled")
        val THEME = intPreferencesKey("theme_mode")
        val ONBOARDED = booleanPreferencesKey("onboarded")
        val STREAK = intPreferencesKey("daily_streak")
        val LAST_PLAYED = longPreferencesKey("last_played_epoch_day")
        val PLAYER_NAME = stringPreferencesKey("player_name")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { p ->
        UserSettings(
            sfxEnabled = p[Keys.SFX] ?: true,
            musicEnabled = p[Keys.MUSIC] ?: true,
            themeMode = ThemeMode.fromValue(p[Keys.THEME] ?: 0),
            onboarded = p[Keys.ONBOARDED] ?: false,
            dailyStreak = p[Keys.STREAK] ?: 0,
            playerName = p[Keys.PLAYER_NAME] ?: ""
        )
    }

    suspend fun setSfxEnabled(enabled: Boolean) =
        edit { it[Keys.SFX] = enabled }

    suspend fun setMusicEnabled(enabled: Boolean) =
        edit { it[Keys.MUSIC] = enabled }

    suspend fun setThemeMode(mode: ThemeMode) =
        edit { it[Keys.THEME] = mode.value }

    suspend fun setOnboarded(value: Boolean) =
        edit { it[Keys.ONBOARDED] = value }

    suspend fun completeOnboarding(name: String, mode: ThemeMode) {
        context.dataStore.edit { p ->
            p[Keys.PLAYER_NAME] = name.trim()
            p[Keys.THEME] = mode.value
            p[Keys.ONBOARDED] = true
        }
    }

    /** Catat bahwa pemain main hari ini; kembalikan streak terbaru (freeze/forgive 1 hari). */
    suspend fun registerPlayedToday(): Int {
        val today = System.currentTimeMillis() / MILLIS_PER_DAY
        var newStreak = 1
        context.dataStore.edit { p ->
            val last = p[Keys.LAST_PLAYED] ?: 0L
            val streak = p[Keys.STREAK] ?: 0
            newStreak = when {
                last == today -> streak.coerceAtLeast(1)   // sudah main hari ini
                last == today - 1 -> streak + 1            // hari berurutan
                else -> 1                                  // reset
            }
            p[Keys.STREAK] = newStreak
            p[Keys.LAST_PLAYED] = today
        }
        return newStreak
    }

    private suspend fun edit(block: (androidx.datastore.preferences.core.MutablePreferences) -> Unit) {
        context.dataStore.edit(block)
    }

    private companion object {
        const val MILLIS_PER_DAY = 86_400_000L
    }
}
