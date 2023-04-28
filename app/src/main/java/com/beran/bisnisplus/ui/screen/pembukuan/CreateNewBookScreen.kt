package com.beran.bisnisplus.ui.screen.pembukuan

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.component.CustomDataFormField
import com.beran.bisnisplus.ui.component.CustomTextArea
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun CreateNewBookScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var entityName by remember {
        mutableStateOf("")
    }
    var mitraName by remember {
        mutableStateOf("")
    }
    var price by remember {
        mutableStateOf("")
    }
    var weight by remember {
        mutableStateOf("")
    }
    var catatan by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Tambah Pembukuan",
            onLeadingClick = onNavigateBack,
            leadingIcon = Icons.Outlined.NavigateBefore
        )
    }) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                CategoryPembukuanSection()
                Text(text = "Pilih Kategori", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp))
                CustomDataFormField(labelName = "Nama Entitas",
                    textHint = "Masukkan nama entitas",
                    value = entityName,
                    onChangeValue = { newValue -> entityName = newValue })
                Spacer(modifier = Modifier.height(16.dp))
                CustomDataFormField(labelName = "Nama Mitra",
                    textHint = "Masukkan nama mitra",
                    value = mitraName,
                    onChangeValue = { newValue -> mitraName = newValue })
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomKuantitasFormField(
                        labelName = "Berat",
                        satuan = "Kg",
                        value = weight,
                        onChangeValue = { newValue -> weight = newValue },
                        modifier = Modifier.weight(1f)
                    )
                    CustomKuantitasFormField(
                        labelName = "Harga",
                        satuan = "Rp",
                        value = price,
                        onChangeValue = { newValue -> price = newValue },
                        modifier = Modifier.weight(3f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextArea(
                    value = catatan,
                    onValueChange = { newValue -> catatan = newValue },
                    labelName = "Catatan",
                    hintText = "*Opsional"
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Buat Pembukuan", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

    }
}

@Composable
private fun CustomKuantitasFormField(
    labelName: String,
    satuan: String,
    value: String,
    onChangeValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = labelName, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = value,
            onValueChange = onChangeValue,
            trailingIcon = { Text(text = satuan, style = MaterialTheme.typography.labelMedium) })
    }
}

@Composable
private fun CategoryPembukuanSection() {
    var selectedText by remember {
        mutableStateOf("")
    }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    val listCategory = listOf(
        "Barang masuk", "Barang Keluar", "Hutang", "Pinjaman", "Pendapatan"
    )

    Box {
        OutlinedTextField(value = selectedText,
            onValueChange = { newValue -> selectedText = newValue },
            label = { Text(text = "Kategori") },
            trailingIcon = {
                Icon(imageVector = if (expanded) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
                    contentDescription = "open dropdown",
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    })
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinate ->
                    // ** this value is used to assign to
                    // ** the drop down the same width
                    textFieldSize = coordinate.size.toSize()
                })
        /**
         * show dropdown when icon dropdown clicked
         */
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            listCategory.forEach { label ->
                DropdownMenuItem(text = { Text(text = label) }, onClick = {
                    selectedText = label
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateNewBookScreenPrev() {
    BisnisPlusTheme {
        CreateNewBookScreen(onNavigateBack = {})
    }
}