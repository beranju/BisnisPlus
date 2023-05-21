package com.beran.bisnisplus.ui.screen.pembukuan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beran.bisnisplus.constant.BookTypes
import com.beran.bisnisplus.constant.ExpenseCategory
import com.beran.bisnisplus.constant.Status
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.component.CustomBadge
import com.beran.bisnisplus.ui.component.CustomStockField
import com.beran.bisnisplus.ui.component.CustomTextArea
import com.beran.bisnisplus.ui.component.CustomUnderLineTextField
import com.beran.bisnisplus.ui.component.ExpensesCustomDropdown
import com.beran.bisnisplus.ui.component.IncomeCustomDropdown
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import com.beran.bisnisplus.utils.Utils.toEpochMilli
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.model.StockItem
import com.beran.core.domain.model.Stocks
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateNewRecordScreen(
    viewModel: BookViewModel,
    onCreateNewBook: (BookModel) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val listStock = remember {
        mutableStateListOf<StockItem>()
    }
    var selectedCategory by remember {
        mutableStateOf("")
    }
    var selectedType by remember {
        mutableStateOf(BookTypes.Pemasukan.string)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    val totalAmount by remember(listStock) {
        derivedStateOf {
            listStock.sumOf { it.totalAmount }
        }
    }
    var amountText by remember {
        mutableStateOf("")
    }
    var selectedStatus by remember {
        mutableStateOf(Status.Lunas.title)
    }
    var mitraName by remember {
        mutableStateOf("")
    }
    var note by remember {
        mutableStateOf("")
    }
    var loading by remember {
        mutableStateOf(false)
    }
    var errorText by remember {
        mutableStateOf<String?>(null)
    }
    var selectedDate by remember {
        mutableStateOf<LocalDate>(LocalDate.now())
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("dd MMM yyyy").format(selectedDate)
        }
    }
    val contactIntent = Intent(Intent.ACTION_PICK).apply {
        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
    }
    val pickContactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contactUri: Uri? = result.data?.data
                val contact = Utils.getContacts(contactUri, context.contentResolver)
                val name: String = contact?.contactName ?: "Joko"
                mitraName = name
            }
        }
    )

    DisposableEffect(key1 = state, effect = {
        when (state) {
            is BookState.Loading -> loading = true
            is BookState.Error -> errorText = (state as BookState.Error).message
            is BookState.Success -> onNavigateBack()
            is BookState.Initial -> {}
        }
        onDispose { viewModel.resetUiState }
    })

    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Buat Pembukuan",
            onLeadingClick = onNavigateBack,
            leadingIcon = Icons.Outlined.NavigateBefore
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                if (errorText != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(vertical = 4.dp, horizontal = 6.dp)
                    ) {
                        Text(
                            text = errorText.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                BookTypeSection(
                    selected = selectedType,
                    onSelected = { newValue ->
                        selectedType = newValue
                        selectedCategory = ""
                    }
                )
                BookCategorySection(
                    selectedType = selectedType,
                    selectedValue = selectedCategory,
                    onChangeValue = { newValue -> selectedCategory = newValue },
                    isExpanded = expanded,
                    onChangeExpanded = { newValue -> expanded = newValue }
                )
                if (selectedCategory == ExpenseCategory.Stock.title) {
                    BookFormStockSection(
                        mitraName = mitraName,
                        onChangeMitraName = { newName -> mitraName = newName },
                        selectedStatus = selectedStatus,
                        onSelectedStatus = { status -> selectedStatus = status },
                        onAddNewStock = { stock ->
                            listStock.add(stock)
                        },
                        listStock = listStock,
                        totalAmount = totalAmount,
                        onGetContact = {
                            if (permissionState.hasPermission) {
                                pickContactLauncher.launch(contactIntent)
                            } else if (permissionState.permissionRequested) {
                                pickContactLauncher.launch(contactIntent)
                            } else {
                                permissionState.launchPermissionRequest()
                            }
                        }
                    )
                } else {
                    BookFormSection(
                        amount = amountText,
                        onChangeAmount = { newValue -> amountText = newValue },
                        selectedStatus = selectedStatus,
                        onSelectedStatus = { newStatus -> selectedStatus = newStatus }
                    )
                }

                if (selectedCategory == ExpenseCategory.Stock.title) {
                    StockOptionalFormSection(
                        date = formattedDate,
                        onChangeDate = { },
                        note = note,
                        onChangeNote = { newNote -> note = newNote },
                        onCalendarSelection = { newDate ->
                            selectedDate = newDate
                        }
                    )
                } else {
                    OptionalFormSection(
                        mitraName = mitraName,
                        onChangeMitraName = { newName -> mitraName = newName },
                        date = formattedDate,
                        onChangeDate = { },
                        note = note,
                        onChangeNote = { newNote -> note = newNote },
                        onCalendarSelection = { newDate ->
                            selectedDate = newDate
                        },
                        selectedDate = selectedDate,
                        onGetContact = {
                            if (permissionState.hasPermission) {
                                pickContactLauncher.launch(contactIntent)
                            } else if (permissionState.permissionRequested) {
                                pickContactLauncher.launch(contactIntent)
                            } else {
                                permissionState.launchPermissionRequest()
                            }
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                ) {
                    Button(onClick = {
                        val bookId = System.currentTimeMillis().toString()
                        if (selectedCategory.isNotEmpty()) {
                            if (selectedCategory == ExpenseCategory.Stock.title) {
                                if (listStock.isNotEmpty() && mitraName.isNotEmpty()) {
                                    val stocks = Stocks(
                                        stocks = listStock.toList()
                                    )
                                    val bookModel = BookModel(
                                        bookId = bookId,
                                        amount = totalAmount,
                                        mitra = mitraName,
                                        note = note,
                                        category = selectedCategory,
                                        listStock = stocks,
                                        type = selectedType,
                                        state = selectedStatus,
                                        createdAt = selectedDate.toEpochMilli()
                                    )
                                    onCreateNewBook(bookModel)
                                } else {
                                    errorText = "Stok dan nama mitra tidak boleh kosong"
                                }

                            } else {
                                if (amountText.isNotEmpty()) {
                                    val amount =
                                        amountText.replace("[.,]".toRegex(), "").toDoubleOrNull()
                                    val bookModel = BookModel(
                                        bookId = bookId,
                                        amount = amount ?: 0.0,
                                        mitra = mitraName,
                                        note = note,
                                        category = selectedCategory,
                                        type = selectedType,
                                        state = selectedStatus,
                                        createdAt = selectedDate.toEpochMilli()
                                    )
                                    onCreateNewBook(bookModel)
                                } else {
                                    errorText = "Jumlah tidak boleh kosong"
                                }
                            }
                        } else {
                            errorText = "Kategori tidak boleh kosong"
                        }

                    }, enabled = !loading, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Tambahkan")
                    }
                    if (loading) {
                        errorText = null
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(25.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockOptionalFormSection(
    date: String,
    onChangeDate: (String) -> Unit,
    note: String,
    onChangeNote: (String) -> Unit,
    onCalendarSelection: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {

    val state = rememberUseCaseState()

    CalendarDialog(
        state = state,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH
        ),
        selection = CalendarSelection.Date { newDates ->
            onCalendarSelection(newDates)
        }
    )

    Box(modifier = modifier.padding(top = 24.dp)) {
        Column {
            Text(text = "Optional", style = MaterialTheme.typography.bodySmall)
            Divider()
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CustomUnderLineTextField(
                    label = "Hari",
                    value = date,
                    onChangeValue = onChangeDate,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = "Calendar Page"
                        )
                    }
                )
                CustomTextArea(
                    value = note,
                    onValueChange = onChangeNote,
                    labelName = "Catatan",
                    hintText = "* Optional"
                )
            }
        }
    }
}

@Composable
fun BookFormStockSection(
    mitraName: String,
    listStock: List<StockItem>,
    onChangeMitraName: (String) -> Unit,
    selectedStatus: String,
    onSelectedStatus: (String) -> Unit,
    onAddNewStock: (StockItem) -> Unit,
    onGetContact: () -> Unit,
    modifier: Modifier = Modifier,
    totalAmount: Double = 0.0
) {
    Box(modifier = modifier.padding(top = 24.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            CustomUnderLineTextField(
                label = "Nama Mitra",
                value = mitraName,
                onChangeValue = onChangeMitraName,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ContactPage,
                        contentDescription = "Halaman Kontak",
                        modifier = Modifier.clickable {
                            onGetContact()
                        }
                    )
                }
            )
            CustomStockField(listStock, addNewStock = onAddNewStock, totalAmount = totalAmount)
            StatusOptionSection(
                selectedStatus = selectedStatus,
                onSelectedStatus = onSelectedStatus
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionalFormSection(
    mitraName: String,
    onChangeMitraName: (String) -> Unit,
    date: String,
    selectedDate: LocalDate,
    onCalendarSelection: (LocalDate) -> Unit,
    onChangeDate: (String) -> Unit,
    note: String,
    onChangeNote: (String) -> Unit,
    onGetContact: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberUseCaseState()

    CalendarDialog(
        state = state,
        config = CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            style = CalendarStyle.MONTH
        ),
        selection = CalendarSelection.Date { newDates ->
            onCalendarSelection(newDates)
        }
    )
    Box(modifier = modifier.padding(top = 24.dp)) {
        Column {
            Text(text = "Optional", style = MaterialTheme.typography.bodySmall)
            Divider()
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CustomUnderLineTextField(
                    label = "Nama Mitra",
                    value = mitraName,
                    onChangeValue = onChangeMitraName,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.ContactPage,
                            contentDescription = "Contact Page",
                            modifier = Modifier.clickable {
                                onGetContact()
                            }
                        )
                    }
                )
                CustomUnderLineTextField(
                    label = "Hari",
                    value = date,
                    readOnly = true,
                    onChangeValue = onChangeDate,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                state.show()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Calendar Page"
                            )
                        }
                    }
                )
                CustomTextArea(
                    value = note,
                    onValueChange = onChangeNote,
                    labelName = "Catatan",
                    hintText = "* Optional"
                )
            }
        }
    }

}

@Composable
fun BookFormSection(
    amount: String,
    onChangeAmount: (String) -> Unit,
    selectedStatus: String,
    onSelectedStatus: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(top = 24.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            CustomUnderLineTextField(
                label = "Jumlah",
                value = amount,
                onChangeValue = onChangeAmount,
                keyboardType = KeyboardType.Number,
                leadingIcon = { Text(text = "Rp", style = MaterialTheme.typography.labelLarge) }
            )
            StatusOptionSection(
                selectedStatus = selectedStatus,
                onSelectedStatus = onSelectedStatus
            )
        }
    }
}

@Composable
fun StatusOptionSection(
    selectedStatus: String,
    onSelectedStatus: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Status",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(3f)
            ) {
                CustomBadge(
                    title = "Lunas",
                    isSelected = selectedStatus == Status.Lunas.title,
                    onClicked = { onSelectedStatus(Status.Lunas.title) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
                CustomBadge(
                    title = "Belum Lunas",
                    isSelected = selectedStatus == Status.BelumLunas.title,
                    onClicked = { onSelectedStatus(Status.BelumLunas.title) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                )
            }
        }
    }

}

@Composable
fun BookCategorySection(
    selectedType: String,
    selectedValue: String,
    onChangeValue: (String) -> Unit,
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(top = 24.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Pilih Kategori", style = MaterialTheme.typography.labelMedium)
            when (selectedType) {
                BookTypes.Pemasukan.string -> {
                    IncomeCustomDropdown(
                        selectedValue = selectedValue,
                        onChangeValue = onChangeValue,
                        isExpanded = isExpanded,
                        onChangeExpanded = onChangeExpanded
                    )
                }

                BookTypes.Pengeluaran.string -> {
                    ExpensesCustomDropdown(
                        selectedValue = selectedValue,
                        onChangeValue = onChangeValue,
                        isExpanded = isExpanded,
                        onChangeExpanded = onChangeExpanded
                    )
                }
            }

        }
    }

}

@Composable
fun BookTypeSection(
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BookTypeBadge(
                title = BookTypes.Pemasukan.string,
                isSelected = selected == BookTypes.Pemasukan.string,
                onClicked = {
                    onSelected(BookTypes.Pemasukan.string)
                },
                modifier = modifier.weight(1f)
            )
            BookTypeBadge(
                title = BookTypes.Pengeluaran.string,
                isSelected = selected == BookTypes.Pengeluaran.string,
                onClicked = {
                    onSelected(BookTypes.Pengeluaran.string)
                },
                modifier = modifier.weight(1f)
            )

        }
        Text(
            text = "* Tentukan jenis pembukuan yang anda buat",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
        )
    }
}

@Composable
fun BookTypeBadge(
    title: String,
    isSelected: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(30.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (title == BookTypes.Pemasukan.string) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer)
            .padding(end = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClicked,
                colors = RadioButtonDefaults.colors(if (title == BookTypes.Pemasukan.string) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer)
            )
            Text(text = title, style = MaterialTheme.typography.labelMedium)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun CreateNewRecordScreenPrev() {
    BisnisPlusTheme {
        CreateNewRecordScreen(
            viewModel = koinViewModel(),
            onCreateNewBook = {},
            onNavigateBack = {},
        )
    }
}