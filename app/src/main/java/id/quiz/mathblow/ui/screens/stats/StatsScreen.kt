package id.quiz.mathblow.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.quiz.mathblow.data.local.WorkoutSessionEntity
import id.quiz.mathblow.domain.model.Skill
import id.quiz.mathblow.ui.components.ScreenHeader
import id.quiz.mathblow.ui.components.StarRow
import id.quiz.mathblow.ui.components.StatPill
import id.quiz.mathblow.ui.rememberAppContainer

@Composable
fun StatsScreen(onBack: () -> Unit) {
    val container = rememberAppContainer()
    val vm: StatsViewModel = viewModel(factory = StatsViewModel.factory(container))
    val state by vm.ui.collectAsStateWithLifecycle()
    val cs = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        ScreenHeader("Statistik", onBack)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
            StatPill("Total Benar", state.totalCorrect.toString(), cs.primary, Modifier.weight(1f))
            StatPill("Skor Terbaik", state.bestScore.toString(), cs.secondary, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
            StatPill("Akurasi", "${(state.accuracy * 100).toInt()}%", cs.tertiary, Modifier.weight(1f))
            StatPill("Total Sesi", state.totalSessions.toString(), cs.primary, Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))
        Text("Riwayat", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 4.dp))
        Spacer(Modifier.height(8.dp))

        if (state.sessions.isEmpty()) {
            Text(
                "Belum ada latihan. Mulai dari beranda!",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)
            ) {
                items(state.sessions, key = { it.id }) { session -> SessionRow(session) }
            }
        }
    }
}

@Composable
private fun SessionRow(session: WorkoutSessionEntity) {
    val cs = MaterialTheme.colorScheme
    val title = runCatching { Skill.valueOf(session.skill).title }.getOrDefault(session.skill)
    Surface(shape = RoundedCornerShape(16.dp), color = cs.surface, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = cs.onSurface)
                Text(
                    "Benar ${session.correct}/${session.total}  ·  Skor ${session.score}",
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant
                )
            }
            StarRow(count = session.stars, starSize = 18)
        }
    }
}
