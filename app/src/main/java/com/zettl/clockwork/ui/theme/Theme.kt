package com.zettl.clockwork.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ClockworkPrimaryDark,
    secondary = ClockworkSecondaryDark,
    tertiary = ClockworkTertiaryDark,
    background = ClockworkBackgroundDark,
    surface = ClockworkSurfaceDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ClockworkPrimary,
    secondary = ClockworkSecondary,
    tertiary = ClockworkTertiary,
    background = ClockworkBackground,
    surface = ClockworkSurface,
    onPrimary = ClockworkOnPrimary,
    onSecondary = ClockworkOnSecondary,
    onTertiary = ClockworkOnBackground,
    onBackground = ClockworkOnBackground,
    onSurface = ClockworkOnSurface
)

@Composable
fun ClockworkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}