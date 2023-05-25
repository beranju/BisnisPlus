package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LooksOne
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.constant.UnitName
import com.beran.bisnisplus.ui.screen.pembukuan.component.StockCard
import com.beran.bisnisplus.ui.screen.pembukuan.component.VerticalCustomDropDown
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import com.beran.core.domain.model.StockItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomStockField(
    listStock: List<StockItem>,
    totalAmount: Double,
    addNewStock: (StockItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val localDensity = LocalDensity.current
    val listUnitName = UnitName.values().map { it.value }.toList()
    var size by remember {
        mutableStateOf(Size.Zero)
    }
    var stockName by remember {
        mutableStateOf("")
    }
    var stockQuantityText by remember {
        mutableStateOf("0")
    }
    var unitPriceText by remember {
        mutableStateOf("")
    }
    var unit by remember {
        mutableStateOf("Kg")
    }
    var openDialog by remember {
        mutableStateOf(false)
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .heightIn(max = 500.dp)
            .padding(top = 24.dp)
            .onGloballyPositioned { layoutCoordinates ->
                size = layoutCoordinates.size.toSize()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
        ) {
            Text(
                text = "Stok",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(3f)
            ) {
                if (listStock.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.height(with(localDensity) { size.height.toDp() - (size.height.toDp() / 4) })) {
                        items(listStock, key = { item -> item.stockId.toString() }) { stock ->
                            StockCard(
                                stockName = stock.stockName.orEmpty(),
                                stockUnitName = stock.unitName,
                                stockQuantityText = stock.unit,
                                stockUnitPrice = stock.unitPrice.toLong(),
                            )
                        }
                        item {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(text = "Total", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = Utils.rupiahFormatter(totalAmount.toLong()),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                } else {
                    Text(text = "Tambahkan Data", style = MaterialTheme.typography.bodyMedium)
                }

                Button(
                    onClick = {
                        openDialog = true
                    }, modifier = Modifier
                        .height(30.dp)
                        .align(Alignment.End)
                ) {
                    Text(
                        text = "Tambah +",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                    )
                }
            }
        }

    }
    /**
     * Menampilkan alert dialog untuk menambahkan stock item...
     *
     */
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp, horizontal = 10.dp)
        ) {
            var dialogErrorText by remember {
                mutableStateOf<String?>(null)
            }
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.background) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = "Masukkan Data Stock",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dialogErrorText ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    CustomTextField(
                        labelText = "Nama stok",
                        hintText = "Masukkan nama stok",
                        icon = Icons.Outlined.Inventory2,
                        value = stockName,
                        onChangeValue = { newValue -> stockName = newValue }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomTextField(
                            labelText = "kuantitas",
                            hintText = "Kuantitas",
                            icon = Icons.Outlined.LooksOne,
                            keyBoardType = KeyboardType.Number,
                            value = stockQuantityText,
                            onChangeValue = { newValue ->
                                stockQuantityText = newValue
                            },
                            modifier = Modifier.weight(3f)
                        )
                        VerticalCustomDropDown(
                            unitName = unit,
                            onChangeUnitName = { newValue -> unit = newValue },
                            isExpanded = isExpanded,
                            onChangeExpanded = { newValue -> isExpanded = newValue },
                            listMenu = listUnitName,
                            labelText = "Satuan",
                            hintText = "Kg",
                            modifier = Modifier.weight(2f)
                        )
                    }
                    CustomTextField(
                        labelText = "Harga satuan",
                        hintText = "Masukkan harga satuan",
                        icon = Icons.Outlined.Sell,
                        keyBoardType = KeyboardType.Number,
                        value = unitPriceText,
                        onChangeValue = { newValue -> unitPriceText = newValue }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                openDialog = false
                            }, colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = "Batal",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Button(onClick = {
                            if (stockName.isNotEmpty() || stockQuantityText.isNotEmpty()) {
                                val stockId = System.currentTimeMillis().toString()
                                val stockQuantity =
                                    stockQuantityText.replace(",", ".").toDoubleOrNull()
                                val unitPrice =
                                    unitPriceText.replace("[.,]".toRegex(), "").toLongOrNull()
                                val amountCost = stockQuantity?.times(unitPrice ?: 0)
                                val stockItem = StockItem(
                                    stockId = stockId,
                                    stockName = stockName,
                                    unit = stockQuantity ?: 0.0,
                                    unitPrice = unitPrice?.toDouble() ?: 0.0,
                                    unitName = unit,
                                    totalAmount = amountCost ?: 0.0
                                )
                                addNewStock(stockItem)
                                openDialog = false
                            } else {
                                dialogErrorText = "Kolom tidak boleh kosong"
                            }
                        }) {
                            Text(
                                text = "Tambah",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomStockFieldPrev() {
    BisnisPlusTheme {
        CustomStockField(arrayListOf(), addNewStock = {}, totalAmount = 0.0)
    }
}