package id.quiz.mathblow.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.quiz.mathblow.domain.model.Skill
import id.quiz.mathblow.ui.theme.AppTheme

enum class AnswerState { IDLE, CORRECT, WRONG, REVEAL }

@Composable
fun ScreenHeader(title: String, onBack: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = if (onBack != null) 0.dp else 8.dp)
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    container: Color = MaterialTheme.colorScheme.primary,
    onContainer: Color = MaterialTheme.colorScheme.onPrimary
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "press"
    )
    Surface(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        shape = RoundedCornerShape(24.dp),
        color = if (enabled) container else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (enabled) onContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .scale(scale)
            .height(60.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun AnswerButton(
    text: String,
    state: AnswerState,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val success = AppTheme.extra.success
    val target = when (state) {
        AnswerState.IDLE -> cs.surfaceVariant
        AnswerState.CORRECT, AnswerState.REVEAL -> success.copy(alpha = 0.22f)
        AnswerState.WRONG -> cs.error.copy(alpha = 0.20f)
    }
    val bg by animateColorAsState(target, label = "answerBg")
    val border = when (state) {
        AnswerState.CORRECT, AnswerState.REVEAL -> success
        AnswerState.WRONG -> cs.error
        AnswerState.IDLE -> cs.outline
    }
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        color = bg,
        border = BorderStroke(if (state == AnswerState.IDLE) 1.dp else 2.dp, border),
        modifier = modifier.height(64.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                color = cs.onSurface,
                textAlign = TextAlign.Center
            )
            if (state == AnswerState.CORRECT || state == AnswerState.REVEAL) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = success, modifier = Modifier.padding(start = 8.dp))
            } else if (state == AnswerState.WRONG) {
                Icon(Icons.Filled.Close, contentDescription = null, tint = cs.error, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun SkillTile(skill: Skill, locked: Boolean = false, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        enabled = !locked,
        shape = RoundedCornerShape(20.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(cs.primary.copy(alpha = 0.16f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (locked) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = cs.onSurfaceVariant)
                } else {
                    Text(skill.symbol, style = MaterialTheme.typography.headlineSmall, color = cs.primary)
                }
            }
            Text(
                skill.title,
                style = MaterialTheme.typography.titleMedium,
                color = cs.onSurface,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun StatPill(label: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = accent, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
        }
    }
}

@Composable
fun StarRow(count: Int, max: Int = 3, starSize: Int = 28) {
    val streak = AppTheme.extra.streak
    val cs = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(max) { i ->
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = if (i < count) streak else cs.outline,
                modifier = Modifier.size(starSize.dp)
            )
        }
    }
}
