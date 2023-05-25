package com.beran.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

object Utils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToDate(epoch: Long): String{
        val instant = Instant.ofEpochMilli(epoch)
        val localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val dayOfWeekName = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val monthName = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = localDate.dayOfMonth
        val year = localDate.year
        return "$dayOfWeekName, $dayOfMonth $monthName $year"
    }
}