package com.beran.bisnisplus.ui.screen.pembukuan.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.utils.Utils
import com.beran.core.domain.model.BookModel

@Composable
fun BooksCard(
    book: BookModel, modifier: Modifier = Modifier
) {
    val amount = Utils.rupiahFormatter(book.amount.toLong())

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (book.mitra?.isNotEmpty() == true) {
                    Text(text = book.mitra.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                    Text(text = book.category.orEmpty(), style = MaterialTheme.typography.bodySmall)
                }else{
                    Text(text = book.category.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = amount,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.NavigateNext,
                    contentDescription = "Navigate to detail"
                )
            }
        }
        Divider(Modifier.height(1.dp))
    }
}
