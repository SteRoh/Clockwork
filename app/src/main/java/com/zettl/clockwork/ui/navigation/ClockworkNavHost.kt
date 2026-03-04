package com.zettl.clockwork.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zettl.clockwork.data.preferences.UserPreferencesRepository
import com.zettl.clockwork.domain.lesson.LessonRepository
import com.zettl.clockwork.domain.level.LevelRepository
import com.zettl.clockwork.ui.screens.HomeScreen
import com.zettl.clockwork.ui.screens.LessonsScreen
import com.zettl.clockwork.ui.screens.LevelScreen
import kotlinx.coroutines.launch

@Composable
fun ClockworkNavHost(
    navController: NavHostController = rememberNavController(),
    preferences: UserPreferencesRepository,
    lessonRepository: LessonRepository,
    levelRepository: LevelRepository
) {
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                preferences = preferences,
                onNavigateToLessons = {
                    navController.navigate(NavRoutes.LESSONS) {
                        popUpTo(NavRoutes.HOME) { inclusive = false }
                    }
                }
            )
        }
        composable(NavRoutes.LESSONS) {
            LessonsScreen(
                lessonRepository = lessonRepository,
                onLessonClick = { lessonId ->
                    navController.navigate(NavRoutes.level(lessonId, 0))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.LEVEL) { backStackEntry ->
            val context = LocalContext.current
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: "1"
            val levelIndex = backStackEntry.arguments?.getString("levelIndex")?.toIntOrNull() ?: 0
            val lessonTitle = when (lessonId) {
                "1" -> context.getString(com.zettl.clockwork.R.string.lesson_1)
                "2" -> context.getString(com.zettl.clockwork.R.string.lesson_2)
                "3" -> context.getString(com.zettl.clockwork.R.string.lesson_3)
                "4" -> context.getString(com.zettl.clockwork.R.string.lesson_4)
                "5" -> context.getString(com.zettl.clockwork.R.string.lesson_5)
                "6" -> context.getString(com.zettl.clockwork.R.string.lesson_6)
                "7" -> context.getString(com.zettl.clockwork.R.string.lesson_7)
                else -> lessonId
            }
            LevelScreen(
                lessonTitle = lessonTitle,
                lessonId = lessonId,
                levelIndex = levelIndex,
                levelRepository = levelRepository,
                onLevelComplete = { earned ->
                    scope.launch {
                        preferences.addTalers(earned)
                        preferences.unlockLevel(lessonId, levelIndex + 1)
                    }
                    val hasNext = levelRepository.getLevel(lessonId, levelIndex + 1) != null
                    if (hasNext) {
                        navController.navigate(NavRoutes.level(lessonId, levelIndex + 1)) {
                            popUpTo(NavRoutes.LESSONS) { inclusive = false }
                            launchSingleTop = true
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
