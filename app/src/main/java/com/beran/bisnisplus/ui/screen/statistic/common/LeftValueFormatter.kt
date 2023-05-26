package com.beran.bisnisplus.ui.screen.statistic.common

import com.beran.bisnisplus.utils.Utils
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class LeftValueFormatter : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return "${Utils.rupiahFormatter(value.toLong() / 1_000_000L)} Jt"
    }

}
