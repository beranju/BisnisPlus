package com.beran.bisnisplus.ui.screen.home.common

import android.content.Context
import android.util.AttributeSet
import androidx.compose.ui.graphics.toArgb
import com.beran.bisnisplus.ui.theme.BlueGreen50
import com.beran.bisnisplus.ui.theme.Red50
import com.beran.bisnisplus.ui.theme.Yellow50
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PieChart(context, attrs, defStyleAttr) {
    init {
        setupChart()
    }

    private fun setupChart() {
        setNoDataText("No data Avaliable")
        setUsePercentValues(true)
        description.isEnabled = false

    }

    fun setData(data: List<PieEntry>) {
        val dataSet = PieDataSet(data, "")
        dataSet.setColors(Red50.toArgb(), Yellow50.toArgb(), BlueGreen50.toArgb())
        val pieData = PieData(dataSet)
        setData(pieData)
        invalidate()
    }
}