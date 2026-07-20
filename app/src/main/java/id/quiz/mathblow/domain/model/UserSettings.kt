package id.quiz.mathblow.domain.model

enum class ThemeMode(val value: Int) {
    SYSTEM(0), DARK(1), LIGHT(2);

    companion object {
        fun fromValue(v: Int): ThemeMode = entries.firstOrNull { it.value == v } ?: SYSTEM
    }
}

data class UserSettings(
    val sfxEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val onboarded: Boolean = false,
    val dailyStreak: Int = 0,
    val playerName: String = ""
)
