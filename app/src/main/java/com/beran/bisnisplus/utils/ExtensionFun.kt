package com.beran.bisnisplus.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import java.time.LocalDate
import java.time.ZoneId


/**
 * this utils used to validate email input...
 * ...use the regular expression pattern...
 * ...that matches the valid email
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
    return emailRegex.matches(this)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toEpochMilli(): Long {
    val zoneId = ZoneId.systemDefault()
    val zoneLocalDate = this.atStartOfDay(zoneId)
    val instantDate = zoneLocalDate.toInstant()
    return instantDate.toEpochMilli()
}


/**
 * fungsi ekstensi untuk membuat efek shimmer pada composeable
 */
fun Modifier.shimmer(
    durationMillis: Int = 1000,
    color: Color = Color.LightGray
) = composed {
    var size by remember { mutableStateOf(Size.Zero) }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis)
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.9f),
                color.copy(alpha = 0.3f),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size.toSize()
    }
}