package com.beran.bisnisplus.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Green80,
    primaryContainer = Green30,
    onPrimaryContainer = Green90,
    secondary = GreenGrey80,
    secondaryContainer = GreenGrey30,
    onSecondaryContainer = GreenGrey90,
    tertiary = Blue80,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = Grey10,
    surface = Grey10,
    onPrimary = Green20,
    onSecondary = GreenGrey20,
    onTertiary = Blue20,
    onBackground = Grey95,
    onSurface = Grey95,
    outline = Grey60,
    surfaceVariant = Grey35,
    onSurfaceVariant = Grey80
)

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    primaryContainer = Green90,
    onPrimaryContainer = Green10,
    secondary = GreenGrey40,
    secondaryContainer = GreenGrey90,
    onSecondaryContainer = GreenGrey10,
    tertiary = Blue40,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = Grey99,
    onBackground = Grey10,
    surface = Grey99,
    onSurface = Grey10,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    outline = Grey50,
    surfaceVariant = Grey90,
    onSurfaceVariant = Grey30

    )

@Composable
fun BisnisPlusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // ** disable dynamic color for all devices
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        // ** get current window by tapping into the activity
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an Activity - Unable to get window reference")
        SideEffect {
            currentWindow.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars =
                darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}