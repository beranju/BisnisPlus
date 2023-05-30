package com.beran.core.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

/**
 * this extension used to transform data class into map
 */
fun <T : Any> T.asMap(): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()
    for (field in this::class.java.declaredFields) {
        field.isAccessible = true
        val name = field.name
        val value = field.get(this)
        map[name] = value
    }
    return map
}