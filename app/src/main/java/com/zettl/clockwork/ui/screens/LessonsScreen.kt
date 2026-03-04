package com.zettl.clockwork.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zettl.clockwork.R
import com.zettl.clockwork.domain.lesson.LessonRepository
import com.zettl.clockwork.ui.components.BackIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    lessonRepository: LessonRepository,
    onLessonClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val lessons = lessonRepository.getAllLessons()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.lessons_title)) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    BackIcon()
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lessons) { lesson ->
                val titleRes = when (lesson.id) {
                    "1" -> R.string.lesson_1
                    "2" -> R.string.lesson_2
                    "3" -> R.string.lesson_3
                    "4" -> R.string.lesson_4
                    "5" -> R.string.lesson_5
                    "6" -> R.string.lesson_6
                    "7" -> R.string.lesson_7
                    else -> R.string.lesson_1
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLessonClick(lesson.id) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = stringResource(titleRes),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }
        }
    }
}
