package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun CustomUnderLineTextField(
    label: String,
    value: String,
    onChangeValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable() () -> Unit)? = null,
    trailingIcon: (@Composable() () -> Unit)? = null
) {
    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )
            TextField(
                value = value,
                onValueChange = onChangeValue,
                singleLine = true,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.weight(3f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomUnderLineTextFieldPrev() {
    BisnisPlusTheme {
        CustomUnderLineTextField(
            label = "Jumlah",
            value = "",
            onChangeValue = {}
        )
    }
}