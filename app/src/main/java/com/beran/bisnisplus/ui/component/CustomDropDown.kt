package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun CustomDropDown(
    options: List<String>,
    selectedValue: String,
    onChangeValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    hint: String? = null,
) {

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (label != null) Text(text = label, style = MaterialTheme.typography.labelMedium)
            Box {
                OutlinedTextField(
                    value = selectedValue,
                    readOnly = true,
                    onValueChange = onChangeValue,
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
                    options.map { item ->
                        DropdownMenuItem(text = { Text(text = item) }, onClick = {
                            onChangeValue(item)
                            expanded = false
                        })
                    }

                }
            }
            if (hint != null) Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun CustomDropDownPrev() {
    BisnisPlusTheme {
        CustomDropDown(
            options = listOf("Menu1", "Menu2", "Menu3"),
            selectedValue = "Menu1",
            onChangeValue = {}
        )
    }
}