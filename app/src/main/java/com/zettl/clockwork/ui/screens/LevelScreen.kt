package com.zettl.clockwork.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zettl.clockwork.R
import com.zettl.clockwork.domain.level.LevelDefinition
import com.zettl.clockwork.domain.level.LevelRepository
import com.zettl.clockwork.domain.level.LevelTaskType
import com.zettl.clockwork.ui.components.BackIcon
import com.zettl.clockwork.ui.components.InteractiveClock

private const val MINUTE_TOLERANCE = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelScreen(
    lessonTitle: String,
    lessonId: String,
    levelIndex: Int,
    levelRepository: LevelRepository,
    onLevelComplete: (earnedTalers: Int) -> Unit,
    onBack: () -> Unit
) {
    val level = levelRepository.getLevel(lessonId, levelIndex)
    val context = LocalContext.current

    var hour by remember(level) {
        mutableIntStateOf(if (level?.taskType == LevelTaskType.SET_CLOCK || level?.taskType == LevelTaskType.EVERYDAY) 0 else level?.targetHour ?: 0)
    }
    var minute by remember(level) {
        mutableIntStateOf(if (level?.taskType == LevelTaskType.SET_CLOCK || level?.taskType == LevelTaskType.EVERYDAY) 0 else level?.targetMinute ?: 0)
    }
    var checked by remember { mutableStateOf(false) }
    var correct by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(lessonTitle) },
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

        if (level == null) {
            Text(
                text = "Level nicht gefunden.",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            return@Column
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val taskText = when (level.taskType) {
                LevelTaskType.SET_CLOCK, LevelTaskType.EVERYDAY -> {
                    val arg = level.taskArg ?: ""
                    context.getString(R.string.task_set_clock, arg)
                }
                LevelTaskType.WHICH_TIME -> context.getString(R.string.task_which_time)
            }
            Text(
                text = taskText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            when (level.taskType) {
                LevelTaskType.SET_CLOCK, LevelTaskType.EVERYDAY -> {
                    InteractiveClock(
                        hour = hour,
                        minute = minute,
                        onTimeChange = { h, m ->
                            if (!checked) {
                                hour = h
                                minute = m
                            }
                        }
                    )
                }
                LevelTaskType.WHICH_TIME -> {
                    InteractiveClock(
                        hour = level.targetHour,
                        minute = level.targetMinute,
                        onTimeChange = { _, _ -> },
                        enabled = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    WhichTimeOptions(
                        targetHour = level.targetHour,
                        targetMinute = level.targetMinute,
                        onCorrect = {
                            correct = true
                            checked = true
                        },
                        onWrong = {
                            correct = false
                            checked = true
                        },
                        checked = checked
                    )
                }
            }

            if (level.taskType == LevelTaskType.SET_CLOCK || level.taskType == LevelTaskType.EVERYDAY) {
                Spacer(modifier = Modifier.height(24.dp))
                if (!checked) {
                    Button(
                        onClick = {
                            val hOk = hour == level.targetHour
                            val mOk = kotlin.math.abs(minute - level.targetMinute) <= MINUTE_TOLERANCE ||
                                kotlin.math.abs(minute - level.targetMinute) >= 60 - MINUTE_TOLERANCE
                            correct = hOk && mOk
                            checked = true
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(stringResource(R.string.check))
                    }
                } else {
                    if (correct) {
                        Text(
                            text = stringResource(R.string.feedback_correct),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.feedback_talers_earned, level.talersReward),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onLevelComplete(level.talersReward) },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text(stringResource(R.string.next))
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.feedback_try_again),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { checked = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text(stringResource(R.string.check))
                        }
                    }
                }
            } else if (level.taskType == LevelTaskType.WHICH_TIME && checked) {
                if (correct) {
                    Text(
                        text = stringResource(R.string.feedback_correct),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.feedback_talers_earned, level.talersReward),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onLevelComplete(level.talersReward) },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(stringResource(R.string.next))
                    }
                } else {
                    Text(
                        text = stringResource(R.string.feedback_try_again),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun WhichTimeOptions(
    targetHour: Int,
    targetMinute: Int,
    onCorrect: () -> Unit,
    onWrong: () -> Unit,
    checked: Boolean
) {
    val options = remember(targetHour, targetMinute) {
        val correct = formatTimeOption(targetHour, targetMinute)
        val wrong1 = formatTimeOption((targetHour + 1) % 12, targetMinute)
        val wrong2 = formatTimeOption(targetHour, (targetMinute + 15) % 60)
        val wrong3 = formatTimeOption((targetHour + 2) % 12, (targetMinute + 30) % 60)
        listOf(correct, wrong1, wrong2, wrong3).shuffled()
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val isCorrect = option == formatTimeOption(targetHour, targetMinute)
            Button(
                onClick = {
                    if (checked) return@Button
                    if (isCorrect) onCorrect() else onWrong()
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text(option)
            }
        }
    }
}

private fun formatTimeOption(hour: Int, minute: Int): String {
    val h = if (hour == 0) 12 else hour
    return when {
        minute == 0 -> "$h Uhr"
        minute == 30 -> "halb ${nextHour(h)}"
        minute == 15 -> "Viertel nach $h"
        minute == 45 -> "Viertel vor ${nextHour(h)}"
        else -> "$h:$minute"
    }
}

private fun nextHour(h: Int): Int = if (h == 12) 1 else h + 1
