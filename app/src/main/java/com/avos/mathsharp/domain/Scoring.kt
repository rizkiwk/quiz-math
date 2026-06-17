package com.avos.mathsharp.domain

/** Aturan skor & bintang — terpusat, tanpa magic number tersebar. */
object Scoring {
    const val QUESTIONS_PER_WORKOUT = 10
    const val BASE_POINTS = 100
    const val ANSWER_TIME_MS = 8_000L          // batas waktu per soal

    /** Poin satu jawaban benar = base + bonus kecepatan + bonus combo. */
    fun pointsForAnswer(reactionMs: Long, comboCount: Int): Int {
        val speedBonus = when {
            reactionMs < 1_500 -> 50
            reactionMs < 3_000 -> 25
            else -> 0
        }
        val comboBonus = comboCount.coerceIn(0, 10) * 10
        return BASE_POINTS + speedBonus + comboBonus
    }

    /** 0–3 bintang dari akurasi. */
    fun stars(correct: Int, total: Int): Int {
        if (total == 0) return 0
        val acc = correct.toFloat() / total
        return when {
            acc >= 0.9f -> 3
            acc >= 0.7f -> 2
            acc >= 0.5f -> 1
            else -> 0
        }
    }

    fun accuracy(correct: Int, total: Int): Float =
        if (total == 0) 0f else correct.toFloat() / total
}
