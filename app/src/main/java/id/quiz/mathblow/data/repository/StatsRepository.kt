package id.quiz.mathblow.data.repository

import id.quiz.mathblow.data.local.AchievementEntity
import id.quiz.mathblow.data.local.SkillProgressEntity
import id.quiz.mathblow.data.local.StatsDao
import id.quiz.mathblow.data.local.WorkoutSessionEntity
import id.quiz.mathblow.domain.Achievements
import kotlinx.coroutines.flow.Flow
import kotlin.math.max

/** Statistik, rekor, dan pencapaian — semua lokal di Room (SQLite). */
class StatsRepository(private val dao: StatsDao) {

    val recentSessions: Flow<List<WorkoutSessionEntity>> = dao.recentSessions()
    val totalSessions: Flow<Int> = dao.totalSessions()
    val totalCorrect: Flow<Int> = dao.totalCorrect()
    val bestScore: Flow<Int> = dao.bestScore()
    val avgAccuracy: Flow<Double> = dao.avgAccuracy()
    val allProgress: Flow<List<SkillProgressEntity>> = dao.allProgress()
    val achievements: Flow<List<AchievementEntity>> = dao.achievements()

    /** Seed katalog pencapaian sekali (idempoten). */
    suspend fun ensureSeeded() {
        if (dao.achievementsOnce().isEmpty()) {
            dao.upsertAchievements(
                Achievements.all.map { AchievementEntity(code = it.code, target = it.target) }
            )
        }
    }

    /** Simpan hasil sesi + update rekor skill + evaluasi pencapaian. */
    suspend fun recordSession(session: WorkoutSessionEntity, dailyStreak: Int) {
        dao.insertSession(session)

        val prev = dao.progress(session.skill)
        dao.upsertProgress(
            SkillProgressEntity(
                skill = session.skill,
                bestScore = max(prev?.bestScore ?: 0, session.score),
                bestStars = max(prev?.bestStars ?: 0, session.stars),
                timesPlayed = (prev?.timesPlayed ?: 0) + 1
            )
        )

        evaluateAchievements(session, dailyStreak)
    }

    private suspend fun evaluateAchievements(session: WorkoutSessionEntity, dailyStreak: Int) {
        val current = dao.achievementsOnce().associateBy { it.code }
        val updates = mutableListOf<AchievementEntity>()
        val now = System.currentTimeMillis()

        fun unlock(code: String) {
            val a = current[code] ?: return
            if (!a.unlocked) updates.add(a.copy(unlocked = true, progress = a.target, unlockedAt = now))
        }

        fun setProgress(code: String, value: Int) {
            val a = current[code] ?: return
            if (a.unlocked) return
            val p = value.coerceAtMost(a.target)
            updates.add(a.copy(progress = p, unlocked = p >= a.target, unlockedAt = if (p >= a.target) now else null))
        }

        fun addProgress(code: String, delta: Int) {
            val a = current[code] ?: return
            setProgress(code, a.progress + delta)
        }

        unlock("first_workout")
        if (session.total > 0 && session.correct == session.total) unlock("perfect")
        if (session.score >= 1500) unlock("sharp_score")
        if (session.correct >= 10 && session.avgReactionMs in 1..1999) unlock("speed_demon")
        addProgress("centurion", session.correct)
        setProgress("streak_3", dailyStreak)
        setProgress("streak_7", dailyStreak)

        if (updates.isNotEmpty()) dao.upsertAchievements(updates)
    }

    suspend fun resetProgress() {
        dao.clearSessions()
        dao.clearProgress()
        // reset pencapaian ke kondisi awal
        dao.upsertAchievements(
            Achievements.all.map { AchievementEntity(code = it.code, target = it.target) }
        )
    }
}
