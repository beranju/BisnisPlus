package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.core.domain.model.BookModel

@Composable
fun PembukuanSection(
    listBook: List<BookModel>,
    deleteBook: (String) -> Unit,
    onNavigateToEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
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
            if (listBook.isEmpty()) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(250.dp)
                            .height(85.dp)
                    ) {
                        Text(
                            text = "Belum ada data terbaru",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            items(listBook, key = { it.bookId.orEmpty() }) { item ->
                PembukuanCard(
                    book = item,
                    deleteBook = deleteBook,
                    onNavigateToEdit = onNavigateToEdit
                )
            }
        }
    }
}