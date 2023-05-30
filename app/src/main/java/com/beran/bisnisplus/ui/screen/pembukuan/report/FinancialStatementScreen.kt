package com.beran.bisnisplus.ui.screen.pembukuan.component

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.constant.BookTypes
import com.beran.bisnisplus.constant.TimeRange
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.component.CustomDropDown
import com.beran.bisnisplus.ui.component.EmptyView
import com.beran.bisnisplus.ui.screen.pembukuan.BookStates
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.ui.theme.Green85
import com.beran.bisnisplus.utils.Utils
import com.beran.core.domain.model.BookModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FinancialStatementScreen(
    state: BookStates<List<BookModel>>,
    uiState: BookStates<Unit>,
    onNavigateBack: () -> Unit,
    selectedDate: String,
    onDateChange: (String) -> Unit,
    exportDataIntoCsv: (String) -> Unit,
    fetchListBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    val options: List<String> = TimeRange.values().toList().map { it.range }
    val context = LocalContext.current
    var filePath: String?
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    val fileId = System.currentTimeMillis()
//    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
//        addCategory(Intent.CATEGORY_OPENABLE)
//        type = "application/vnd.ms-excel"
//        putExtra(Intent.EXTRA_TITLE, "${fileId}bisnisplus.xlsx")
//    }
    val writeDocLauncher = rememberLauncherForActivityResult(
        contract = CreateDocument("text/csv"),
        onResult = { uri ->
            uri?.let {
                filePath = Utils.getFileFromUri(context, uri)
                filePath?.let {
                    exportDataIntoCsv(it)
                }
            }
        }
    )

    when (uiState) {
        is BookStates.Loading -> Timber.tag("Financial").i("Convert Loading")
        is BookStates.Success -> {
            Timber.tag("Financial").i(" convert Success")
        }

        is BookStates.Error -> Timber.tag("Financial").i("Error: ${uiState.message}")
    }

    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Laporan Keuangan",
            onLeadingClick = onNavigateBack, leadingIcon = Icons.Outlined.NavigateBefore
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                CustomDropDown(
                    options = options,
                    selectedValue = selectedDate,
                    onChangeValue = { newValue -> onDateChange(newValue) },
                    label = "Tentukan rentang waktu"
                )
                Spacer(modifier = Modifier.height(24.dp))
                when (state) {
                    is BookStates.Loading -> fetchListBook()
                    is BookStates.Error -> {
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = state.message)
                        }
                    }

                    is BookStates.Success -> {
                        Timber.tag("Financial").i("${state.data}")
                        FinancialStatementContent(
                            listBook = state.data,
                            modifier = modifier
                        )
                    }
                }
            }
            Button(
                onClick = {
                    val fileName = System.currentTimeMillis()
                    val folderPath = "bisnisPlus/${fileName}.csv"
                    if (permissionState.hasPermission) {
                        writeDocLauncher.launch(folderPath)
                    } else if (permissionState.permissionRequested) {
                        writeDocLauncher.launch(folderPath)
                    } else {
                        permissionState.launchPermissionRequest()
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Unduh Laporan",
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}


@Composable
fun FinancialStatementContent(
    listBook: List<BookModel>,
    modifier: Modifier = Modifier
) {
    val incomeAmount by remember {
        derivedStateOf {
            listBook.filter { it.type == BookTypes.Pemasukan.string }.sumOf { it.amount }
        }
    }
    val expenseAmount by remember {
        derivedStateOf {
            listBook.filter { it.type == BookTypes.Pengeluaran.string }.sumOf { it.amount }
        }
    }

    Box(modifier = modifier) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            CardIncomeOutcome(
                incomeAmount, expenseAmount
            )
            if (listBook.isEmpty()) {
                EmptyView(hintText = "Data pembukuan kosong")
            } else {
                ListBookSection(listBook)
            }
        }
    }

}

@Composable
private fun ListBookSection(
    listBook: List<BookModel>,
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
            items(listBook, key = { item -> item.bookId.orEmpty() }) { book ->
                BookItem(
                    category = book.category.orEmpty(),
                    date = Utils.convertToDate(book.createdAt ?: 0),
                    amount = Utils.rupiahFormatter(book.amount.toLong()),
                    type = book.type.orEmpty(),
                    isIncome = book.type == BookTypes.Pemasukan.string
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun BookItem(
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
                    .background(if (isIncome) Green85 else MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 6.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(3f)
                ) {
                    Text(
                        text = category,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(text = date, style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = amount,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(2f)
                )

            }
        }
    }
}

@Composable
private fun CardIncomeOutcome(
    incomeAmount: Double,
    expenseAmount: Double,
    modifier: Modifier = Modifier
) {
    val income = Utils.rupiahFormatter(incomeAmount.toLong())
    val expense = Utils.rupiahFormatter(expenseAmount.toLong())
    val profitable = if (incomeAmount > expenseAmount) {
        "Untung"
    } else if (incomeAmount < expenseAmount) {
        "Rugi"
    } else {
        "Selisih"
    }
    val difference by remember {
        derivedStateOf {
            incomeAmount.minus(expenseAmount)
        }
    }
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
                    amount = income,
                )
                CustomCard(
                    labelText = "Pengeluaran",
                    amount = expense,
                    isIncome = false
                )
            }
            Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = profitable,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = Utils.rupiahFormatter(difference.toLong()),
                        style = MaterialTheme.typography.titleMedium
                    )
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
        Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 16.dp)) {
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
            state = BookStates.Loading,
            uiState = BookStates.Loading,
            selectedDate = "",
            onDateChange = {},
            onNavigateBack = {},
            fetchListBook = {},
            exportDataIntoCsv = {},
        )
    }
}