package com.avos.mathsharp.domain.model

/** Tingkat kesulitan → level generator (1..5). Adaptive difficulty = pendorong retensi utama (dewasa). */
enum class Difficulty(val level: Int, val title: String) {
    EASY(1, "Mudah"),
    MEDIUM(3, "Sedang"),
    HARD(5, "Sulit")
}
