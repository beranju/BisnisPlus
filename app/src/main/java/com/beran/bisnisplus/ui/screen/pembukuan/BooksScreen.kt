package com.beran.bisnisplus.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.pembukuan.BookState
import com.beran.bisnisplus.ui.screen.pembukuan.component.BookTabsSection
import com.beran.bisnisplus.ui.screen.pembukuan.component.BookTipsCardSection
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.core.common.Constant
import com.beran.core.domain.model.BookModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BooksScreen(
    state: BookState<List<BookModel>>,
    fetchListBook: () -> Unit,
    resetState: () -> Unit,
    onNavigateToCreateBook: () -> Unit,
    onNavigateToLaporanScreen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var incomeList by remember {
        mutableStateOf<List<Pair<Long?, List<BookModel>>>>(listOf())
    }
    var expenseList by remember {
        mutableStateOf<List<Pair<Long?, List<BookModel>>>>(listOf())
    }
    var loading by remember {
        mutableStateOf(false)
    }
    var errorText by remember {
        mutableStateOf<String?>(null)
    }
    DisposableEffect(key1 = state) {
        when (state) {
            is BookState.Initial -> fetchListBook()
            is BookState.Loading -> loading = true
            is BookState.Error -> errorText = state.message
            is BookState.Success -> {
                // ** filtered book by type and grouped it by createdAt
                val groupedIncome = state.data.filter {
                    it.type == Constant.Income
                }.groupBy {
                    it.createdAt
                }.toList()

                val groupedExpense = state.data.filter {
                    it.type == Constant.Expense
                }.groupBy {
                    it.createdAt
                }.toList()
                incomeList = groupedIncome
                expenseList = groupedExpense
            }
        }
        onDispose { resetState() }
    }
    Scaffold(floatingActionButton = {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Button(
                onClick = { onNavigateToLaporanScreen(Screen.FinancialStatement.route) },
                shape = CircleShape
            ) {
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
                BookTipsCardSection(onNavigateToLaporanScreen)
                BookTabsSection(expenseList = expenseList, incomeList = incomeList)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PembukuanScreenPrev() {
    BisnisPlusTheme {
        BooksScreen(
            state = BookState.Initial,
            onNavigateToCreateBook = {},
            onNavigateToLaporanScreen = {},
            fetchListBook = {},
            resetState = {})
    }
}