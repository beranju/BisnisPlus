package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.data.PieEntry


@Composable
fun HomeChartSection(
    data: List<PieEntry>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AndroidView(factory = {
            PieChartView(context = it).apply {
                setData(data)
            }
        }, modifier = Modifier.size(180.dp))
    }
}