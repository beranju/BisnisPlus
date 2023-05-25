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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.constant.ExpenseCategory
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.utils.Utils
import com.beran.bisnisplus.utils.toEpochMilli
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.model.StockItem
import com.beran.core.domain.model.Stocks
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditBookRecordScreen(
    state: BookStates<BookModel>,
    updateBookState: BookStates<Unit>,
    onNavigateBack: () -> Unit,
    getBookById: () -> Unit,
    onUpdateBook: (BookModel) -> Unit,
    bookId: String,
    modifier: Modifier = Modifier
) {
    when (state) {
        is BookStates.Loading -> getBookById()
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
            EditBookRecordContent(
                bookModel = state.data,
                onNavigateBack = onNavigateBack,
                onUpdateBook = onUpdateBook,
                bookId = bookId
            )
        }
    }
    when (updateBookState) {
        is BookStates.Loading -> getBookById()
        is BookStates.Error -> {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = updateBookState.message)
            }
        }

        is BookStates.Success -> onNavigateBack()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditBookRecordContent(
    bookModel: BookModel,
    onNavigateBack: () -> Unit,
    onUpdateBook: (BookModel) -> Unit,
    bookId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
    val scrollState = rememberScrollState()
    val listStock = remember {
        mutableStateListOf<StockItem>().apply {
            bookModel.listStock?.stocks?.let {
                addAll(it)
            }
        }
    }
    var selectedCategory by remember {
        mutableStateOf(bookModel.category)
    }
    var selectedType by remember {
        mutableStateOf(bookModel.type)
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
        mutableStateOf(bookModel.amount.toLong().toString())
    }
    var selectedStatus by remember {
        mutableStateOf(bookModel.state)
    }
    var mitraName by remember {
        mutableStateOf(bookModel.mitra)
    }
    var note by remember {
        mutableStateOf(bookModel.note)
    }
    val loading by remember {
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
                val name: String = contact?.contactName.orEmpty()
                mitraName = name
            }
        }
    )

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
                    selected = selectedType.orEmpty(),
                    onSelected = { newValue ->
                        selectedType = newValue
                        selectedCategory = ""
                    }
                )
                BookCategorySection(
                    selectedType = selectedType.orEmpty(),
                    selectedValue = selectedCategory.orEmpty(),
                    onChangeValue = { newValue -> selectedCategory = newValue },
                    isExpanded = expanded,
                    onChangeExpanded = { newValue -> expanded = newValue }
                )
                if (selectedCategory == ExpenseCategory.Stock.title) {
                    BookFormStockSection(
                        mitraName = mitraName.orEmpty(),
                        onChangeMitraName = { newName -> mitraName = newName },
                        selectedStatus = selectedStatus.orEmpty(),
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
                        selectedStatus = selectedStatus.orEmpty(),
                        onSelectedStatus = { newStatus -> selectedStatus = newStatus }
                    )
                }

                if (selectedCategory == ExpenseCategory.Stock.title) {
                    StockOptionalFormSection(
                        date = formattedDate,
                        onChangeDate = { },
                        note = note.orEmpty(),
                        onChangeNote = { newNote -> note = newNote },
                        onCalendarSelection = { newDate ->
                            selectedDate = newDate
                        }
                    )
                } else {
                    OptionalFormSection(
                        mitraName = mitraName.orEmpty(),
                        onChangeMitraName = { newName -> mitraName = newName },
                        date = formattedDate,
                        onChangeDate = { },
                        note = note.orEmpty(),
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
                        if (selectedCategory?.isNotEmpty() == true) {
                            if (selectedCategory == ExpenseCategory.Stock.title) {
                                if (listStock.isNotEmpty() && mitraName?.isNotEmpty() == true) {
                                    val stocks = Stocks(
                                        stocks = listStock.toList()
                                    )
                                    val book = BookModel(
                                        bookId = bookId,
                                        bisnisId = bookModel.bisnisId,
                                        amount = totalAmount,
                                        mitra = mitraName,
                                        note = note,
                                        category = selectedCategory,
                                        listStock = stocks,
                                        type = selectedType,
                                        state = selectedStatus,
                                        createdAt = bookModel.createdAt,
                                        updatedAt = selectedDate.toEpochMilli()
                                    )
                                    onUpdateBook(book)
                                } else {
                                    errorText = "Stok dan nama mitra tidak boleh kosong"
                                }

                            } else {
                                if (amountText.isNotEmpty()) {
                                    val amount =
                                        amountText.replace("[.,]".toRegex(), "").toDoubleOrNull()
                                    val book = BookModel(
                                        bookId = bookId,
                                        bisnisId = bookModel.bisnisId,
                                        amount = amount ?: 0.0,
                                        mitra = mitraName,
                                        note = note,
                                        category = selectedCategory,
                                        type = selectedType,
                                        state = selectedStatus,
                                        createdAt = bookModel.createdAt,
                                        updatedAt = selectedDate.toEpochMilli()
                                    )
                                    onUpdateBook(book)
                                } else {
                                    errorText = "Jumlah tidak boleh kosong"
                                }
                            }
                        } else {
                            errorText = "Kategori tidak boleh kosong"
                        }

                    }, enabled = !loading, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Simpan")
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