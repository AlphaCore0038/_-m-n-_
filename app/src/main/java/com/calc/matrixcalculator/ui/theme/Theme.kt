package com.calc.matrixcalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Black,
    onPrimary = White,
    background = White,
    surface = White,
    onBackground = Black,
    onSurface = Black,
)

private val DarkColors = darkColorScheme(
    primary = White,
    onPrimary = Black,
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onBackground = White,
    onSurface = White,
)

@Composable
fun CalCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
