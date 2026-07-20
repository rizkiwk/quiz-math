package id.quiz.mathblow.domain

import id.quiz.mathblow.domain.model.Question
import id.quiz.mathblow.domain.model.Skill
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.random.Random

interface QuestionGenerator {
    fun generate(skill: Skill, level: Int, rng: Random = Random.Default): Question
}

/**
 * Generator soal 100% prosedural — nol bank-soal, replay tak terbatas.
 *
 * Distractor (opsi salah) dibangun dari MODEL KESALAHAN MASUK AKAL
 * (off-by-one, salah operasi, error simpan/pinjam, tetangga tabel), lalu difilter
 * agar setiap opsi: berbeda, non-negatif, dan tidak sama dengan jawaban benar.
 * Random murni dihindari karena gampang dieliminasi lewat magnitude.
 */
class ProceduralQuestionGenerator : QuestionGenerator {

    override fun generate(skill: Skill, level: Int, rng: Random): Question = when (skill) {
        Skill.ADD -> addSub(skill, level, rng, subtract = false)
        Skill.SUBTRACT -> addSub(skill, level, rng, subtract = true)
        Skill.MULTIPLY -> multiply(level, rng)
        Skill.DIVIDE -> divide(level, rng)
        Skill.PERCENT -> percent(level, rng)
        Skill.SEQUENCE -> sequence(level, rng)
        Skill.ORDER_OF_OPS -> orderOfOps(level, rng)
        Skill.POWERS -> powers(level, rng)
        Skill.TRUE_FALSE -> trueFalse(level, rng)
        Skill.MISSING -> missing(level, rng)
    }

    // ── Generator per skill ─────────────────────────────────────────────────

    private fun addSub(skill: Skill, level: Int, rng: Random, subtract: Boolean): Question {
        val max = when (level.coerceIn(1, 5)) { 1 -> 9; 2 -> 20; 3 -> 99; 4 -> 499; else -> 999 }
        var a = rng.nextInt(1, max + 1)
        var b = rng.nextInt(1, max + 1)
        if (subtract && b > a) { val t = a; a = b; b = t }
        val correct = if (subtract) a - b else a + b
        val sym = if (subtract) "−" else "+"
        val cand = listOf(
            correct + 1, correct - 1, correct + 10, correct - 10,
            abs(a - b), a + b, correct + 9, correct - 9
        )
        return numericQuestion("$a $sym $b = ?", correct, skill, level, rng, cand)
    }

    private fun multiply(level: Int, rng: Random): Question {
        val (amax, bmax) = when (level.coerceIn(1, 5)) {
            1 -> 5 to 5; 2 -> 9 to 9; 3 -> 12 to 12; 4 -> 20 to 12; else -> 99 to 9
        }
        val a = rng.nextInt(2, amax + 1)
        val b = rng.nextInt(2, bmax + 1)
        val correct = a * b
        val cand = listOf(
            a * (b + 1), a * (b - 1), (a + 1) * b, (a - 1) * b,
            a + b, correct + 1, correct - 1, correct + 10
        )
        return numericQuestion("$a × $b = ?", correct, Skill.MULTIPLY, level, rng, cand)
    }

    private fun divide(level: Int, rng: Random): Question {
        val (dmax, qmax) = when (level.coerceIn(1, 5)) {
            1 -> 5 to 5; 2 -> 9 to 9; 3 -> 12 to 9; 4 -> 12 to 12; else -> 20 to 12
        }
        val d = rng.nextInt(2, dmax + 1)
        val q = rng.nextInt(2, qmax + 1)
        val a = d * q
        val cand = listOf(q + 1, q - 1, q + 2, q - 2, d, a - d, q + 10)
        return numericQuestion("$a ÷ $d = ?", q, Skill.DIVIDE, level, rng, cand)
    }

    private fun percent(level: Int, rng: Random): Question {
        val pcts = listOf(5, 10, 15, 20, 25, 50)
        val pct = pcts.random(rng)
        val k = rng.nextInt(1, level.coerceIn(1, 5) * 4 + 1)
        val base = 20 * k                       // kelipatan 20 → semua pct di atas pasti bulat
        val correct = base * pct / 100
        val others = pcts.filter { it != pct }.map { base * it / 100 }
        val cand = (others + listOf(correct + 1, correct - 1, correct * 2, base - correct))
        return numericQuestion("$pct% dari $base = ?", correct, Skill.PERCENT, level, rng, cand)
    }

    private fun sequence(level: Int, rng: Random): Question {
        val geometric = level >= 3 && rng.nextBoolean()
        return if (geometric) {
            val start = rng.nextInt(1, 4)
            val ratio = listOf(2, 3).random(rng)
            val terms = mutableListOf(start)
            repeat(4) { terms.add(terms.last() * ratio) }
            val correct = terms[4]
            val shown = terms.take(4).joinToString(", ")
            val cand = listOf(
                correct + ratio, correct - ratio, terms[3] + (terms[3] - terms[2]),
                correct + terms[3], correct + 1, correct - 1
            )
            numericQuestion("$shown, ?", correct, Skill.SEQUENCE, level, rng, cand)
        } else {
            val lvl = level.coerceIn(1, 5)
            val start = rng.nextInt(1, 10 * lvl + 1)
            val step = rng.nextInt(1, 5 * lvl + 1)
            val terms = (0..4).map { start + step * it }
            val correct = terms[4]
            val shown = terms.take(4).joinToString(", ")
            val cand = listOf(
                correct + step, correct - step, correct + 1, correct - 1,
                correct + 2 * step, terms[3] + step + 1
            )
            numericQuestion("$shown, ?", correct, Skill.SEQUENCE, level, rng, cand)
        }
    }

    private fun orderOfOps(level: Int, rng: Random): Question {
        val max = when (level.coerceIn(1, 5)) { 1 -> 5; 2 -> 9; else -> 12 }
        val a = rng.nextInt(1, max + 1)
        val b = rng.nextInt(2, max + 1)
        val c = rng.nextInt(2, max + 1)
        val correct = a + b * c
        val wrongLeftToRight = (a + b) * c          // jebakan: hitung kiri→kanan
        val cand = listOf(wrongLeftToRight, correct + 1, correct - 1, a + b + c, correct + b, correct - c)
        return numericQuestion("$a + $b × $c = ?", correct, Skill.ORDER_OF_OPS, level, rng, cand)
    }

    private fun powers(level: Int, rng: Random): Question {
        val nmax = when (level.coerceIn(1, 5)) { 1 -> 6; 2 -> 10; 3 -> 12; 4 -> 15; else -> 20 }
        val n = rng.nextInt(2, nmax + 1)
        return if (rng.nextBoolean()) {
            val correct = n * n
            val cand = listOf(correct + 1, correct - 1, (n + 1) * (n + 1), (n - 1) * (n - 1), n * 2, correct + 10)
            numericQuestion("${n}² = ?", correct, Skill.POWERS, level, rng, cand)
        } else {
            val sq = n * n
            val cand = listOf(n + 1, n - 1, n + 2, n - 2, n * 2, sq / 2)
            numericQuestion("√$sq = ?", n, Skill.POWERS, level, rng, cand)
        }
    }

    private fun trueFalse(level: Int, rng: Random): Question {
        val max = when (level.coerceIn(1, 5)) { 1 -> 9; 2 -> 12; else -> 20 }
        val a = rng.nextInt(2, max + 1)
        val b = rng.nextInt(2, max + 1)
        val real = a * b
        val isTrue = rng.nextBoolean()
        val shown = if (isTrue) real else {
            var off = listOf(-2, -1, 1, 2, 10, -10).random(rng)
            if (real + off <= 0) off = 2
            real + off
        }
        val correctIndex = if (shown == real) 0 else 1  // 0 = Benar, 1 = Salah
        return Question("$a × $b = $shown", listOf("Benar", "Salah"), correctIndex, Skill.TRUE_FALSE, level)
    }

    private fun missing(level: Int, rng: Random): Question {
        val max = when (level.coerceIn(1, 5)) { 1 -> 20; 2 -> 50; 3 -> 99; else -> 499 }
        val a = rng.nextInt(1, max + 1)
        val x = rng.nextInt(1, max + 1)
        val sum = a + x
        val cand = listOf(x + 1, x - 1, x + 10, x - 10, sum, a, x + 2, x - 2)
        return numericQuestion("$a + ? = $sum", x, Skill.MISSING, level, rng, cand)
    }

    // ── Inti: rakit opsi numerik dengan guard ────────────────────────────────

    private fun numericQuestion(
        prompt: String,
        correct: Int,
        skill: Skill,
        level: Int,
        rng: Random,
        candidates: List<Int>,
        optionCount: Int = 4
    ): Question {
        val distractors = LinkedHashSet<Int>()
        // 1) kandidat "kesalahan masuk akal" yang lolos guard magnitude
        for (c in candidates.shuffled(rng)) {
            if (distractors.size >= optionCount - 1) break
            if (validDistractor(c, correct)) distractors.add(c)
        }
        // 2) backfill nilai dekat (correct ± k) yang lolos guard
        var k = 1
        while (distractors.size < optionCount - 1 && k <= 200) {
            for (c in listOf(correct + k, correct - k)) {
                if (distractors.size >= optionCount - 1) break
                if (validDistractor(c, correct)) distractors.add(c)
            }
            k++
        }
        // 3) jaminan terakhir (praktis tak terpakai): apa pun yang beda & non-negatif
        var pad = 1
        while (distractors.size < optionCount - 1) {
            val c = correct + pad
            if (c >= 0 && c != correct) distractors.add(c)
            pad++
        }
        val ordered = (distractors.toList() + correct).shuffled(rng)
        return Question(
            prompt = prompt,
            options = ordered.map { it.toString() },
            correctIndex = ordered.indexOf(correct),
            skill = skill,
            difficulty = level
        )
    }

    /**
     * Distractor valid bila: non-negatif, beda dari jawaban, DAN berada dalam
     * SATU ORDE magnitude dari jawaban — mencegah opsi "salah jelas" karena
     * ukurannya jauh (mis. opsi 3 saat jawaban 560 mudah dieliminasi).
     */
    private fun validDistractor(d: Int, correct: Int): Boolean =
        d >= 0 && d != correct && orderDiff(d, correct) <= 1

    private fun orderOfMagnitude(n: Int): Int =
        if (n <= 0) 0 else floor(log10(n.toDouble())).toInt()

    private fun orderDiff(a: Int, b: Int): Int =
        abs(orderOfMagnitude(a) - orderOfMagnitude(b))
}
