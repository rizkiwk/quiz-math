package com.avos.mathsharp.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.avos.mathsharp.domain.model.Skill
import com.avos.mathsharp.ui.screens.achievements.AchievementsScreen
import com.avos.mathsharp.ui.screens.dashboard.DashboardScreen
import com.avos.mathsharp.ui.screens.result.ResultScreen
import com.avos.mathsharp.ui.screens.settings.SettingsScreen
import com.avos.mathsharp.ui.screens.stats.StatsScreen
import com.avos.mathsharp.ui.screens.workout.WorkoutScreen

@Composable
fun MathSharpNavHost(nav: NavHostController = rememberNavController()) {
    NavHost(
        navController = nav,
        startDestination = DashboardRoute,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        composable<DashboardRoute> {
            DashboardScreen(
                onPlaySkill = { skill, level -> nav.navigate(WorkoutRoute(skill.name, level)) },
                onStats = { nav.navigate(StatsRoute) },
                onAchievements = { nav.navigate(AchievementsRoute) },
                onSettings = { nav.navigate(SettingsRoute) }
            )
        }

        composable<WorkoutRoute> { entry ->
            val r = entry.toRoute<WorkoutRoute>()
            WorkoutScreen(
                skill = runCatching { Skill.valueOf(r.skill) }.getOrDefault(Skill.ADD),
                level = r.level,
                onFinish = { result ->
                    nav.navigate(
                        ResultRoute(result.skill.name, result.level, result.correct, result.total, result.score, result.stars)
                    ) {
                        popUpTo<WorkoutRoute> { inclusive = true }
                    }
                },
                onQuit = { nav.popBackStack() }
            )
        }

        composable<ResultRoute> { entry ->
            val r = entry.toRoute<ResultRoute>()
            ResultScreen(
                skill = runCatching { Skill.valueOf(r.skill) }.getOrDefault(Skill.ADD),
                level = r.level,
                correct = r.correct,
                total = r.total,
                score = r.score,
                stars = r.stars,
                onPlayAgain = {
                    nav.navigate(WorkoutRoute(r.skill, r.level)) {
                        popUpTo<ResultRoute> { inclusive = true }
                    }
                },
                onHome = { nav.popBackStack(DashboardRoute, inclusive = false) }
            )
        }

        composable<StatsRoute> { StatsScreen(onBack = { nav.popBackStack() }) }
        composable<AchievementsRoute> { AchievementsScreen(onBack = { nav.popBackStack() }) }
        composable<SettingsRoute> { SettingsScreen(onBack = { nav.popBackStack() }) }
    }
}
