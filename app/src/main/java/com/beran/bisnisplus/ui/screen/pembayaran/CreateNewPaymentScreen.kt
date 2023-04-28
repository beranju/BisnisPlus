package com.beran.bisnisplus.ui.screen.pembayaran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun CreateNewPaymentScreen() {
    CreateNewPaymentContent()
}

@Composable
private fun CreateNewPaymentContent(
    modifier: Modifier = Modifier
) {
    var entityName by remember {
        mutableStateOf("")
    }
    var mitraName by remember {
        mutableStateOf("")
    }
    var totalPayment by remember {
        mutableStateOf("")
    }
    var catatan by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Buat Tagihan",
            onLeadingClick = { },
            leadingIcon = Icons.Outlined.NavigateBefore
        )
    }) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(modifier = modifier.padding(horizontal = 26.dp)) {
                Text(text = "Pilih Kategori", style = MaterialTheme.typography.labelMedium)
                CategoryPaymentSection()
                Spacer(modifier = Modifier.height(16.dp))
                CustomDataFormField(
                    labelName = "Nama Entitas",
                    textHint = "Masukan nama Entitas yang ditagih",
                    value = entityName,
                    onChangeValue = { newValue -> entityName = newValue }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomDataFormField(
                    labelName = "Nama Mitra",
                    textHint = "Masukkan nama mitra",
                    value = mitraName,
                    onChangeValue = { newValue -> mitraName = newValue }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomDataFormField(
                    labelName = "Total Tagihan",
                    textHint = "Misalnya 200.000",
                    value = totalPayment,
                    onChangeValue = { newValue -> totalPayment = newValue }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextArea(
                    value = catatan,
                    onValueChange = { newValue -> catatan = newValue },
                    labelName = "Catatan",
                    hintText = "*Opsional"
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Buat Tagihan", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

    }

}

@Composable
private fun CategoryPaymentSection() {
    var selectedValue by remember {
        mutableStateOf("")
    }

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    val listCategory = listOf(
        "Semua",
        "Tagihan",
        "Utang"
    )

    Box {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = { newValue -> selectedValue = newValue },
            label = { Text(text = "Jenis Pembayaran") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
                    contentDescription = "Show Options",
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    textFieldSize = layoutCoordinates.size.toSize()
                }
        )
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) { textFieldSize.width.toDp() }
            )) {
            listCategory.map { item ->
                DropdownMenuItem(text = { Text(text = item) }, onClick = {
                    selectedValue = item
                    expanded = false
                })
            }
        }
    }
}

@Preview
@Composable
fun CreateNewPaymentScreenPrev() {
    BisnisPlusTheme {
        CreateNewPaymentScreen()
    }
}
