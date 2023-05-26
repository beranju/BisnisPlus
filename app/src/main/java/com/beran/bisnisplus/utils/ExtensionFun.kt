package com.beran.bisnisplus.utils

import android.os.Build
import androidx.annotation.RequiresApi
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