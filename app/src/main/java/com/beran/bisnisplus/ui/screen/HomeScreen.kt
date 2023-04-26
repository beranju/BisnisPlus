package com.beran.bisnisplus.ui.screen

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.data.listDummyBuku
import com.beran.bisnisplus.ui.component.FiturCepatCard
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.bisnisplus.ui.component.ProgressCard
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        HomeMainCard()
        FiturCepatSection()
        PembukuanSection()
        PembukuanSection()
    }
}

@Composable
fun PembukuanSection() {

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Pembukuan",
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(listDummyBuku) { item ->
                PembukuanCard(
                    judulBuku = item.judulBuku,
                    namaAgen = item.agen,
                    jenisBuku = item.kategori,
                    date = item.date
                )
            }
        }
    }
}

@Composable
fun FiturCepatSection() {
    Column {
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
            FiturCepatCard(title = "Buat Buku", icon = Icons.Outlined.StickyNote2)
            FiturCepatCard(title = "lihat Report", icon = Icons.Outlined.InsertChart)
            FiturCepatCard(title = "Buat Tagihan", icon = Icons.Outlined.CreditCard)
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPrev() {
    BisnisPlusTheme {
        HomeScreen()
    }
}