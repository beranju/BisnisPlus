package com.beran.bisnisplus.ui.screen.pembukuan.component

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.toSize

@Composable
fun VerticalCustomDropDown(
    labelText: String,
    hintText: String,
    unitName: String,
    onChangeUnitName: (String) -> Unit,
    onChangeExpanded: (Boolean) -> Unit,
    listMenu: List<String>,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
) {
    val localDensity = LocalDensity.current
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }

    Box(modifier = modifier) {
        Column {
            Text(text = labelText, style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = unitName,
                onValueChange = onChangeUnitName,
                placeholder = {
                    Text(
                        text = hintText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (isExpanded) Icons.Outlined.ArrowDropUp else Icons.Outlined.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onChangeExpanded(!isExpanded)
                        })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { layoutCoordinates ->
                        textFieldSize = layoutCoordinates.size.toSize()
                    }
            )
        }
        DropdownMenu(expanded = isExpanded,
            onDismissRequest = { !isExpanded },
            modifier = Modifier.width(
                with(localDensity) { textFieldSize.width.toDp() }
            )) {
            listMenu.map { menu ->
                DropdownMenuItem(text = { Text(text = menu) }, onClick = {
                    onChangeUnitName(menu)
                    onChangeExpanded(!isExpanded)
                }
                )
            }
        }
    }
}
