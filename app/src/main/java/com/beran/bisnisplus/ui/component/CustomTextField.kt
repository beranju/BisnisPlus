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
import com.beran.bisnisplus.ui.theme.OutlineColor

@Composable
fun CustomTextField(
    labelText: String,
    hintText: String,
    icon: ImageVector,
    value: String,
    onChangeValue: (String) -> Unit,
    keyBoardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                unfocusedBorderColor = OutlineColor,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
        )
    }
}