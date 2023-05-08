package com.beran.bisnisplus.ui.screen.pembukuan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ContactPage
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Composable
fun CreateNewRecordScreen(
    modifier: Modifier = Modifier
) {
    var scrollState = rememberScrollState()
    var selectedCategory by remember {
        mutableStateOf("")
    }
    var selectedType by remember {
        mutableStateOf(BookTypes.Pemasukan.string)
    }
    var expanded by remember {
        mutableStateOf(false)
    }
    var amount by remember {
        mutableStateOf("")
    }
    var selectedStatus by remember {
        mutableStateOf(Status.Lunas.title)
    }
    var mitraName by remember {
        mutableStateOf("")
    }
    var date by remember {
        mutableStateOf("")
    }
    var note by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Buat Pembukuan",
            onLeadingClick = {},
            leadingIcon = Icons.Outlined.NavigateBefore
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(state = scrollState)
            ) {
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
                if (selectedCategory == ExpenseCategory.Stock.title || selectedCategory == ExpenseCategory.Equipment.title) {
                    BookFormStockSection(
                        mitraName = mitraName,
                        onChangeMitraName = { newName -> mitraName = newName },
                        selectedStatus = selectedStatus,
                        onSelectedStatus = { status -> selectedStatus = status }
                    )
                } else {
                    BookFormSection(
                        amount = amount,
                        onChangeAmount = { newValue -> amount = newValue },
                        selectedStatus = selectedStatus,
                        onSelectedStatus = { newStatus -> selectedStatus = newStatus }
                    )
                }

                if (selectedCategory == ExpenseCategory.Stock.title) {
                    StockOptionalFormSection(
                        date = date,
                        onChangeDate = { newDate -> date = newDate },
                        note = note,
                        onChangeNote = { newNote -> note = newNote }
                    )
                } else {
                    OptionalFormSection(
                        mitraName = mitraName,
                        onChangeMitraName = { newName -> mitraName = newName },
                        date = date,
                        onChangeDate = { newDate -> date = newDate },
                        note = note,
                        onChangeNote = { newNote -> note = newNote }
                    )
                }

                Box(modifier = Modifier.padding(top = 30.dp)) {
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Tambahkan")
                    }
                }
            }
        }
    }
}

@Composable
fun StockOptionalFormSection(
    date: String,
    onChangeDate: (String) -> Unit,
    note: String,
    onChangeNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
    onChangeMitraName: (String) -> Unit,
    selectedStatus: String,
    onSelectedStatus: (String) -> Unit,
    modifier: Modifier = Modifier
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
                        contentDescription = "Halaman Kontak"
                    )
                }
            )
            CustomStockField()
            StatusOptionSection(
                selectedStatus = selectedStatus,
                onSelectedStatus = onSelectedStatus
            )
        }
    }
}

@Composable
fun OptionalFormSection(
    mitraName: String,
    onChangeMitraName: (String) -> Unit,
    date: String,
    onChangeDate: (String) -> Unit,
    note: String,
    onChangeNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                            contentDescription = "Contact Page"
                        )
                    }
                )
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

@Preview(showBackground = true)
@Composable
fun CreateNewRecordScreenPrev() {
    BisnisPlusTheme {
        CreateNewRecordScreen()
    }
}