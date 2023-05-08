package com.beran.bisnisplus.ui.screen.pembukuan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.data.PembukuanItem
import com.beran.bisnisplus.data.dummyItem
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import androidx.compose.foundation.layout.Box as Box1

@Composable
fun TabContentScreen(
    title: String, modifier: Modifier = Modifier
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
        ListBookSection(size = size)
    }
}

@Composable
private fun BooksCard(
    book: PembukuanItem, modifier: Modifier = Modifier
) {
    val amount = Utils.rupiahFormatter(book.amount)

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = book.mitraName, style = MaterialTheme.typography.bodyMedium)
                Text(text = book.category, style = MaterialTheme.typography.bodySmall)
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


@Composable
private fun ListBookSection(
    size: Size
) {
    val localDensity = LocalDensity.current
    LazyColumn(
        modifier = Modifier.height(with(localDensity) { size.height.toDp() })
    ) {
        item {
            DateSection(date = "Senin, 27 April 2023")
        }
        items(dummyItem, key = { item -> item.id }) { book ->
            BooksCard(book = book)
        }
        item {
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Total", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Rp15.000.000,00",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun DateSection(
    date: String, modifier: Modifier = Modifier
) {
    Box1(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(text = date, style = MaterialTheme.typography.bodySmall)
    }
}

@Preview(showBackground = true)
@Composable
fun TabContentScreenPrev() {
    BisnisPlusTheme {
        TabContentScreen(title = "Pemasukan")
    }
}