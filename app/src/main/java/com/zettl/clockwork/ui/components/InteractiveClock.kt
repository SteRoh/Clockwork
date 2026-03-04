package com.zettl.clockwork.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Interaktive Uhr mit Stunden- und Minutenzeiger.
 * Beide Zeiger sind per Drag einstellbar (große Touch-Bereiche für Kinder).
 * Bei enabled = false nur Anzeige (z. B. für „Welche Uhrzeit?“).
 *
 * @param hour Stunde 0–11 (12 = 0)
 * @param minute Minute 0–59
 * @param onTimeChange Callback bei Änderung (hour, minute)
 * @param enabled Zeiger per Touch einstellbar (false = nur Anzeige)
 */
@Composable
fun InteractiveClock(
    hour: Int,
    minute: Int,
    onTimeChange: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var dragHour by remember { mutableFloatStateOf(-1f) }
    var dragMinute by remember { mutableFloatStateOf(-1f) }
    var currentHour by remember { mutableIntStateOf(hour) }
    var currentMinute by remember { mutableIntStateOf(minute) }
    LaunchedEffect(hour, minute) {
        currentHour = hour
        currentMinute = minute
    }

    val sizeDp = 280.dp
    val sizePx = with(androidx.compose.ui.platform.LocalDensity.current) { sizeDp.toPx() }
    val centerX = sizePx / 2f
    val centerY = sizePx / 2f
    val radius = sizePx / 2f - 16f

    Canvas(
        modifier = modifier
            .size(sizeDp)
            .then(
                if (enabled) Modifier.pointerInput(Unit) {
                    detectDragGestures(
                    onDragStart = { offset ->
                        val dx = offset.x - centerX
                        val dy = offset.y - centerY
                        val dist = sqrt(dx * dx + dy * dy)
                        val angle = atan2(dy, dx)
                        val deg = Math.toDegrees(angle.toDouble()).toFloat() + 90f
                        val normalizedDeg = (deg + 360f) % 360f
                        if (dist < radius * 0.5f) {
                            dragHour = normalizedDeg
                            dragMinute = -1f
                        } else {
                            dragMinute = normalizedDeg
                            dragHour = -1f
                        }
                    },
                    onDrag = { change, _ ->
                        val dx = change.position.x - centerX
                        val dy = change.position.y - centerY
                        val angle = atan2(dy, dx)
                        var deg = Math.toDegrees(angle.toDouble()).toFloat() + 90f
                        if (deg < 0f) deg += 360f
                        if (dragHour >= 0f) {
                            val h = (deg / 30f).toInt().coerceIn(0, 11)
                            currentHour = h
                            onTimeChange(h, currentMinute)
                        } else if (dragMinute >= 0f) {
                            val m = (deg / 6f).toInt().coerceIn(0, 59)
                            currentMinute = m
                            onTimeChange(currentHour, m)
                        }
                    },
                    onDragEnd = {
                        dragHour = -1f
                        dragMinute = -1f
                    }
                )
                } else Modifier
            )
    ) {
        // Kindgerechte Farben: freundlich und klar
        val faceColor = Color(0xFFFFF8E8)
        val rimColor = Color(0xFF4A90A4)
        val hourMarkColor = Color(0xFF2E6B7C)
        val minuteMarkColor = Color(0xFF7BC4A0)
        val minuteMarkColorLight = Color(0xFFB8E0CC)
        val hourHandColor = Color(0xFF2E6B7C)
        val minuteHandColor = Color(0xFFE8A030)
        val centerColor = Color(0xFF4A90A4)

        // Ziffernblatt füllen
        drawCircle(
            color = faceColor,
            radius = radius,
            center = Offset(centerX, centerY)
        )
        // Deutlicher Uhrrand (dick, farbig)
        drawCircle(
            color = rimColor,
            radius = radius,
            center = Offset(centerX, centerY),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f)
        )

        // 60 Minuten-Striche: jede Minute ein Strich, jede 5. Minute länger und dicker
        for (i in 0 until 60) {
            val angleRad = (i * 6 - 90) * Math.PI / 180
            val isFive = i % 5 == 0
            val innerR = if (isFive) radius - 36f else radius - 28f
            val outerR = radius
            drawLine(
                color = if (isFive) minuteMarkColor else minuteMarkColorLight,
                start = Offset(
                    (centerX + innerR * cos(angleRad)).toFloat(),
                    (centerY + innerR * sin(angleRad)).toFloat()
                ),
                end = Offset(
                    (centerX + outerR * cos(angleRad)).toFloat(),
                    (centerY + outerR * sin(angleRad)).toFloat()
                ),
                strokeWidth = if (isFive) 7f else 4f,
                cap = StrokeCap.Round
            )
        }

        // 12 Stunden-Striche: deutlich länger und dicker (Unterteilung der Uhr)
        for (i in 0 until 12) {
            val angleRad = (i * 30 - 90) * Math.PI / 180
            val innerR = radius - 28f
            val outerR = radius
            drawLine(
                color = hourMarkColor,
                start = Offset(
                    (centerX + innerR * cos(angleRad)).toFloat(),
                    (centerY + innerR * sin(angleRad)).toFloat()
                ),
                end = Offset(
                    (centerX + outerR * cos(angleRad)).toFloat(),
                    (centerY + outerR * sin(angleRad)).toFloat()
                ),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
        }

        // Zahlen 12, 3, 6, 9
        val textRadius = radius - 40f
        val textSizePx = 44f
        val textPaint = Paint().apply {
            color = 0xFF2E6B7C.toInt()
            textAlign = Paint.Align.CENTER
            this.textSize = textSizePx
            isAntiAlias = true
        }
        val fontMetrics = textPaint.fontMetrics
        val textOffsetY = (fontMetrics.ascent + fontMetrics.descent) / 2f
        val numbers = listOf("12" to 0, "3" to 90, "6" to 180, "9" to 270)
        numbers.forEach { (text, deg) ->
            val angleRad = (deg - 90) * Math.PI / 180
            val x = centerX + textRadius * cos(angleRad).toFloat()
            val y = centerY + textRadius * sin(angleRad).toFloat() - textOffsetY
            drawContext.canvas.nativeCanvas.apply {
                drawText(text, x, y, textPaint)
            }
        }

        val hourHandLen = radius * 0.50f
        val minuteHandLen = radius * 0.78f

        // Minutenzeiger (orange) – zuerst zeichnen, damit er unter dem Stundenzeiger liegt
        rotate(degrees = minute * 6f, pivot = Offset(centerX, centerY)) {
            drawLine(
                color = minuteHandColor,
                start = Offset(centerX, centerY),
                end = Offset(centerX, centerY - minuteHandLen),
                strokeWidth = 20f,
                cap = StrokeCap.Round
            )
        }
        // Stundenzeiger (blau)
        rotate(degrees = hour * 30f + minute / 2f, pivot = Offset(centerX, centerY)) {
            drawLine(
                color = hourHandColor,
                start = Offset(centerX, centerY),
                end = Offset(centerX, centerY - hourHandLen),
                strokeWidth = 26f,
                cap = StrokeCap.Round
            )
        }

        // Mittelpunkt (deutlicher Punkt)
        drawCircle(
            color = centerColor,
            radius = 14f,
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = faceColor,
            radius = 6f,
            center = Offset(centerX, centerY)
        )
    }
}
