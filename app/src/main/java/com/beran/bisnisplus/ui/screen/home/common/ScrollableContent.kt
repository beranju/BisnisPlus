package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableContent(
    loading: Boolean,
    scrollState: ScrollState,
    content: @Composable() () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        if (loading) {
            HomeMainCardShimmer()
            FiturCepatSection(
                onNavigateToCreateBook = {},
                onNavigateToDebt = {},
                onNavigateToStatistic = {})
            PembukuanSectionShimmer()
        } else {
            content()
        }
    }

}