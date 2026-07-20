package id.quiz.mathblow.domain

/** Definisi statis pencapaian — sumber kebenaran untuk seeding & label UI. */
data class AchievementDef(
    val code: String,
    val title: String,
    val description: String,
    val target: Int
)

object Achievements {
    val all: List<AchievementDef> = listOf(
        AchievementDef("first_workout", "Langkah Pertama", "Selesaikan 1 latihan", 1),
        AchievementDef("perfect", "Sempurna", "Skor 100% dalam satu latihan", 1),
        AchievementDef("sharp_score", "Tajam", "Capai skor 1500 dalam satu latihan", 1),
        AchievementDef("speed_demon", "Kilat", "Rata-rata jawab cepat (< 2 dtk) dgn 10 benar", 1),
        AchievementDef("streak_3", "Konsisten", "Streak 3 hari berturut-turut", 3),
        AchievementDef("streak_7", "Tujuh Hari", "Streak 7 hari berturut-turut", 7),
        AchievementDef("centurion", "Seratus", "Total 100 jawaban benar", 100),
    )

    fun titleOf(code: String): String = all.firstOrNull { it.code == code }?.title ?: code
    fun descriptionOf(code: String): String = all.firstOrNull { it.code == code }?.description ?: ""
}
