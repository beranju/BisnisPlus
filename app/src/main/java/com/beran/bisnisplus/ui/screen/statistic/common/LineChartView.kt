package com.beran.bisnisplus.ui.screen.statistic.common

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.compose.ui.graphics.toArgb
import com.beran.bisnisplus.ui.theme.Green30
import com.beran.bisnisplus.ui.theme.Green40
import com.beran.bisnisplus.ui.theme.Red30
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr) {

    init {
        setupChart()
    }

    private fun setupChart() {
        setNoDataText("No data available")
        description.isEnabled = false
        setDrawGridBackground(false)
        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        legend.isEnabled = false
        // ** custom x-axis
        val xAxis = this.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
//        xAxis.valueFormatter = DateValueFormatter()

        // ** custom y-axis
        val yAxis = this.axisLeft
        yAxis.granularity = 1f
        yAxis.valueFormatter = LeftValueFormatter()

        axisRight.isEnabled = false
    }

    fun setData(data: List<Entry>, isIncome: Boolean = true) {
        val dataSet = LineDataSet(data, "Data Set")
        dataSet.color = if (isIncome) Green40.toArgb() else Red30.toArgb()
        dataSet.lineWidth = 3f
        dataSet.setDrawCircles(false)
        val lineData = LineData(dataSet)

        setData(lineData)
        invalidate()
    }
}