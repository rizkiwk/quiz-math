package id.quiz.mathblow.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Riwayat satu sesi latihan — sumber statistik & grafik. */
@Entity(tableName = "workout_session")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val skill: String,
    val level: Int,
    val correct: Int,
    val total: Int,
    val score: Int,
    val stars: Int,
    val avgReactionMs: Long,
    val playedAt: Long = System.currentTimeMillis()
)

/** Rekor per-skill (best score/stars, jumlah main). */
@Entity(tableName = "skill_progress", indices = [Index(value = ["skill"], unique = true)])
data class SkillProgressEntity(
    @PrimaryKey val skill: String,
    val bestScore: Int = 0,
    val bestStars: Int = 0,
    val timesPlayed: Int = 0
)

/** Pencapaian (badge) — di-seed sekali saat pertama jalan. */
@Entity(tableName = "achievement")
data class AchievementEntity(
    @PrimaryKey val code: String,
    val unlocked: Boolean = false,
    val progress: Int = 0,
    val target: Int = 1,
    val unlockedAt: Long? = null
)
