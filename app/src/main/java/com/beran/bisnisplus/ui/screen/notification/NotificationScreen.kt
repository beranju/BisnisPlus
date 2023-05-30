package com.beran.bisnisplus.ui.screen.notification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        Text(text = "Notification screen", style = MaterialTheme.typography.labelSmall)

    }
}

@Preview
@Composable
fun NotificationScreenPrev() {
    BisnisPlusTheme {
        NotificationScreen()
    }
}