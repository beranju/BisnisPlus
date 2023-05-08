package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beran.bisnisplus.data.dummyStocks
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun CustomStockField(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .heightIn(min = 100.dp, max = 300.dp)
            .padding(top = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Stok",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(3f)
            ) {
                LazyColumn {
                    items(dummyStocks, key = { item -> item.stockId }) { stock ->
                        StockCard(
                            stockName = stock.stockName,
                            stockUnit = stock.unit,
                            stockQuantity = stock.quantity,
                            stockUnitPrice = stock.unitPrice,
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Total", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "150000",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Button(
                    onClick = {}, modifier = Modifier
                        .height(30.dp)
                        .align(Alignment.End)
                ) {
                    Text(
                        text = "Tambah +",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                    )
                }
            }
        }

    }
}

@Composable
private fun StockCard(
    stockName: String,
    stockUnit: String?,
    stockUnitPrice: Long,
    modifier: Modifier = Modifier,
    stockQuantity: Double? = 0.0
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
                    text = stockName,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                )
                Text(
                    text = "$stockQuantity $stockUnit",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                )
            }
            Text(
                text = "X $stockUnitPrice",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CustomStockFieldPrev() {
    BisnisPlusTheme {
        CustomStockField()
    }
}