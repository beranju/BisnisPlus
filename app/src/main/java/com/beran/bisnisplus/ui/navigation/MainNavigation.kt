package com.beran.bisnisplus.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.beran.bisnisplus.ui.component.BottomBar
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.screen.BooksScreen
import com.beran.bisnisplus.ui.screen.HomeScreen
import com.beran.bisnisplus.ui.screen.PembayaranScreen
import com.beran.bisnisplus.ui.screen.SettingScreen
import com.beran.bisnisplus.ui.screen.StatistikScreen
import com.beran.bisnisplus.ui.screen.home.HomeViewModel
import com.beran.bisnisplus.ui.screen.notification.NotificationScreen
import com.beran.bisnisplus.ui.screen.pembayaran.CreateNewPaymentScreen
import com.beran.bisnisplus.ui.screen.pembukuan.BookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.CreateNewRecordScreen
import com.beran.bisnisplus.ui.screen.pembukuan.EditBookRecordScreen
import com.beran.bisnisplus.ui.screen.pembukuan.component.FinancialStatementScreen
import com.beran.bisnisplus.ui.screen.pembukuan.create.CreateBookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.edit.EditBookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.list.SortedBookScreen
import com.beran.bisnisplus.ui.screen.pembukuan.report.FinancialReportViewModel
import com.beran.bisnisplus.ui.screen.setting.EditProfileUserScreen
import com.beran.bisnisplus.ui.screen.setting.common.SettingViewModel
import com.beran.bisnisplus.ui.screen.statistic.StatisticViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    navController: NavHostController,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    logOut: () -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination?.route
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            if (currentDestination == MainScreen.Home.route || currentDestination == MainScreen.Pembukuan.route || currentDestination == MainScreen.Statistik.route || currentDestination == MainScreen.Pembayaran.route || currentDestination == MainScreen.Setting.route) {
                CustomAppBar(
                    titleAppBar = when (currentDestination) {
                        MainScreen.Home.route -> "Bisnis Plus"
                        MainScreen.Pembukuan.route -> "Pembukuan"
                        MainScreen.Statistik.route -> "Statistik"
                        MainScreen.Pembayaran.route -> "Pembayaran"
                        MainScreen.Setting.route -> "Setting"
                        else -> ""
                    },
                    showTrailingIcon = true,
                    onTrailingClick = { navController.navigate(MainScreen.Notification.route) }
                )
            }
        },
        bottomBar = {
            if (currentDestination == MainScreen.Home.route || currentDestination == MainScreen.Pembukuan.route || currentDestination == MainScreen.Statistik.route || currentDestination == MainScreen.Pembayaran.route || currentDestination == MainScreen.Setting.route) {
                BottomBar(navController = navController)
            }
        }

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MainScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = MainScreen.Home.route) {
                val viewModel = koinViewModel<HomeViewModel>()
                val scope = rememberCoroutineScope()
                val state = viewModel.state.value
                HomeScreen(
                    state = state,
                    scaffoldState = scaffoldState,
                    onNavigateToCreateBook = {
                        navController.navigate(MainScreen.CreateNewRecord.route)
                    },
                    onNavigateToProfile = {
                        navController.navigate(MainScreen.Setting.route)
                    },
                    fetching = {
                        // ** menjalan fungsi secara bersamaan
                        scope.launch {
                            val fetchUserDeferred = async { viewModel.fetchUser() }
                            val fetchBookDeferred = async { viewModel.fetchBook() }
                            awaitAll(fetchUserDeferred, fetchBookDeferred)
                        }
                    },
                    onNavigateToStatistic = {
                        navController.navigate(MainScreen.Statistik.route)
                    },
                    onNavigateToEdit = { id ->
                        navController.navigate(MainScreen.EditBookRecord.createRoute(id))
                    },
                    deleteBook = { id ->
                        viewModel.deleteBook(id)
                    },
                    onNavigateToDebt = {
                        navController.navigate(MainScreen.SortedBook.route)
                    },
                    checkNetworkAvailable = {
                        viewModel.checkNetworkAvailable(it)
                    }
                )
            }
            composable(route = MainScreen.Pembukuan.route) {
                val viewModel = koinViewModel<BookViewModel>()
                val state = viewModel.listBook.collectAsStateWithLifecycle().value
                val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                BooksScreen(
                    state = state,
                    uiState = uiState,
                    onNavigateToCreateBook = { navController.navigate(MainScreen.CreateNewRecord.route) },
                    onNavigateToLaporanScreen = { route ->
                        navController.navigate(route)
                    },
                    fetchListBook = {
                        viewModel.fetchBooks()
                    },
                    onNavigateToEdit = { bookId ->
                        navController.navigate(MainScreen.EditBookRecord.createRoute(bookId = bookId))
                    },
                    deleteBook = {
                        viewModel.deleteBook(it)
                    }
                )
            }
            composable(
                route = MainScreen.EditBookRecord.route,
                arguments = listOf(navArgument("bookId") { type = NavType.StringType })
            ) {
                val bookId = it.arguments?.getString("bookId")
                val viewModel = koinViewModel<EditBookViewModel>()
                val state = viewModel.book.collectAsStateWithLifecycle().value
                val updateBookState = viewModel.uiState.collectAsStateWithLifecycle().value
                EditBookRecordScreen(
                    state = state,
                    updateBookState = updateBookState,
                    bookId = bookId.orEmpty(),
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onUpdateBook = { book ->
                        viewModel.updateBook(book)
                    },
                    getBookById = {
                        viewModel.fetchBookById(bookId!!)
                    },
                )
            }
            composable(route = MainScreen.SortedBook.route){
                SortedBookScreen()
            }
            composable(route = MainScreen.Statistik.route) {
                val viewmodel = koinViewModel<StatisticViewModel>()
                val state = viewmodel.state.value
                StatistikScreen(
                    state = state,
                    fetchData = { viewmodel.fetchData() }
                )
            }
            composable(route = MainScreen.Pembayaran.route) {
                PembayaranScreen(
                    onNavigateToCreateNewPayment = {
                        navController.navigate(MainScreen.CreateNewPayment.route)
                    }
                )
            }
            composable(route = MainScreen.Setting.route) {
                val viewModel = koinViewModel<SettingViewModel>()
                val state = viewModel.user.collectAsStateWithLifecycle().value
                SettingScreen(
                    state = state,
                    onNavigateToEditProfile = {
                        navController.navigate(MainScreen.EditProfileUser.route)
                    },
                    fetchUserData = {
                        viewModel.fetchCurrentUser()
                    },
                    signOut = logOut
                )
            }

            composable(route = MainScreen.EditProfileUser.route) {
                val viewModel = koinViewModel<SettingViewModel>()
                val state = viewModel.uiState.collectAsStateWithLifecycle().value
                val userState = viewModel.user.collectAsStateWithLifecycle().value
                EditProfileUserScreen(
                    userState = userState,
                    state = state,
                    onUpdateProfile = { userModel ->
                        viewModel.updateProfile(userModel)
                    },
                    onNavigateBack = { navController.navigateUp() },
                    fetchUserDetail = { viewModel.fetchCurrentUser() }
                )
            }
            composable(route = MainScreen.CreateNewRecord.route) {
                val viewModel = koinViewModel<CreateBookViewModel>()
                val state = viewModel.uiState.collectAsStateWithLifecycle().value
                CreateNewRecordScreen(
                    state = state,
                    onCreateNewBook = { bookModel ->
                        viewModel.createNewBook(bookModel)
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                )
            }
            composable(route = MainScreen.CreateNewPayment.route) {
                CreateNewPaymentScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(route = MainScreen.FinancialStatement.route) {
                val financialViewModel = koinViewModel<FinancialReportViewModel>()
                val state = financialViewModel.listBook.collectAsStateWithLifecycle().value
                val uiState = financialViewModel.iuState.collectAsStateWithLifecycle().value
                FinancialStatementScreen(
                    selectedDate = financialViewModel.selectedDate.value,
                    state = state, uiState = uiState,
                    fetchListBook = {
                        financialViewModel.fetchBooks()
                    },
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onDateChange = { value ->
                        financialViewModel.onselectedChange(value)
                    },
                    exportDataIntoCsv = { filePath ->
                        financialViewModel.exportDataIntoCsv(filePath)
                    }
                )
            }
            composable(
                route = MainScreen.EditBookRecord.route,
                arguments = listOf(navArgument("bookId") { type = NavType.StringType })
            ) {
                val bookId = it.arguments?.getString("bookId")
                val viewModel = koinViewModel<EditBookViewModel>()
                val state = viewModel.book.collectAsStateWithLifecycle().value
                val updateBookState = viewModel.uiState.collectAsStateWithLifecycle().value
                EditBookRecordScreen(
                    state = state,
                    updateBookState = updateBookState,
                    bookId = bookId.orEmpty(),
                    onNavigateBack = {
                        navController.navigateUp()
                    },
                    onUpdateBook = { book ->
                        viewModel.updateBook(book)
                    },
                    getBookById = {
                        viewModel.fetchBookById(bookId!!)
                    },
                )
            }
            composable(MainScreen.Notification.route) {
                NotificationScreen()
            }
        }
    }
}