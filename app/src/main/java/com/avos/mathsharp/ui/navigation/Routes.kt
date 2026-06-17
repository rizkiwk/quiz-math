package com.avos.mathsharp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute

@Serializable
data class WorkoutRoute(val skill: String, val level: Int)

@Serializable
data class ResultRoute(
    val skill: String,
    val level: Int,
    val correct: Int,
    val total: Int,
    val score: Int,
    val stars: Int
)

@Serializable
data object StatsRoute

@Serializable
data object AchievementsRoute

@Serializable
data object SettingsRoute
