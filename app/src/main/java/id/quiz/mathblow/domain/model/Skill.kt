package id.quiz.mathblow.domain.model

/**
 * Skill = jenis latihan mental-math. Semua dibangkitkan secara prosedural
 * (lihat [id.quiz.mathblow.domain.ProceduralQuestionGenerator]).
 */
enum class Skill(val title: String, val symbol: String, val maxLevel: Int = 5) {
    ADD("Penjumlahan", "+"),
    SUBTRACT("Pengurangan", "−"),
    MULTIPLY("Perkalian", "×"),
    DIVIDE("Pembagian", "÷"),
    PERCENT("Persen", "%"),
    SEQUENCE("Deret Angka", "…"),
    ORDER_OF_OPS("Urutan Operasi", "()"),
    POWERS("Kuadrat & Akar", "²"),
    TRUE_FALSE("Benar / Salah", "✓"),
    MISSING("Angka Hilang", "□");

    companion object {
        /** Empat operasi inti — ditonjolkan di Dashboard. */
        val core: List<Skill> get() = listOf(ADD, SUBTRACT, MULTIPLY, DIVIDE)
    }
}
