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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.component.EmptyView
import com.beran.bisnisplus.ui.component.ErrorView
import com.beran.bisnisplus.ui.navigation.MainScreen
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.pembukuan.BookStates
import com.beran.bisnisplus.ui.screen.pembukuan.component.BookTabsSection
import com.beran.bisnisplus.ui.screen.pembukuan.component.BookTipsCardSection
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.core.common.Constant
import com.beran.core.domain.model.BookModel
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BooksScreen(
    state: BookStates<List<BookModel>>,
    uiState: BookStates<Unit>,
    fetchListBook: () -> Unit,
    onNavigateToCreateBook: () -> Unit,
    onNavigateToLaporanScreen: (String) -> Unit,
    onNavigateToEdit: (String) -> Unit,
    deleteBook: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(floatingActionButton = {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Button(
                onClick = { onNavigateToLaporanScreen(MainScreen.FinancialStatement.route) },
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
            Box {
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        BookTipsCardSection(onNavigateToLaporanScreen)
                        when (state) {
                            is BookStates.Loading -> fetchListBook()
                            is BookStates.Error -> {
                                ErrorView(errorText = state.message)
                            }

                            is BookStates.Success -> {
                                Timber.tag("BookScreen").i("${state.data}")
                                if (state.data.isEmpty()) {
                                    EmptyView(hintText = "Pembukuan anda masih kosong")
                                } else {
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
                                    BookTabsSection(
                                        expenseList = groupedExpense,
                                        incomeList = groupedIncome,
                                        onNavigateToEdit = onNavigateToEdit,
                                        deleteBook = deleteBook
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun BooksContent(
//    incomeList: List<Pair<Long?, List<BookModel>>>,
//    expenseList: List<Pair<Long?, List<BookModel>>>,
//    onNavigateToCreateBook: () -> Unit,
//    onNavigateToLaporanScreen: (String) -> Unit,
//    onNavigateToEdit: (String) -> Unit,
//    deleteBook: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//
//    Scaffold(floatingActionButton = {
//        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
//            Button(
//                onClick = { onNavigateToLaporanScreen(Screen.FinancialStatement.route) },
//                shape = CircleShape
//            ) {
//                Icon(
//                    imageVector = Icons.Default.IosShare,
//                    contentDescription = "Melihat Laporan Keuangan"
//                )
//            }
//            Button(onClick = onNavigateToCreateBook, shape = CircleShape) {
//                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Buat pembukuan")
//            }
//        }
//    }) { innerPadding ->
//        Box(modifier = modifier.padding(innerPadding)) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 16.dp)
//            ) {
//                BookTipsCardSection(onNavigateToLaporanScreen)
//                BookTabsSection(
//                    expenseList = expenseList,
//                    incomeList = incomeList,
//                    onNavigateToEdit = onNavigateToEdit,
//                    deleteBook = deleteBook
//                )
//            }
//        }
//    }
//}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PembukuanScreenPrev() {
    BisnisPlusTheme {
        BooksScreen(
            state = BookStates.Loading,
            uiState = BookStates.Loading,
            onNavigateToCreateBook = {},
            onNavigateToLaporanScreen = {},
            fetchListBook = {},
            onNavigateToEdit = {},
            deleteBook = {}
        )
    }
}