package com.beran.bisnisplus.ui.screen.pembukuan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beran.bisnisplus.utils.Utils

@Composable
fun StockCard(
    stockName: String,
    stockUnitName: String?,
    stockUnitPrice: Long,
    modifier: Modifier = Modifier,
    stockQuantityText: Double = 0.0
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stockName, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "$stockQuantityText $stockUnitName",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
            Text(
                text = "X ${Utils.rupiahFormatter(stockUnitPrice)}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )
        }
    }

}