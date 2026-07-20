package id.quiz.mathblow.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.quiz.mathblow.BuildConfig
import id.quiz.mathblow.domain.model.ThemeMode
import id.quiz.mathblow.ui.components.ScreenHeader
import id.quiz.mathblow.ui.rememberAppContainer

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val container = rememberAppContainer()
    val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(container))
    val s by vm.ui.collectAsStateWithLifecycle()
    val cs = MaterialTheme.colorScheme

    var showReset by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        ScreenHeader("Pengaturan", onBack)
        Column(modifier = Modifier.padding(horizontal = 4.dp)) {
            ToggleRow("Efek Suara", s.sfxEnabled, vm::setSfx)
            ToggleRow("Musik", s.musicEnabled, vm::setMusic)

            Spacer(Modifier.height(16.dp))
            Text("Tema", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                ThemeChip("Sistem", s.themeMode == ThemeMode.SYSTEM, { vm.setTheme(ThemeMode.SYSTEM) }, Modifier.weight(1f))
                ThemeChip("Gelap", s.themeMode == ThemeMode.DARK, { vm.setTheme(ThemeMode.DARK) }, Modifier.weight(1f))
                ThemeChip("Terang", s.themeMode == ThemeMode.LIGHT, { vm.setTheme(ThemeMode.LIGHT) }, Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = cs.outline)
            Spacer(Modifier.height(8.dp))

            ClickRow("Reset Progres", color = cs.error) { showReset = true }
            ClickRow("Kebijakan Privasi") { showPrivacy = true }

            Spacer(Modifier.height(24.dp))
            Text(
                "MathBlow v${BuildConfig.VERSION_NAME}  ·  Offline  ·  Tanpa izin  ·  Data lokal",
                style = MaterialTheme.typography.labelMedium,
                color = cs.onSurfaceVariant
            )
        }
    }

    if (showReset) {
        AlertDialog(
            onDismissRequest = { showReset = false },
            title = { Text("Reset semua progres?") },
            text = { Text("Statistik, skor, streak, dan pencapaian akan dihapus permanen. Tindakan ini tidak bisa dibatalkan.") },
            confirmButton = {
                TextButton(onClick = { vm.resetProgress(); showReset = false }) {
                    Text("Reset", color = cs.error)
                }
            },
            dismissButton = { TextButton(onClick = { showReset = false }) { Text("Batal") } }
        )
    }

    if (showPrivacy) {
        AlertDialog(
            onDismissRequest = { showPrivacy = false },
            title = { Text("Kebijakan Privasi") },
            text = {
                Text(
                    "MathBlow TIDAK mengumpulkan data apa pun. Tanpa izin sistem, tanpa internet, " +
                        "tanpa SDK pihak ketiga, tanpa analitik. Semua data (statistik, skor, streak) tersimpan " +
                        "lokal di perangkatmu dan terhapus saat aplikasi di-uninstall."
                )
            },
            confirmButton = { TextButton(onClick = { showPrivacy = false }) { Text("Mengerti") } }
        )
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun ThemeChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) cs.primary else cs.surfaceVariant,
        contentColor = if (selected) cs.onPrimary else cs.onSurface,
        modifier = modifier.height(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun ClickRow(label: String, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit) {
    Surface(onClick = onClick, color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            modifier = Modifier.padding(vertical = 14.dp)
        )
    }
}
