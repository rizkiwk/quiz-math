package com.avos.mathsharp.domain.model

/**
 * Satu soal siap-tampil. [options] adalah string agar seragam untuk jawaban
 * numerik maupun "Benar/Salah". [correctIndex] menunjuk opsi yang benar.
 */
data class Question(
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val skill: Skill,
    val difficulty: Int
) {
    val correctOption: String get() = options[correctIndex]
    fun isCorrect(selectedIndex: Int): Boolean = selectedIndex == correctIndex
}
