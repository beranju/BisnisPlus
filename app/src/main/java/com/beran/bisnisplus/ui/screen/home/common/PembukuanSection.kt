package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.bisnisplus.utils.Utils
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
            items(listBook, key = { it.bookId.orEmpty() }) { item ->
                PembukuanCard(book = item, deleteBook = deleteBook, onNavigateToEdit = onNavigateToEdit)
            }
        }
    }
}