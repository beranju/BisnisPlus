package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.bisnisplus.utils.shimmer
import com.beran.core.domain.model.BookModel


@Composable
fun PembukuanSectionShimmer(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.shimmer()) {
        Text(
            text = "Terbaru",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .height(150.dp)
                .padding(4.dp)
        ) {
            items(3) {
                PembukuanCard(
                    book = BookModel(),
                    onNavigateToEdit = {},
                    deleteBook = {},
                    modifier = Modifier.shimmer(),
                )
            }
        }
    }
}