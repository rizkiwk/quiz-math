package id.quiz.mathblow.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.graphicsLayer
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import id.quiz.mathblow.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.quiz.mathblow.domain.model.Difficulty
import id.quiz.mathblow.domain.model.Skill
import id.quiz.mathblow.ui.components.PrimaryButton
import id.quiz.mathblow.ui.components.SkillTile
import id.quiz.mathblow.ui.components.StatPill
import id.quiz.mathblow.ui.rememberAppContainer
import id.quiz.mathblow.ui.screens.onboarding.OnboardingScreen
import id.quiz.mathblow.ui.theme.AppTheme

private const val DAILY_LEVEL = 3

@Composable
fun DashboardScreen(
    onPlaySkill: (Skill, Int) -> Unit,
    onStats: () -> Unit,
    onAchievements: () -> Unit,
    onSettings: () -> Unit
) {
    val container = rememberAppContainer()
    val vm: DashboardViewModel = viewModel(factory = DashboardViewModel.factory(container))
    val state by vm.ui.collectAsStateWithLifecycle()

    when (state.onboarded) {
        null -> Box(Modifier.fillMaxSize())          // memuat — cegah flash
        false -> OnboardingScreen()
        true -> DashboardContent(state, onPlaySkill, onStats, onAchievements, onSettings)
    }
}

@Composable
private fun DashboardContent(
    state: DashboardUiState,
    onPlaySkill: (Skill, Int) -> Unit,
    onStats: () -> Unit,
    onAchievements: () -> Unit,
    onSettings: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    var pickerSkill by remember { mutableStateOf<Skill?>(null) }
    val greeting = if (state.playerName.isBlank()) "MathBlow" else "Halo, ${state.playerName} 👋"
    val pulse by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_pulse))
    val pulseProgress by animateLottieCompositionAsState(pulse, iterations = LottieConstants.IterateForever)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text(greeting, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Asah otakmu tiap hari", style = MaterialTheme.typography.bodyMedium, color = cs.onSurfaceVariant)
            }
            IconButton(onClick = onSettings) {
                Icon(Icons.Filled.Settings, contentDescription = "Pengaturan")
            }
        }

        Spacer(Modifier.height(16.dp))
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = AppTheme.extra.streak.copy(alpha = 0.16f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    LottieAnimation(
                        composition = pulse,
                        progress = { pulseProgress },
                        modifier = Modifier.size(76.dp).graphicsLayer { alpha = 0.6f }
                    )
                    Text("🔥", style = MaterialTheme.typography.headlineMedium)
                }
                Column(Modifier.padding(start = 16.dp)) {
                    Text(
                        "${state.streak} hari",
                        style = MaterialTheme.typography.headlineSmall,
                        color = AppTheme.extra.streak,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Streak harianmu", style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            StatPill("Total Benar", state.totalCorrect.toString(), cs.primary, Modifier.weight(1f))
            StatPill("Skor Terbaik", state.bestScore.toString(), cs.secondary, Modifier.weight(1f))
            StatPill("Akurasi", "${(state.accuracy * 100).toInt()}%", cs.tertiary, Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = "Latihan Harian",
            onClick = { onPlaySkill(Skill.MULTIPLY, DAILY_LEVEL) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Text("Pilih Latihan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        Skill.entries.chunked(2).forEach { rowSkills ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                rowSkills.forEach { skill ->
                    SkillTile(
                        skill = skill,
                        onClick = { pickerSkill = skill },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowSkills.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            PrimaryButton(
                text = "Statistik",
                onClick = onStats,
                container = cs.surfaceVariant,
                onContainer = cs.onSurface,
                modifier = Modifier.weight(1f)
            )
            PrimaryButton(
                text = "Pencapaian",
                onClick = onAchievements,
                container = cs.surfaceVariant,
                onContainer = cs.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(32.dp))
    }

    pickerSkill?.let { skill ->
        AlertDialog(
            onDismissRequest = { pickerSkill = null },
            title = { Text("${skill.title} — pilih kesulitan") },
            text = {
                Column {
                    Difficulty.entries.forEach { diff ->
                        PrimaryButton(
                            text = diff.title,
                            onClick = { pickerSkill = null; onPlaySkill(skill, diff.level) },
                            container = cs.surfaceVariant,
                            onContainer = cs.onSurface,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}
