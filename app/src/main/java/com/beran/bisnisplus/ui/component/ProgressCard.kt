package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun ProgressCard(
    value: String,
    subTitle: String,
    upTrend: Boolean
) {
    Card(
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = if (upTrend) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (upTrend) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Row {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (upTrend) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                )
                Icon(
                    imageVector = if (upTrend) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                    contentDescription = null,
                    tint = if (upTrend) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                )
            }
            Text(
                text = subTitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (upTrend) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressCardPrev() {
    BisnisPlusTheme {
        ProgressCard("20%", "Hutang", true)
    }

}