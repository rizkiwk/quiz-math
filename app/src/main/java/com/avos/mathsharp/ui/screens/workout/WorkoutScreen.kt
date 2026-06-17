package com.avos.mathsharp.ui.screens.workout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avos.mathsharp.domain.model.Skill
import com.avos.mathsharp.ui.components.AnswerButton
import com.avos.mathsharp.ui.components.AnswerState
import com.avos.mathsharp.ui.rememberAppContainer
import com.avos.mathsharp.ui.theme.AppTheme

@Composable
fun WorkoutScreen(
    skill: Skill,
    level: Int,
    onFinish: (WorkoutResult) -> Unit,
    onQuit: () -> Unit
) {
    val container = rememberAppContainer()
    val vm: WorkoutViewModel = viewModel(
        key = "workout_${skill.name}_$level",
        factory = WorkoutViewModel.factory(skill, level, container)
    )
    val state by vm.ui.collectAsStateWithLifecycle()
    val haptics = LocalHapticFeedback.current
    val cs = MaterialTheme.colorScheme

    LaunchedEffectFinish(vm, onFinish)

    androidx.compose.runtime.LaunchedEffect(state.questionNumber, state.answered) {
        if (state.answered) {
            haptics.performHapticFeedback(
                if (state.lastCorrect == true) HapticFeedbackType.LongPress
                else HapticFeedbackType.TextHandleMove
            )
        }
    }

    val shakeX = remember { Animatable(0f) }
    androidx.compose.runtime.LaunchedEffect(state.questionNumber, state.answered) {
        if (state.answered && state.lastCorrect == false) {
            for (dx in listOf(-14f, 14f, -10f, 10f, -5f, 0f)) shakeX.animateTo(dx, tween(45))
        }
    }

    val q = state.question
    val timeFrac by animateFloatAsState(state.timeFraction, label = "timer")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onQuit) { Icon(Icons.Filled.Close, contentDescription = "Keluar") }
            Text(
                "${state.questionNumber}/${state.total}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                state.score.toString(),
                style = MaterialTheme.typography.titleLarge,
                color = cs.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { timeFrac },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            trackColor = cs.surfaceVariant,
            color = if (timeFrac < 0.25f) cs.error else cs.primary
        )

        Spacer(Modifier.weight(1f))
        if (state.combo >= 2) {
            Text(
                "🔥 Combo ${state.combo}",
                style = MaterialTheme.typography.titleMedium,
                color = AppTheme.extra.streak,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(shakeX.value.roundToInt(), 0) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = q?.prompt ?: "",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.weight(1f))

        if (q != null) {
            val opts = q.options
            for (rowStart in opts.indices step 2) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    for (i in rowStart until minOf(rowStart + 2, opts.size)) {
                        val answerState = when {
                            !state.answered -> AnswerState.IDLE
                            i == q.correctIndex -> AnswerState.CORRECT
                            i == state.selectedIndex -> AnswerState.WRONG
                            else -> AnswerState.IDLE
                        }
                        AnswerButton(
                            text = opts[i],
                            state = answerState,
                            enabled = !state.answered,
                            onClick = { vm.submit(i) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (opts.size - rowStart == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun LaunchedEffectFinish(vm: WorkoutViewModel, onFinish: (WorkoutResult) -> Unit) {
    androidx.compose.runtime.LaunchedEffect(vm) {
        vm.finishEvents.collect { onFinish(it) }
    }
}
