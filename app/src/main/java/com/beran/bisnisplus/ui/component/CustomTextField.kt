package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    labelText: String,
    hintText: String,
    icon: ImageVector,
    value: String,
    onChangeValue: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null,
    isError: Boolean = false,
    keyBoardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = labelText, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            singleLine = true,
            onValueChange = onChangeValue,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = hintText, style = MaterialTheme.typography.bodyMedium)
            },
            leadingIcon = {
                Icon(imageVector = icon, contentDescription = null)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(
                        text = errorText.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )
    }
}