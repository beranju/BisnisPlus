package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.data.listDummyBuku
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun PembukuanScreen() {

    Scaffold(
        floatingActionButton = {
            Button(onClick = { }, shape = CircleShape) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Buat pembukuan")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                CategoryBukuSection()
                Text(text = "*Pilih kategori", style = MaterialTheme.typography.bodySmall)
                PembukuanSection()
            }
        }
    }
}

@Composable
private fun PembukuanSection() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(listDummyBuku, key = { item -> item.id }) { item ->
            PembukuanCard(
                judulBuku = item.judulBuku,
                namaAgen = item.agen,
                jenisBuku = item.kategori,
                date = item.date,
                width = Dp.Infinity
            )
        }
    }
}

@Composable
fun CategoryBukuSection() {

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
        "Barang masuk",
        "Barang Keluar",
        "Hutang",
        "Pinjaman",
        "Pendapatan"
    )

    Box {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { newValue -> selectedText = newValue },
            label = { Text(text = "Kategori") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
                    contentDescription = "open dropdown",
                    modifier = Modifier.clickable {
                        expanded = !expanded
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinate ->
                    // ** this value is used to assign to
                    // ** the drop down the same width
                    textFieldSize = coordinate.size.toSize()
                }
        )
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
fun PembukuanScreenPrev() {
    BisnisPlusTheme {
        PembukuanScreen()
    }
}