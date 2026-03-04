package com.zettl.clockwork.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val TALERS = intPreferencesKey("talers")
        val UNLOCKED_LESSON_LEVELS = stringSetPreferencesKey("unlocked_lesson_levels")
        val DIFFICULTY = intPreferencesKey("difficulty")
    }

    val talers: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.TALERS] ?: 0
    }

    val unlockedLessonLevels: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[Keys.UNLOCKED_LESSON_LEVELS] ?: setOf("1-0")
    }

    val difficulty: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.DIFFICULTY] ?: 0
    }

    suspend fun addTalers(amount: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.TALERS] ?: 0
            prefs[Keys.TALERS] = (current + amount).coerceAtLeast(0)
        }
    }

    suspend fun unlockLevel(lessonId: String, levelIndex: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.UNLOCKED_LESSON_LEVELS] ?: setOf("1-0")
            prefs[Keys.UNLOCKED_LESSON_LEVELS] = current + "$lessonId-$levelIndex"
        }
    }

    suspend fun setDifficulty(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DIFFICULTY] = value.coerceIn(0, 2)
        }
    }

    fun isLevelUnlocked(lessonId: String, levelIndex: Int): Flow<Boolean> =
        unlockedLessonLevels.map { set -> "$lessonId-$levelIndex" in set }
}
