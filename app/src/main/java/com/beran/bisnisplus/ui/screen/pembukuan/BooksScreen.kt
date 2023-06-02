@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.beran.bisnisplus.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.screen.pembukuan.BooksState
import com.beran.bisnisplus.ui.screen.pembukuan.component.BookCardSection
import com.beran.bisnisplus.ui.screen.pembukuan.component.BookTabsSection
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterialApi::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BooksScreen(
    state: BooksState,
    snackbarHostState: SnackbarHostState,
    fetchListBook: () -> Unit,
    selectedDate: String,
    onDateChange: (String) -> Unit,
    onDownloadReport: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onNavigateToCreate: () -> Unit,
    deleteBook: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomSheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var isLoading by remember {
        mutableStateOf(state.isLoading)
    }
    var error by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = state.isLoading) {
        isLoading = state.isLoading
        if (state.isLoading) {
            fetchListBook()
        }
    }
    /**
     * https://stackoverflow.com/a/71223728/18219793
     * this is reference for create temp file to open the file was created
     */
//    val permissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
//    val intent = Intent(Intent.ACTION_VIEW).apply {
//        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//        data = Uri.parse(state.uri.path)
//        setDataAndType(state.uri, "text/csv")
//    }
//    val openFileLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult(),
//        onResult = {
//        }
//    )
//    LaunchedEffect(key1 = state.isSuccess) {
//        if (state.uri != Uri.EMPTY){
//            if (permissionState.hasPermission){
//                openFileLauncher.launch(intent)
//            }else{
//                permissionState.launchPermissionRequest()
//            }
//        }
//    }
    LaunchedEffect(key1 = state.error?.isNotEmpty()) {
        if (state.error?.isNotEmpty() == true) {
            error = "Terjadi Kesalahan, Coba lagi!"
            snackbarHostState.showSnackbar(error.orEmpty())
        }
    }

    ModalBottomSheetLayout(sheetState = bottomSheetState, sheetContent = {
        BottomSheet(
            onDownloadReport = onDownloadReport,
            bottomSheetState = bottomSheetState
        )
    }) {
        Box(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                BookCardSection(
                    isLoading = state.isLoading,
                    selected = selectedDate,
                    onChangeSelected = onDateChange,
                    incomeAmount = state.incomeAmount,
                    expenseAmount = state.expenseAmount
                )
                BookTabsSection(
                    isLoading = isLoading,
                    expenseList = state.expenseBooks,
                    incomeList = state.incomeBooks,
                    onNavigateToEdit = onNavigateToEdit,
                    deleteBook = deleteBook
                )

            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp, end = 24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                if (state.user?.bisnisId == null) scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Lengkapi profile terlebih dahulu!"
                                    )
                                } else bottomSheetState.show()
                            }
                        },
                        shape = CircleShape, containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.IosShare,
                            contentDescription = "Melihat Laporan Keuangan",
                            tint = Color.White
                        )
                    }
                    FloatingActionButton(
                        onClick = {
                            if (state.user?.bisnisId == null) {
                                scope.launch { snackbarHostState.showSnackbar("Lengkapi profile terlebih dahulu!") }
                            } else {
                                onNavigateToCreate()
                            }
                        },
                        shape = CircleShape, containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Melihat Laporan Keuangan",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheet(
    onDownloadReport: () -> Unit,
    bottomSheetState: ModalBottomSheetState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Unduh laporan anda",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Divider()
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Laporan anda akan diunduh dalam format csv",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onDownloadReport()
                        scope.launch { if (bottomSheetState.isVisible) bottomSheetState.hide() }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Unduh")
                }
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
            state = BooksState(),
            snackbarHostState = SnackbarHostState(),
            onDownloadReport = {},
            fetchListBook = {},
            onNavigateToEdit = {},
            deleteBook = {},
            selectedDate = "",
            onDateChange = {},
            onNavigateToCreate = {},
        )
    }
}