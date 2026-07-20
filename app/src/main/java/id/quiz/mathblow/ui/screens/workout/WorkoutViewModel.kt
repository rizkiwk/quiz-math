package id.quiz.mathblow.ui.screens.workout

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import id.quiz.mathblow.audio.SoundManager
import id.quiz.mathblow.data.local.WorkoutSessionEntity
import id.quiz.mathblow.data.repository.SettingsRepository
import id.quiz.mathblow.data.repository.StatsRepository
import id.quiz.mathblow.di.AppContainer
import id.quiz.mathblow.domain.QuestionGenerator
import id.quiz.mathblow.domain.Scoring
import id.quiz.mathblow.domain.model.Question
import id.quiz.mathblow.domain.model.Skill
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

data class WorkoutResult(
    val skill: Skill,
    val level: Int,
    val correct: Int,
    val total: Int,
    val score: Int,
    val stars: Int
)

data class WorkoutUiState(
    val question: Question? = null,
    val questionNumber: Int = 0,
    val total: Int = Scoring.QUESTIONS_PER_WORKOUT,
    val score: Int = 0,
    val correctCount: Int = 0,
    val combo: Int = 0,
    val selectedIndex: Int? = null,
    val answered: Boolean = false,
    val lastCorrect: Boolean? = null,
    val timeFraction: Float = 1f,
    val finished: Boolean = false
)

class WorkoutViewModel(
    private val skill: Skill,
    private val level: Int,
    private val generator: QuestionGenerator,
    private val stats: StatsRepository,
    private val settings: SettingsRepository,
    private val sound: SoundManager
) : ViewModel() {

    private val rng = Random(SystemClock.elapsedRealtimeNanos())
    private val seenPrompts = HashSet<String>()
    private val _ui = MutableStateFlow(WorkoutUiState())
    val ui = _ui.asStateFlow()

    private val _finish = Channel<WorkoutResult>(Channel.BUFFERED)
    val finishEvents = _finish.receiveAsFlow()

    private var timerJob: Job? = null
    private var questionStart = 0L
    private var reactionSum = 0L
    private var reactionCount = 0

    init {
        viewModelScope.launch {
            settings.settings.collect { sound.enabled = it.sfxEnabled }
        }
        nextQuestion()
    }

    fun submit(index: Int?) {
        val s = _ui.value
        val q = s.question ?: return
        if (s.answered) return
        timerJob?.cancel()

        val isCorrect = index != null && q.isCorrect(index)
        val reaction = SystemClock.elapsedRealtime() - questionStart
        if (isCorrect) {
            reactionSum += reaction
            reactionCount++
        }
        val newCombo = if (isCorrect) s.combo + 1 else 0
        val gained = if (isCorrect) Scoring.pointsForAnswer(reaction, s.combo) else 0
        sound.play(if (isCorrect) "correct" else "wrong")

        _ui.update {
            it.copy(
                selectedIndex = index,
                answered = true,
                lastCorrect = isCorrect,
                score = it.score + gained,
                combo = newCombo,
                correctCount = it.correctCount + if (isCorrect) 1 else 0
            )
        }

        viewModelScope.launch {
            delay(REVEAL_DELAY_MS)
            advance()
        }
    }

    private fun advance() {
        if (_ui.value.questionNumber >= _ui.value.total) finish() else nextQuestion()
    }

    private fun nextQuestion() {
        val number = _ui.value.questionNumber + 1
        // Ruang soal level rendah kecil (mis. perkalian lvl 1) — hindari soal kembar sesesi.
        var q = generator.generate(skill, level, rng)
        var retries = 0
        while (q.prompt in seenPrompts && retries < MAX_DUPLICATE_RETRIES) {
            q = generator.generate(skill, level, rng)
            retries++
        }
        seenPrompts += q.prompt
        questionStart = SystemClock.elapsedRealtime()
        _ui.update {
            it.copy(
                question = q,
                questionNumber = number,
                selectedIndex = null,
                answered = false,
                lastCorrect = null,
                timeFraction = 1f
            )
        }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = Scoring.ANSWER_TIME_MS
            while (remaining > 0 && isActive) {
                _ui.update { it.copy(timeFraction = remaining.toFloat() / Scoring.ANSWER_TIME_MS) }
                delay(TICK_MS)
                remaining -= TICK_MS
            }
            if (isActive && !_ui.value.answered) submit(null) // timeout = salah
        }
    }

    private fun finish() {
        timerJob?.cancel()
        val s = _ui.value
        val stars = Scoring.stars(s.correctCount, s.total)
        val avgReaction = if (reactionCount > 0) reactionSum / reactionCount else 0L
        sound.play("finish")
        _ui.update { it.copy(finished = true) }
        viewModelScope.launch {
            val streak = settings.registerPlayedToday()
            stats.recordSession(
                WorkoutSessionEntity(
                    skill = skill.name,
                    level = level,
                    correct = s.correctCount,
                    total = s.total,
                    score = s.score,
                    stars = stars,
                    avgReactionMs = avgReaction
                ),
                dailyStreak = streak
            )
            _finish.send(WorkoutResult(skill, level, s.correctCount, s.total, s.score, stars))
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
    }

    companion object {
        private const val REVEAL_DELAY_MS = 750L
        private const val TICK_MS = 50L
        private const val MAX_DUPLICATE_RETRIES = 8

        fun factory(skill: Skill, level: Int, container: AppContainer): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    WorkoutViewModel(
                        skill = skill,
                        level = level,
                        generator = container.questionGenerator,
                        stats = container.statsRepository,
                        settings = container.settingsRepository,
                        sound = container.soundManager
                    )
                }
            }
    }
}
