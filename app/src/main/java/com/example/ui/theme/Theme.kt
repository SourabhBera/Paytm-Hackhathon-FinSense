package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = FinSenseCyan,
    onPrimary = Color.White,
    primaryContainer = FinSenseCyan,
    onPrimaryContainer = Color.White,
    secondary = FinSenseNavy,
    onSecondary = Color.White,
    secondaryContainer = FinSenseSurfaceContainer,
    onSecondaryContainer = FinSenseNavy,
    tertiary = StatusWarning,
    onTertiary = Color.White,
    background = FinSenseCanvas,
    onBackground = TextPrimary,
    surface = FinSenseSurface,
    onSurface = TextPrimary,
    surfaceVariant = FinSenseSurfaceLow,
    onSurfaceVariant = TextSecondary,
    outline = BorderOutline,
    outlineVariant = BorderHairline,
    error = StatusError,
    onError = Color.White,
    errorContainer = StatusErrorBg,
    onErrorContainer = StatusError,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Forced pure light mode across all devices
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    // Pure, crisp Light Mode as specified by the user
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
