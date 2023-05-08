package com.beran.bisnisplus.ui.screen.pembukuan.component

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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.constant.BookTypes
import com.beran.bisnisplus.constant.TimeRange
import com.beran.bisnisplus.data.dummyItem
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.component.CustomDropDown
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.ui.theme.Green85
import com.beran.bisnisplus.ui.theme.Red40
import com.beran.bisnisplus.utils.Utils

@Composable
fun FinancialStatementScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedValue by remember {
        mutableStateOf(TimeRange.Today.range)
    }

    val options: List<String> = TimeRange.values().toList().map { it.range }

    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Laporan Keuangan",
            onLeadingClick = onNavigateBack, leadingIcon = Icons.Outlined.NavigateBefore
        )
    }) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                CustomDropDown(
                    options = options,
                    selectedValue = selectedValue,
                    onChangeValue = { newValue -> selectedValue = newValue },
                    label = "Tentukan rentang waktu"
                )
                Spacer(modifier = Modifier.height(24.dp))
                CardIncomeOutcome()
                ListBookSection()
            }
            Button(
                onClick = {}, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(text = "Unduh Laporan", color = MaterialTheme.colorScheme.background, style = MaterialTheme.typography.labelMedium)
            }
        }

    }

}

@Composable
private fun ListBookSection(
    modifier: Modifier = Modifier
) {
    var size by remember {
        mutableStateOf(Size.Zero)
    }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinate ->
                size = coordinate.size.toSize()
            }
    ) {
        LazyColumn(modifier = Modifier.height(with(density) { size.height.toDp() })) {
            item { Spacer(modifier = Modifier.height(10.dp)) }
            items(dummyItem, key = { item -> item.id }) { book ->
                BookItem(
                    category = book.category,
                    date = book.date,
                    amount = Utils.rupiahFormatter(book.amount),
                    type = book.bookType,
                    isIncome = book.bookType == BookTypes.Pemasukan.string
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
private fun BookItem(
    category: String,
    date: String,
    amount: String,
    type: String,
    modifier: Modifier = Modifier,
    isIncome: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = type,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(
                        RoundedCornerShape(topEnd = 10.dp, bottomStart = 10.dp)
                    )
                    .background(if (isIncome) Green85 else Red40)
                    .padding(horizontal = 6.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = category, style = MaterialTheme.typography.titleMedium)
                    Text(text = date, style = MaterialTheme.typography.bodySmall)
                }
                Text(text = amount, style = MaterialTheme.typography.titleMedium)

            }
        }
    }

}

@Composable
private fun CardIncomeOutcome(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomCard(
                    labelText = "Pemasukan",
                    amount = "25.000.000",
                )
                CustomCard(
                    labelText = "Pengeluaran",
                    amount = "8.000.000",
                    isIncome = false
                )
            }
            Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Untung", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Rp17.000.000.-", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

    }
}

@Composable
private fun CustomCard(
    labelText: String,
    amount: String,
    modifier: Modifier = Modifier,
    isIncome: Boolean = true
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isIncome) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer)
    ) {
        Text(
            text = labelText,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
                )
                .background(if (isIncome) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer)
                .padding(horizontal = 6.dp, vertical = 2.dp)

        )
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = amount,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun FinancialStatementScreenPrev() {
    BisnisPlusTheme {
        FinancialStatementScreen(
            onNavigateBack = {}
        )
    }
}