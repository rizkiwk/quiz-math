package id.quiz.mathblow.ui.screens.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.quiz.mathblow.domain.Scoring
import id.quiz.mathblow.domain.model.Skill
import id.quiz.mathblow.ui.components.PrimaryButton
import id.quiz.mathblow.ui.components.StarRow
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun ResultScreen(
    skill: Skill,
    level: Int,
    correct: Int,
    total: Int,
    score: Int,
    stars: Int,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val accuracy = (Scoring.accuracy(correct, total) * 100).toInt()

    // Count-up skor 0 → score
    var scoreTarget by remember { mutableIntStateOf(0) }
    val animatedScore by animateIntAsState(scoreTarget, animationSpec = tween(900), label = "score")
    LaunchedEffect(Unit) { scoreTarget = score }
    val subtitle = when (stars) {
        3 -> "Luar biasa! 🎯"
        2 -> "Bagus sekali!"
        1 -> "Terus berlatih!"
        else -> "Jangan menyerah!"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Selesai!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = cs.onSurfaceVariant)

            Spacer(Modifier.height(24.dp))
            StarRow(count = stars, starSize = 48)

            Spacer(Modifier.height(24.dp))
            Text(
                animatedScore.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = cs.primary,
                fontWeight = FontWeight.Bold
            )
            Text("Skor", style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)

            Spacer(Modifier.height(16.dp))
            Text(
                "${skill.title}  ·  Benar $correct/$total  ·  Akurasi $accuracy%",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(40.dp))
            PrimaryButton(text = "Main Lagi", onClick = onPlayAgain, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PrimaryButton(
                    text = "Beranda",
                    onClick = onHome,
                    container = cs.surfaceVariant,
                    onContainer = cs.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (stars >= 2) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(
                    Party(
                        emitter = Emitter(duration = 120, TimeUnit.MILLISECONDS).max(140),
                        spread = 360,
                        colors = listOf(0x4C8DFF, 0x2DD4BF, 0xF59E0B, 0xA78BFA),
                        position = Position.Relative(0.5, 0.25)
                    )
                )
            )
        }
    }
}
