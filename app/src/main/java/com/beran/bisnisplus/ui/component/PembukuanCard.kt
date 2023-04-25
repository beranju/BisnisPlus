package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun PembukuanCard() {
    Column(
        modifier = Modifier
            .width(250.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(10.dp)
    ) {
        Text(
            text = "Barang Masuk",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp),
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Manahan Sihombing",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
        )
        Text(
            text = "Cabai Merah",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Kemarin",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp)
        )
        IconButton(onClick = {}, modifier = Modifier.align(Alignment.End)) {
            Icon(
                imageVector = Icons.Outlined.NavigateNext,
                contentDescription = "Navigate to detail"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PembukuanCardPrev() {
    BisnisPlusTheme {
        PembukuanCard()
    }
}