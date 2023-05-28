package com.beran.bisnisplus.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.ErrorView
import com.beran.bisnisplus.ui.component.FiturCepatCard
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.bisnisplus.ui.screen.home.common.HomeStates
import com.beran.bisnisplus.ui.screen.home.common.PieChartView
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import com.beran.core.domain.model.BookModel
import com.github.mikephil.charting.data.PieEntry

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    state: HomeStates,
    onNavigateToCreateBook: () -> Unit,
    fetchAllBooks: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = state.isLoading) {
        if (state.isLoading) fetchAllBooks()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        WelcomeDateCard()
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            if (state.pieEntry.isNotEmpty()) {
                HomeMainCard(state.pieEntry)
            }
            FiturCepatSection(onNavigateToCreateBook = onNavigateToCreateBook)
            if (state.isLoading) {
                fetchAllBooks()
                CircularProgressIndicator()
            } else if (state.error?.isNotEmpty() == true) {
                ErrorView(errorText = state.error)
            } else {
                PembukuanSection(listBook = state.listBook)
            }
        }
    }
}

@Composable
fun WelcomeDateCard(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        Text(
            text = "Hai Juragan",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        Column {
            Text(text = "Senin", style = MaterialTheme.typography.labelMedium)
            Text(text = "29 Mei 2023", style = MaterialTheme.typography.bodySmall)
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PembukuanSection(
    listBook: List<BookModel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Terbaru",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .height(150.dp)
                .padding(4.dp)
        ) {
            items(listBook, key = { it.bookId.orEmpty() }) { item ->
                PembukuanCard(
                    judulBuku = item.category.orEmpty(),
                    amount = Utils.rupiahFormatter(item.amount.toLong()),
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
fun HomeMainCard(dataEntry: List<PieEntry>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                hoveredElevation = 0.dp
            ), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ), modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                HomeProfileCard(
                    photoUrl = "",
                    username = "beranju",
                    phoneNumber = "08236485906",
                    job = "Agen cabai"
                )
                Divider()
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Pemasukan", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Semua pemasukan",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    TextButton(onClick = {}) {
                        Text(text = "detail", style = MaterialTheme.typography.bodySmall)
                        Icon(
                            imageVector = Icons.Outlined.NavigateNext, contentDescription = null
                        )
                    }
                }
                HomeChartSection(dataEntry)
            }
        }
    }
}

@Composable
fun HomeChartSection(
    data: List<PieEntry>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AndroidView(factory = {
            PieChartView(context = it).apply {
                setData(data)
            }
        }, modifier = Modifier.size(180.dp))
    }
}

@Composable
fun HomeProfileCard(
    photoUrl: String,
    username: String,
    phoneNumber: String,
    job: String,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        AsyncImage(
            model = photoUrl, contentDescription = "Photo profile",
            placeholder = painterResource(id = R.drawable.img_empty_profile),
            error = painterResource(id = R.drawable.img_empty_profile),
            modifier = Modifier.size(80.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            Text(text = username, style = MaterialTheme.typography.titleSmall)
            Text(text = phoneNumber, style = MaterialTheme.typography.bodySmall)
            Box(modifier = Modifier.padding(top = 4.dp)) {
                Text(text = job, style = MaterialTheme.typography.labelSmall)
            }
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
        )
    }
}