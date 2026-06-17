package com.avos.mathsharp

import com.avos.mathsharp.domain.ProceduralQuestionGenerator
import com.avos.mathsharp.domain.Scoring
import com.avos.mathsharp.domain.model.Skill
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.random.Random

class QuestionGeneratorTest {

    private val gen = ProceduralQuestionGenerator()

    private fun orderOfMag(n: Int) = if (n <= 0) 0 else floor(log10(n.toDouble())).toInt()

    private val numericSkills = listOf(
        Skill.ADD, Skill.SUBTRACT, Skill.MULTIPLY, Skill.DIVIDE,
        Skill.PERCENT, Skill.MISSING, Skill.ORDER_OF_OPS, Skill.POWERS, Skill.SEQUENCE
    )

    @Test
    fun allSkillsProduceStructurallyValidQuestions() {
        val rng = Random(42)
        for (skill in Skill.entries) {
            for (level in 1..skill.maxLevel) {
                repeat(50) {
                    val q = gen.generate(skill, level, rng)
                    assertTrue("prompt kosong untuk $skill", q.prompt.isNotBlank())
                    assertTrue("correctIndex di luar batas untuk $skill", q.correctIndex in q.options.indices)
                    assertEquals("opsi duplikat untuk $skill", q.options.size, q.options.toSet().size)
                    val expected = if (skill == Skill.TRUE_FALSE) 2 else 4
                    assertEquals("jumlah opsi salah untuk $skill", expected, q.options.size)
                }
            }
        }
    }

    @Test
    fun exactlyOneOptionIsTheCorrectAnswer() {
        val rng = Random(99)
        for (skill in Skill.entries) {
            repeat(60) {
                val q = gen.generate(skill, 3, rng)
                val occurrences = q.options.count { it == q.correctOption }
                assertEquals("harus tepat 1 opsi benar untuk $skill", 1, occurrences)
            }
        }
    }

    /** Bukti: correctIndex benar-benar menunjuk hasil aritmetika yang tepat. */
    @Test
    fun coreArithmeticAnswersAreActuallyCorrect() {
        val rng = Random(7)
        val ops = listOf(Skill.ADD, Skill.SUBTRACT, Skill.MULTIPLY, Skill.DIVIDE)
        for (skill in ops) {
            for (level in 1..5) {
                repeat(80) {
                    val q = gen.generate(skill, level, rng)
                    val nums = Regex("\\d+").findAll(q.prompt).map { it.value.toInt() }.toList()
                    val a = nums[0]; val b = nums[1]
                    val expected = when (skill) {
                        Skill.ADD -> a + b
                        Skill.SUBTRACT -> a - b
                        Skill.MULTIPLY -> a * b
                        else -> a / b   // DIVIDE: prompt "dividend ÷ divisor = ?"
                    }
                    assertEquals("jawaban salah pada '${q.prompt}' ($skill)", expected, q.correctOption.toInt())
                }
            }
        }
    }

    /** Bukti: tidak ada opsi yang "salah jelas karena ukuran" — semua dalam 1 orde magnitude. */
    @Test
    fun distractorsStayWithinOneOrderOfMagnitude() {
        val rng = Random(123)
        for (skill in numericSkills) {
            for (level in 1..5) {
                repeat(60) {
                    val q = gen.generate(skill, level, rng)
                    val correct = q.correctOption.toInt()
                    q.options.forEach { opt ->
                        val v = opt.toInt()
                        assertTrue("opsi negatif $v ($skill)", v >= 0)
                        assertTrue(
                            "opsi $v jauh dari jawaban $correct ($skill / '${q.prompt}')",
                            abs(orderOfMag(v) - orderOfMag(correct)) <= 1
                        )
                    }
                }
            }
        }
    }

    @Test
    fun trueFalseStatementMatchesCorrectIndex() {
        val rng = Random(55)
        repeat(300) {
            val q = gen.generate(Skill.TRUE_FALSE, 3, rng)
            assertEquals(listOf("Benar", "Salah"), q.options)
            val nums = Regex("\\d+").findAll(q.prompt).map { it.value.toInt() }.toList()
            val a = nums[0]; val b = nums[1]; val shown = nums[2]
            val shouldBeBenar = (a * b == shown)
            assertEquals("pernyataan '${q.prompt}'", if (shouldBeBenar) 0 else 1, q.correctIndex)
        }
    }

    @Test
    fun deterministicForSameSeed() {
        val q1 = gen.generate(Skill.MULTIPLY, 3, Random(321))
        val q2 = gen.generate(Skill.MULTIPLY, 3, Random(321))
        assertEquals(q1.prompt, q2.prompt)
        assertEquals(q1.options, q2.options)
        assertEquals(q1.correctIndex, q2.correctIndex)
    }

    @Test
    fun scoringStarsBoundaries() {
        assertEquals(3, Scoring.stars(10, 10))
        assertEquals(3, Scoring.stars(9, 10))
        assertEquals(2, Scoring.stars(7, 10))
        assertEquals(1, Scoring.stars(5, 10))
        assertEquals(0, Scoring.stars(4, 10))
        assertEquals(0, Scoring.stars(0, 0))
    }

    @Test
    fun pointsIncreaseWithSpeedAndCombo() {
        val slow = Scoring.pointsForAnswer(5_000, 0)
        val fast = Scoring.pointsForAnswer(1_000, 0)
        val fastCombo = Scoring.pointsForAnswer(1_000, 5)
        assertTrue(fast > slow)
        assertTrue(fastCombo > fast)
    }
}
