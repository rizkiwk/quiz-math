package com.avos.mathsharp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {

    // ── Sessions ──
    @Insert
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Query("SELECT * FROM workout_session ORDER BY playedAt DESC LIMIT :limit")
    fun recentSessions(limit: Int = 30): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT COUNT(*) FROM workout_session")
    fun totalSessions(): Flow<Int>

    @Query("SELECT COALESCE(SUM(correct), 0) FROM workout_session")
    fun totalCorrect(): Flow<Int>

    @Query("SELECT COALESCE(MAX(score), 0) FROM workout_session")
    fun bestScore(): Flow<Int>

    @Query("SELECT COALESCE(AVG(CAST(correct AS REAL) / total), 0) FROM workout_session WHERE total > 0")
    fun avgAccuracy(): Flow<Double>

    // ── Skill progress ──
    @Upsert
    suspend fun upsertProgress(progress: SkillProgressEntity)

    @Query("SELECT * FROM skill_progress WHERE skill = :skill")
    suspend fun progress(skill: String): SkillProgressEntity?

    @Query("SELECT * FROM skill_progress")
    fun allProgress(): Flow<List<SkillProgressEntity>>

    // ── Achievements ──
    @Upsert
    suspend fun upsertAchievements(items: List<AchievementEntity>)

    @Query("SELECT * FROM achievement ORDER BY unlocked DESC, code ASC")
    fun achievements(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievement")
    suspend fun achievementsOnce(): List<AchievementEntity>

    @Query("DELETE FROM workout_session")
    suspend fun clearSessions()

    @Query("DELETE FROM skill_progress")
    suspend fun clearProgress()
}
