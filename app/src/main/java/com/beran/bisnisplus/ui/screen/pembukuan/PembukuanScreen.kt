package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.pembukuan.TabContentScreen
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PembukuanScreen(
    onNavigateToCreateBook: () -> Unit,
    onNavigateToLaporanScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(floatingActionButton = {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Button(onClick = { onNavigateToLaporanScreen(Screen.FinancialStatement.route) }, shape = CircleShape) {
                Icon(
                    imageVector = Icons.Default.IosShare,
                    contentDescription = "Melihat Laporan Keuangan"
                )
            }
            Button(onClick = onNavigateToCreateBook, shape = CircleShape) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Buat pembukuan")
            }
        }
    }) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                TipsCardSection(onNavigateToLaporanScreen)
                TabsSection()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsSection(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()

    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        Tabs(pagerState = pagerState)
        TabsContent(pagerState = pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsContent(
    pagerState: PagerState, modifier: Modifier = Modifier
) {
    HorizontalPager(pageCount = 2, state = pagerState, modifier = modifier) { page ->
        when (page) {
            0 -> TabContentScreen(title = "Tab content pemasukan")
            1 -> TabContentScreen(title = "Tab content pengeluaran")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Tabs(
    pagerState: PagerState, modifier: Modifier = Modifier
) {
    val listTabs = listOf(
        "Pemasukan", "Pengeluaran"
    )
    val coroutineScope = rememberCoroutineScope()
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
    ) {
        listTabs.forEachIndexed { index, _ ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp)
                    .clip(
                        if (pagerState.currentPage == index) RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp
                        ) else RoundedCornerShape(10.dp)
                    )
                    .background(if (listTabs[index] == "Pemasukan") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(
                    imageVector = if (pagerState.currentPage == index) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = "radio button",
                    tint = if (listTabs[index] == "Pembukuan") MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
                Text(text = listTabs[index])
            }

        }
    }
}

@Composable
private fun TipsCardSection(
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


@Preview(showBackground = true)
@Composable
fun PembukuanScreenPrev() {
    BisnisPlusTheme {
        PembukuanScreen(onNavigateToCreateBook = {}, onNavigateToLaporanScreen = {})
    }
}