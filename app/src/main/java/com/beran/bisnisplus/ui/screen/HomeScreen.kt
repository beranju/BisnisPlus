package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.component.FiturCepatCard
import com.beran.bisnisplus.ui.component.PembukuanCard
import com.beran.bisnisplus.ui.component.ProgressCard
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        CustomAppBar(titleAppBar = "Bisnis Plus", showTrailingIcon = true)
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
                            contentDescription = "Navigate Next"
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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Fitur Cepat",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(
                    horizontal = 24.dp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FiturCepatCard(title = "Buat Buku", icon = Icons.Outlined.StickyNote2)
                FiturCepatCard(title = "lihat Report", icon = Icons.Outlined.InsertChart)
                FiturCepatCard(title = "Buat Tagihan", icon = Icons.Outlined.CreditCard)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pembukuan",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(
                    horizontal = 24.dp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .scrollable(
                        state = scrollState,
                        orientation = Orientation.Horizontal
                    ).padding(horizontal = 16.dp)
            ) {
                PembukuanCard()
                Spacer(modifier = Modifier.width(16.dp))
                PembukuanCard()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hutang",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(
                    horizontal = 24.dp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .scrollable(
                        state = scrollState,
                        orientation = Orientation.Horizontal
                    ).padding(horizontal = 16.dp)
            ) {
                PembukuanCard()
                Spacer(modifier = Modifier.width(16.dp))
                PembukuanCard()
            }
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