package com.beran.core.common

import com.beran.core.domain.model.BookModel

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