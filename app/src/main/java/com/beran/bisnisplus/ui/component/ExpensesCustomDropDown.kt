package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.constant.ExpenseCategory
import com.beran.bisnisplus.constant.IncomeCategory
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun ExpensesCustomDropdown(
    selectedValue: String,
    onChangeValue: (String) -> Unit,
    isExpanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        var textFieldSize by remember {
            mutableStateOf(Size.Zero)
        }
        val listCategory = ExpenseCategory.values()

        Box {
            OutlinedTextField(value = selectedValue,
                onValueChange = onChangeValue,
                label = { Text(text = "Kategori") },
                trailingIcon = {
                    Icon(imageVector = if (isExpanded) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
                        contentDescription = "open dropdown",
                        modifier = Modifier.clickable {
                            onChangeExpanded(!isExpanded)
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
                expanded = isExpanded,
                onDismissRequest = { onChangeExpanded(false) },
                modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                listCategory.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(text = label.title) },
                        onClick = {
                            onChangeValue(label.title)
                            onChangeExpanded(false)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpensesCustomDropDownPrev() {
    BisnisPlusTheme {
        ExpensesCustomDropdown(
            selectedValue = "",
            onChangeValue = {},
            isExpanded = false,
            onChangeExpanded = {}
        )
    }
}