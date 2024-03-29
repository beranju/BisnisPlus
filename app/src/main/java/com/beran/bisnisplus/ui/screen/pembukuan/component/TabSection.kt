package com.beran.bisnisplus.ui.screen.pembukuan.component

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.constant.BookTypes
import com.beran.bisnisplus.ui.component.EmptyView
import com.beran.bisnisplus.ui.screen.pembukuan.TabContentScreen
import com.beran.core.domain.model.BookModel
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookTabsSection(
    incomeList: List<Pair<Long?, List<BookModel>>>,
    expenseList: List<Pair<Long?, List<BookModel>>>,
    onNavigateToEdit: (String) -> Unit,
    deleteBook: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()

    Column(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        Tabs(pagerState = pagerState)
        TabsContent(
            pagerState = pagerState,
            incomeList = incomeList,
            expenseList = expenseList,
            onNavigateToEdit = onNavigateToEdit,
            deleteBook = deleteBook
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsContent(
    incomeList: List<Pair<Long?, List<BookModel>>>,
    expenseList: List<Pair<Long?, List<BookModel>>>,
    onNavigateToEdit: (String) -> Unit,
    deleteBook: (String) -> Unit,
    pagerState: PagerState, modifier: Modifier = Modifier
) {
    HorizontalPager(pageCount = 2, state = pagerState) { page ->
        when (page) {
            0 -> {
                Box(modifier = modifier) {
                    if (incomeList.isEmpty()) {
                        EmptyView(hintText = "Mulai catat pemasukan anda")
                    } else {
                        TabContentScreen(
                            listBook = incomeList,
                            onNavigateToEdit = onNavigateToEdit,
                            deleteBook = deleteBook
                        )
                    }
                }
            }

            1 -> {
                Box(modifier = modifier) {
                    if (expenseList.isEmpty()) {
                        EmptyView(hintText = "Mulai catat pengeluaran anda")
                    } else {
                        TabContentScreen(
                            listBook = expenseList,
                            onNavigateToEdit = onNavigateToEdit,
                            deleteBook = deleteBook
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Tabs(
    pagerState: PagerState, modifier: Modifier = Modifier
) {
    val listTabs = BookTypes.values().toList().map { it.string }
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