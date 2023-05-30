package com.beran.bisnisplus.ui.screen.pembukuan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.ui.screen.pembukuan.component.BooksCard
import com.beran.bisnisplus.utils.Utils
import com.beran.bisnisplus.utils.Utils.convertToDate
import com.beran.core.domain.model.BookModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabContentScreen(
    listBook: List<Pair<Long?, List<BookModel>>>,
    onNavigateToEdit: (String) -> Unit,
    deleteBook: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var size by remember {
        mutableStateOf(Size.Zero)
    }
    Box(modifier = modifier
        .fillMaxSize()
        .padding(top = 24.dp)
        .onGloballyPositioned { coordinate ->
            size = coordinate.size.toSize()
        }) {
        ListBookSection(
            size = size,
            listBook = listBook,
            onNavigateToEdit = onNavigateToEdit,
            deleteBook = deleteBook
        )

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListBookSection(
    listBook: List<Pair<Long?, List<BookModel>>>,
    onNavigateToEdit: (String) -> Unit,
    deleteBook: (String) -> Unit,
    size: Size
) {
    val localDensity = LocalDensity.current
    LazyColumn(
        modifier = Modifier.height(with(localDensity) { size.height.toDp() })
    ) {
        listBook.forEach { (timeStamp, list) ->
            val totalAmount = list.sumOf { it.amount }.toLong()
            val totalAmountFormatted = Utils.rupiahFormatter(totalAmount)
            stickyHeader { DateSection(timeStamp) }
            items(list, key = { item -> item.bookId.orEmpty() }) { book ->
                BooksCard(
                    book = book,
                    onNavigateToEdit = onNavigateToEdit,
                    deleteBook = deleteBook
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Total", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = totalAmountFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun DateSection(
    date: Long?, modifier: Modifier = Modifier
) {
    val dateString = if (date != null) convertToDate(date) else return
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(text = dateString, style = MaterialTheme.typography.bodySmall)
    }
}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun TabContentScreenPrev() {
//    BisnisPlusTheme {
//        TabContentScreen(emptyList(), onNavigateToEdit = {})
//    }
//}