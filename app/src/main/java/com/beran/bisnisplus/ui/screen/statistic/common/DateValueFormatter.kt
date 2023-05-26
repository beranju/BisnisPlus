package com.beran.bisnisplus.ui.screen.statistic.common

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateValueFormatter(private val startDate: Long) :
    ValueFormatter() {
    private val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val daysToAdd = value.toLong()
        val calendar = Calendar.getInstance()
        calendar.time = Date(startDate)
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd.toInt())
        return dateFormatter.format(calendar.time)
    }
}
