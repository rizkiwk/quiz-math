package id.quiz.mathblow.ui.screens.onboarding

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.quiz.mathblow.domain.model.ThemeMode
import id.quiz.mathblow.ui.components.PrimaryButton
import id.quiz.mathblow.ui.rememberAppContainer
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen() {
    val container = rememberAppContainer()
    val scope = rememberCoroutineScope()
    val cs = MaterialTheme.colorScheme
    var name by remember { mutableStateOf("") }
    var theme by remember { mutableStateOf(ThemeMode.SYSTEM) }

    Column(
        modifier = Modifier.fillMaxSize().padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Selamat datang di", style = MaterialTheme.typography.titleMedium, color = cs.onSurfaceVariant)
        Text("MathBlow", style = MaterialTheme.typography.displayMedium, color = cs.primary, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Latihan mental math harian biar otakmu tetap tajam.",
            style = MaterialTheme.typography.bodyLarge,
            color = cs.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(36.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { if (it.length <= 16) name = it },
            label = { Text("Nama panggilan") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
        Text("Tema", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            ThemeChip("Sistem", theme == ThemeMode.SYSTEM, { theme = ThemeMode.SYSTEM }, Modifier.weight(1f))
            ThemeChip("Gelap", theme == ThemeMode.DARK, { theme = ThemeMode.DARK }, Modifier.weight(1f))
            ThemeChip("Terang", theme == ThemeMode.LIGHT, { theme = ThemeMode.LIGHT }, Modifier.weight(1f))
        }

        Spacer(Modifier.height(36.dp))
        PrimaryButton(
            text = "Mulai",
            onClick = {
                scope.launch {
                    container.settingsRepository.completeOnboarding(
                        name = name.ifBlank { "Sobat" },
                        mode = theme
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
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
