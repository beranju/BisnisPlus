package com.beran.bisnisplus.ui.screen

import android.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.beran.bisnisplus.ui.component.EmptyView
import com.beran.bisnisplus.ui.component.ErrorView
import com.beran.bisnisplus.ui.screen.statistic.StatisticState
import com.beran.bisnisplus.ui.screen.statistic.common.DateValueFormatter
import com.beran.bisnisplus.ui.screen.statistic.common.LineChartView
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.github.mikephil.charting.data.Entry

private const val width = 350.0
private const val height = 400.0

@Composable
fun StatistikScreen(
    state: StatisticState,
    fetchData: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
    {
        ExportDataSection()
        if (state.loading) {
            fetchData()
            CircularProgressIndicator()
        } else if (state.error?.isNotEmpty() == true) {
            ErrorView(errorText = state.error, modifier = Modifier.fillMaxSize())
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                StatisticSection(
                    title = "Statistik Pemasukan",
                    chartData = state.listIncomeDate,
                    startDate = state.incomeStartDate ?: 0
                )
                StatisticSection(
                    title = "Statistik Pengeluaran",
                    chartData = state.listExpenseData,
                    startDate = state.expenseStartDate ?: 0,
                    isIncome = false
                )
            }
        }
    }
}

@Composable
fun StatisticSection(
    title: String,
    chartData: List<Entry>,
    startDate: Long,
    modifier: Modifier = Modifier,
    isIncome: Boolean = true
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Outlined.NavigateNext,
                    contentDescription = "statistic in landscape"
                )
            }
        }
        if (chartData.isEmpty()) {
            EmptyView(hintText = "Data Kosong")
        }else if(chartData.size < 3){
            EmptyView(hintText = "Data Tidak Cukup")
        } else {
            ChartSection(
                chartData = chartData,
                startDate = startDate,
                isIncome = isIncome
            )
        }
    }
}

@Composable
fun ChartSection(
    startDate: Long,
    chartData: List<Entry>,
    modifier: Modifier = Modifier,
    isIncome: Boolean = true
) {
    AndroidView(
        factory = { context ->
            LineChartView(context).apply {
                setData(chartData, isIncome)
                setVisibleXRangeMaximum(9f)
                val xAxis = this.xAxis
                xAxis.valueFormatter = DateValueFormatter(startDate)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    )

}

@Composable
fun ExportDataSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = "Cetak pembukuan kamu manjadi  file excel",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = { }) {
            Text(text = "Export")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatistikScreenPrev() {
    BisnisPlusTheme {
        StatistikScreen(state = StatisticState(), fetchData = {})
    }
}