package com.avos.mathsharp.ui.screens.achievements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
import com.avos.mathsharp.data.local.AchievementEntity
import com.avos.mathsharp.domain.Achievements
import com.avos.mathsharp.ui.components.ScreenHeader
import com.avos.mathsharp.ui.rememberAppContainer
import com.avos.mathsharp.ui.theme.AppTheme

@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    val container = rememberAppContainer()
    val vm: AchievementsViewModel = viewModel(factory = AchievementsViewModel.factory(container))
    val items by vm.achievements.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        ScreenHeader("Pencapaian", onBack)
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
        ) {
            items(items, key = { it.code }) { a -> AchievementRow(a) }
        }
    }
}

@Composable
private fun AchievementRow(a: AchievementEntity) {
    val cs = MaterialTheme.colorScheme
    val streak = AppTheme.extra.streak
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (a.unlocked) streak.copy(alpha = 0.12f) else cs.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(if (a.unlocked) streak.copy(alpha = 0.25f) else cs.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (a.unlocked) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = streak)
                } else {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = cs.onSurfaceVariant)
                }
            }
            Column(Modifier.weight(1f).padding(start = 16.dp)) {
                Text(
                    Achievements.titleOf(a.code),
                    style = MaterialTheme.typography.titleMedium,
                    color = cs.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    Achievements.descriptionOf(a.code),
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant
                )
                if (!a.unlocked && a.target > 1) {
                    Text(
                        "${a.progress}/${a.target}",
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.primary
                    )
                }
            }
            if (a.unlocked) {
                Text("Terbuka", style = MaterialTheme.typography.labelMedium, color = streak, fontWeight = FontWeight.Bold)
            }
        }
    }
}
