package com.beran.bisnisplus.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.component.EmptyView
import com.beran.bisnisplus.ui.component.ErrorView
import com.beran.bisnisplus.ui.component.FiturCepatCard
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.bisnisplus.ui.component.ProgressCard
import com.beran.bisnisplus.ui.screen.home.common.HomeState
import com.beran.bisnisplus.ui.screen.home.common.HomeStates
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.core.domain.model.BookModel
import com.beran.core.utils.Utils
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    state: HomeStates,
    bookState: HomeState<List<BookModel>>,
    onNavigateToCreateBook: () -> Unit,
    fetchAllBooks: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        HomeMainCard()
        FiturCepatSection(onNavigateToCreateBook = onNavigateToCreateBook)
        if (state.isLoading){
            fetchAllBooks()
            CircularProgressIndicator()
        }else if(state.error?.isNotEmpty() == true){
            ErrorView(errorText = state.error)
        }else{
            Timber.tag("homescreen").i("data: ${state.listBook}")
            PembukuanSection(listBook = state.listBook, title = "dataclass pembukuan")
        }
//        when (bookState) {
//            is HomeState.Loading -> {
//                fetchAllBooks()
//                Timber.tag("HomeScreen").i("loadinggg, fetching data")
//                EmptyView(hintText = "Mengambil data")
//            }
//
//            is HomeState.Success -> {
//                Timber.tag("HomeScreen").i("${bookState.data}")
//                PembukuanSection(
//                    listBook = bookState.data, title = "Sealed Pembukuan"
//                )
//            }
//
//            is HomeState.Error -> ErrorView(errorText = bookState.message)
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PembukuanSection(
    title: String,
    listBook: List<BookModel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(150.dp)
        ) {
            items(listBook, key = { it.bookId.orEmpty() }) { item ->
                PembukuanCard(
                    judulBuku = item.category.orEmpty(),
                    namaAgen = item.mitra.orEmpty(),
                    jenisBuku = item.type.orEmpty(),
                    date = Utils.convertToDate(item.createdAt ?: 0)
                )
            }
        }
    }
}

@Composable
fun FiturCepatSection(
    onNavigateToCreateBook: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Fitur Cepat",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            FiturCepatCard(
                title = "Buat Buku",
                icon = Icons.Outlined.StickyNote2,
                onNavigateToCreateBook = onNavigateToCreateBook
            )
            FiturCepatCard(
                title = "lihat Report",
                icon = Icons.Outlined.InsertChart,
                onNavigateToCreateBook = onNavigateToCreateBook
            )
            FiturCepatCard(
                title = "Buat Tagihan",
                icon = Icons.Outlined.CreditCard,
                onNavigateToCreateBook = onNavigateToCreateBook
            )
        }
    }
}

@Composable
fun HomeMainCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        Row {
            Text(
                text = "Hai Beranju",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Outlined.NavigateNext,
                contentDescription = "Navigate Next",
                modifier = Modifier.clickable { }
            )
        }
        Text(text = "Jumat, 12 April 2023", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProgressCard(value = "-40%", subTitle = "Hutang", upTrend = false)
            ProgressCard(value = "20%", subTitle = "Profit", upTrend = true)
            ProgressCard(value = "10%", subTitle = "Modal", upTrend = true)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
    BisnisPlusTheme {
        HomeScreen(
            state = HomeStates(),
            onNavigateToCreateBook = {},
            fetchAllBooks = {},
            bookState = HomeState.Loading
        )
    }
}