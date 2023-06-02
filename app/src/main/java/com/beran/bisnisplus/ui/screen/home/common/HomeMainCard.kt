package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.core.domain.model.UserModel
import com.github.mikephil.charting.data.PieEntry

@Composable
fun HomeMainCard(
    dataEntry: List<PieEntry>,
    user: UserModel?,
    onNavigateToProfile: () -> Unit,
    onNavigateToStatistic: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                hoveredElevation = 0.dp
            ), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ), modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                HomeProfileCard(
                    user = user,
                    onNavigateToProfile = onNavigateToProfile
                )
                Divider()
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Pemasukan", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Semua pemasukan",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    TextButton(onClick = {onNavigateToStatistic()}) {
                        Text(text = "detail", style = MaterialTheme.typography.bodySmall)
                        Icon(
                            imageVector = Icons.Outlined.NavigateNext, contentDescription = null
                        )
                    }
                }
                HomeChartSection(dataEntry)
            }
        }
    }
}
