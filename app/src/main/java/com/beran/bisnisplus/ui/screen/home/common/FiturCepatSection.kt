package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.component.FiturCepatCard

@Composable
fun FiturCepatSection(
    onNavigateToCreateBook: () -> Unit,
    onNavigateToStatistic: () -> Unit,
    onNavigateToDebt: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Fitur Cepat",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            FiturCepatCard(
                title = "Buku",
                icon = Icons.Outlined.StickyNote2,
                action = onNavigateToCreateBook
            )
            FiturCepatCard(
                title = "Report",
                icon = Icons.Outlined.InsertChart,
                action = onNavigateToStatistic
            )
            FiturCepatCard(
                title = "Utang",
                icon = Icons.Outlined.CreditCard,
                action = onNavigateToDebt
            )
            FiturCepatCard(
                title = "Piutang",
                icon = Icons.Outlined.ReceiptLong,
                action = onNavigateToDebt
            )
        }
    }
}
