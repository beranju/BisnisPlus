package com.beran.bisnisplus.ui.screen.pembukuan.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.utils.Utils
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.model.StockItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksCard(
    book: BookModel,
    deleteBook: (String) -> Unit,
    onNavigateToEdit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val amount = Utils.rupiahFormatter(book.amount.toLong())
    var openDialog by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (book.mitra?.isNotEmpty() == true) {
                    Text(text = book.mitra.orEmpty(), style = MaterialTheme.typography.bodyMedium)
                    Text(text = book.category.orEmpty(), style = MaterialTheme.typography.bodySmall)
                } else {
                    Text(
                        text = book.category.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium
                    )
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
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Navigate to detail",
                    modifier = Modifier.clickable {
                        openDialog = true
                    }
                )
            }
        }
        Box(modifier = Modifier.padding(4.dp)) {
            Divider(Modifier.height(1.dp))
        }
    }

    if (openDialog) {
        AlertDialog(onDismissRequest = { openDialog = false }) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    DetailFieldDialog(
                        "Jenis", book.category.orEmpty()
                    )
                    DetailFieldDialog(
                        "Mitra", book.mitra ?: "Unknown"
                    )
                    DetailFieldDialog(
                        "Jumlah", Utils.rupiahFormatter(book.amount.toLong())
                    )
                    DetailFieldDialog(
                        "Status", book.state
                    )
                    if (book.note?.isNotEmpty() == true) {
                        DetailFieldDialog(labelText = "Catatan", value = book.note.orEmpty())
                    }
                    book.listStock?.stocks?.let {
                        Text(text = "Stok", style = MaterialTheme.typography.bodySmall)
                        ListStockDialog(
                            it
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                openDialog = false
                                deleteBook(book.bookId.orEmpty())
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                openDialog = false
                                onNavigateToEdit(book.bookId.toString())
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Edit",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { openDialog = false }) {
                            Text(text = "Ok", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListStockDialog(
    list: List<StockItem>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(list, key = { it.stockId.orEmpty() }) { stock ->
                StockCard(
                    stockName = stock.stockName.orEmpty(),
                    stockUnitName = stock.unitName,
                    stockUnitPrice = stock.unitPrice.toLong(),
                    stockQuantityText = stock.unit,
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }

}

@Composable
fun DetailFieldDialog(
    labelText: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(bottom = 16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = labelText, style = MaterialTheme.typography.bodySmall)
            Text(text = value, style = MaterialTheme.typography.labelMedium)
        }
    }

}
