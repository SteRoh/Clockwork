package com.zettl.clockwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.zettl.clockwork.data.preferences.UserPreferencesRepository
import com.zettl.clockwork.domain.lesson.LessonRepository
import com.zettl.clockwork.domain.level.LevelRepository
import com.zettl.clockwork.ui.navigation.ClockworkNavHost
import com.zettl.clockwork.ui.theme.ClockworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferences = UserPreferencesRepository(applicationContext)
        val lessonRepository = LessonRepository()
        val levelRepository = LevelRepository()
        setContent {
            ClockworkTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ClockworkNavHost(
                        preferences = preferences,
                        lessonRepository = lessonRepository,
                        levelRepository = levelRepository
                    )
                }
            }
        }
    }
}
