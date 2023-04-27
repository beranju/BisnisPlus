package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun PembayaranScreen(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp))
    {
        Text(text = "PembayaranScreen")
    }
}

@Preview(showBackground = true)
@Composable
fun PembayaranScreenPrev() {
    BisnisPlusTheme {
        PembayaranScreen()
    }
}