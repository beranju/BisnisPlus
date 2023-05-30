package com.beran.bisnisplus.ui.screen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.screen.home.common.FiturCepatSection
import com.beran.bisnisplus.ui.screen.home.common.HomeMainCard
import com.beran.bisnisplus.ui.screen.home.common.HomeStates
import com.beran.bisnisplus.ui.screen.home.common.PembukuanSection
import com.beran.bisnisplus.ui.screen.home.common.ScrollableContent
import com.beran.bisnisplus.ui.screen.home.common.WelcomeDateCard
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    state: HomeStates,
    scaffoldState: ScaffoldState,
    onNavigateToCreateBook: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToStatistic: () -> Unit,
    onNavigateToDebt: () -> Unit,
    deleteBook: (String) -> Unit,
    onNavigateToEdit: (String) -> Unit,
    checkNetworkAvailable: (Context) -> Boolean,
    fetching: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val isNetworkAvailable by remember {
        mutableStateOf(checkNetworkAvailable(context))
    }
    var wasConnectedToInternet by remember {
        mutableStateOf(isNetworkAvailable)
    }
    val scope = rememberCoroutineScope()
    val date = Utils.convertToDate(System.currentTimeMillis())
    var isLoading by remember {
        mutableStateOf(state.isLoading)
    }
    var isSuccess by remember {
        mutableStateOf(state.isSuccess)
    }
    var error by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(key1 = state.error?.isNotEmpty()) {
        error = state.error
        if (error?.isNotEmpty() == true) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Terjadi kesalahan, coba lagi!"
                )
            }
        }
    }
    LaunchedEffect(key1 = state.isSuccess) {
        isSuccess = state.isSuccess
        if (isSuccess) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Buku berhasil di hapus"
                )
            }
        }
    }
    LaunchedEffect(key1 = state.isLoading) {
        if (state.isLoading) {
            fetching()
        }
        isLoading = state.isLoading
    }
    LaunchedEffect(key1 = isNetworkAvailable) {
        if (!wasConnectedToInternet && isNetworkAvailable) {
            fetching()
        }
        if (!isNetworkAvailable) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Anda tidak memiliki koneksi internet"
                )
            }
        }
        wasConnectedToInternet = isNetworkAvailable
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        WelcomeDateCard(date = date)
        ScrollableContent(isLoading, scrollState) {
            HomeMainCard(
                state.pieEntry,
                state.user,
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToStatistic = onNavigateToStatistic
            )
            FiturCepatSection(
                onNavigateToCreateBook = onNavigateToCreateBook,
                onNavigateToStatistic = onNavigateToStatistic,
                onNavigateToDebt = onNavigateToDebt
            )
            PembukuanSection(
                listBook = state.listBook,
                deleteBook = deleteBook,
                onNavigateToEdit = onNavigateToEdit
            )
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
            scaffoldState = rememberScaffoldState(),
            onNavigateToCreateBook = {},
            onNavigateToProfile = {},
            onNavigateToStatistic = {},
            fetching = {},
            deleteBook = {},
            onNavigateToEdit = {},
            onNavigateToDebt = {},
            checkNetworkAvailable = { true },
        )
    }
}