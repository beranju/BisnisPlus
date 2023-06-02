package com.beran.bisnisplus.ui.screen.pembukuan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.utils.shimmer

@Composable
fun ProfitLoseCardShimmer(
    modifier: Modifier = Modifier,
    incomeAmount: String = "",
    expenseAmount: String = "",
    difference: String = "",
    stateText: String = ""
) {
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp, horizontal = 8.dp)
                .shimmer()
        ) {
            Row {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = incomeAmount,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.shimmer()
                    )
                    Text(
                        text = "Pemasukan",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.shimmer()
                    )
                }
                Divider(
                    Modifier
                        .height(40.dp)
                        .width(1.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = expenseAmount,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.shimmer()
                    )
                    Text(
                        text = "Pengeluaran",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.shimmer()
                    )
                }
            }
            Divider()
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = stateText,
                        style = MaterialTheme.typography.bodySmall,
                        color = when (stateText) {
                            "Untung" -> MaterialTheme.colorScheme.primary
                            "Rugi" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onBackground
                        },
                        modifier = Modifier.shimmer()
                    )
                    Text(
                        text = difference, style = MaterialTheme.typography.labelMedium,
                        color = when (stateText) {
                            "Untung" -> MaterialTheme.colorScheme.primary
                            "Rugi" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onBackground
                        },
                        modifier = Modifier.shimmer()
                    )
                }
            }
        }
    }

}
