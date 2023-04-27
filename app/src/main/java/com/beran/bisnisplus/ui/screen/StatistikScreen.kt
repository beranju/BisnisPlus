package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun StatistikScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    )
    {
        ExportDataSection()
    }
}

@Composable
fun ExportDataSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
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
        StatistikScreen()
    }
}