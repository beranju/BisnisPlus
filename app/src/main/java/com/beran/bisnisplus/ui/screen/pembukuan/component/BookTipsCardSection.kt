package com.beran.bisnisplus.ui.screen.pembukuan.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.navigation.Screen

@Composable
fun BookTipsCardSection(
    onNavigateToLaporanScreen: (String) -> Unit, modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        append("Anda dapat melihat laporan keuangan melalui tombol export dibawah atau tekan disini ")
        pushStringAnnotation(tag = Screen.FinancialStatement.route, annotation = "laporanKeuangan")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onTertiaryContainer)) {
            append("Lihat laporan Keuangan")
        }
        pop()
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        modifier = modifier.fillMaxWidth()
    ) {
        ClickableText(
            text = annotatedText, onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = Screen.FinancialStatement.route,
                    start = offset,
                    end = offset
                )
                    .firstOrNull()?.let { annotation ->
                        onNavigateToLaporanScreen(annotation.item)
                    }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}