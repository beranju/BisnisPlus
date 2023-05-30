package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beran.bisnisplus.constant.BookTypes
import com.beran.bisnisplus.ui.screen.pembukuan.component.DetailFieldDialog
import com.beran.bisnisplus.ui.screen.pembukuan.component.ListStockDialog
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import com.beran.core.domain.model.BookModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PembukuanCard(
    book: BookModel,
    deleteBook: (String) -> Unit,
    onNavigateToEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 250.dp,
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(
            modifier = modifier
                .width(width)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = book.type.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = 10.dp, topEnd = 10.dp))
                        .background(if (book.type == BookTypes.Pemasukan.string) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer)
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                )
            }
            Text(
                text = book.category.orEmpty(),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = Utils.rupiahFormatter(book.amount.toLong()),
                style = MaterialTheme.typography.titleSmall,
                color = if (book.type == BookTypes.Pemasukan.string) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Text(
                text = Utils.convertToDate(book.createdAt ?: 0),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
            )
            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "Navigate to detail",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { openDialog = true }
            )
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

@Preview(showBackground = true)
@Composable
fun PembukuanCardPrev() {
    BisnisPlusTheme {
        PembukuanCard(BookModel(("")), onNavigateToEdit = {}, deleteBook = {})
    }
}