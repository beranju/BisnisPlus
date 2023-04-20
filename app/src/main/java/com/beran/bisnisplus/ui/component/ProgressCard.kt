package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
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
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Row {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                Icon(
                    imageVector = if (upTrend) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            Text(
                text = subTitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer
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