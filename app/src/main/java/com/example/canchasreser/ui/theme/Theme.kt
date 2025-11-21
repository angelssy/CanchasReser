package com.example.canchasreser.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = DarkText,
    onSurface = DarkText
)

private val DarkColors = darkColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    background = DarkText,
    surface = DarkText,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White
)

@Composable
fun CanchasTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}