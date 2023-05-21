package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun CustomTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    labelName: String,
    hintText: String,
    modifier: Modifier = Modifier,
    maxlines: Int = 3,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = labelName, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            minLines = maxlines,
            maxLines = maxlines,
            shape = RoundedCornerShape(10.dp), colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = hintText, style = MaterialTheme.typography.bodySmall)
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextAreaPrev() {
    BisnisPlusTheme {
        CustomTextArea(value = "", onValueChange = {}, "Catatan", "*Opsional")
    }
}