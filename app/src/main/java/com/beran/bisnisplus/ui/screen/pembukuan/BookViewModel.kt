package com.beran.bisnisplus.ui.screen.pembukuan

import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.bisnisplus.constant.BookTypes
import com.beran.bisnisplus.constant.TimeRange
import com.beran.bisnisplus.utils.toEpochMilli
import com.beran.core.common.Constant
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.AuthUseCase
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date

class BookViewModel(private val useCase: BookUseCase, private val authUseCase: AuthUseCase) :
    ViewModel() {

    private var _selectedDate: MutableState<String> = mutableStateOf(TimeRange.Week.range)
    val selectedDate: State<String> get() = _selectedDate

    private var _state: MutableState<BooksState> = mutableStateOf(BooksState())
    val state: State<BooksState> get() = _state

    init {
//        fetchBooks()
        fetch()
        fetchCurrentUser()
    }

    fun fetchCurrentUser(){
        viewModelScope.launch {
            authUseCase.userDetail().collect{result ->
                when(result){
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> _state.value =
                        _state.value.copy(isLoading = false, user = result.data)
                }
            }
        }
    }

    fun fetch() {
        viewModelScope.launch {
            when (selectedDate.value) {
                TimeRange.Today.range -> fetchBookByDate(
                    generateCurrentDate().first,
                    generateCurrentDate().second
                )

                TimeRange.Yesterday.range -> fetchBookByDate(
                    generateYesterdayDate().first,
                    generateYesterdayDate().second
                )

                TimeRange.Week.range -> fetchBookByDate(
                    generateWeekDate().first,
                    generateWeekDate().second
                )

                TimeRange.Month.range -> fetchBookByDate(
                    generateMonthDate().first,
                    generateMonthDate().second
                )
            }
        }
    }

    private fun fetchBookByDate(firstDate: Long, lastDate: Long) {
        viewModelScope.launch {
            useCase.fetchBookByDates(firstDate, lastDate).collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> {
                        val filteredList = result.data
                        val incomeAmount = countTotal(filteredList, BookTypes.Pemasukan.string)
                        val expenseAmount = countTotal(filteredList, BookTypes.Pengeluaran.string)

                        // ** grouped the data by it date
                        val groupedIncome = groupedList(filteredList, Constant.Income)
                        val groupedExpense = groupedList(filteredList, Constant.Expense)

                        Timber.tag("BookViewmodel").i("$filteredList")
                        _state.value = _state.value.copy(
                            isLoading = false,
                            books = filteredList,
                            incomeBooks = groupedIncome,
                            expenseBooks = groupedExpense,
                            incomeAmount = incomeAmount,
                            expenseAmount = expenseAmount
                        )
                    }
                }
            }

        }
    }

    private fun groupedList(
        filteredList: List<BookModel>,
        type: String
    ): List<Pair<Long?, List<BookModel>>> {
        return filteredList.filter {
            it.type == type
        }.groupBy {
            it.createdAt
        }.toList()
    }

    private fun countTotal(filteredList: List<BookModel>, type: String): Double {
        return filteredList.filter { it.type == type }
            .sumOf { it.amount }
    }

//    private fun filterBooksByDate(listBooks: List<BookModel>, value: String): List<BookModel> {
//        return when (value) {
//            TimeRange.Today.range -> listBooks.filter { it.createdAt == generateCurrentDate() }
//            TimeRange.Yesterday.range -> listBooks.filter { it.createdAt == generateYesterdayDate() }
//            TimeRange.Week.range -> listBooks.filter { it.createdAt in generateWeekDate() }
//            TimeRange.Month.range -> listBooks.filter { it.createdAt in generateMonthDate() }
//            else -> listBooks
//        }
//    }

    private fun generateMonthDate(): Pair<Long, Long> {
        val listDate = mutableListOf<Long>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentYearMonth = YearMonth.now()
            val firstDayOfMonth = LocalDate.of(currentYearMonth.year, currentYearMonth.month, 1)
                .toEpochMilli()
            val lastDayOfMonth = LocalDate.of(
                currentYearMonth.year,
                currentYearMonth.month,
                currentYearMonth.lengthOfMonth()
            ).toEpochMilli()
//            for (i in firstDayOfMonth..lastDayOfMonth) {
//                listDate.add(i)
//            }
            val pairDate: Pair<Long, Long> = Pair(firstDayOfMonth, lastDayOfMonth)
            pairDate
        } else {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val currentMonth = calendar.get(Calendar.MONTH)
            val firstDate = calendar.timeInMillis

            while (calendar.get(Calendar.MONTH) == currentMonth) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val lastDate = calendar.timeInMillis
            val pairDate: Pair<Long, Long> = Pair(firstDate, lastDate)
            pairDate
        }
    }

    private fun generateWeekDate(): Pair<Long, Long> {
        val firstDay: Long
        val lastDay: Long

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDate.now()
            val firstDayOfWeek = today.with(DayOfWeek.MONDAY)
            val lastDayOfWeek = today.with(DayOfWeek.SUNDAY)
            firstDay = firstDayOfWeek.toEpochMilli()
            lastDay = lastDayOfWeek.toEpochMilli()
        } else {
            val calendar = Calendar.getInstance()
            val epochList = mutableListOf<Long>()
            val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val daysToMonday = (currentDayOfWeek + 7 - Calendar.MONDAY) % 7

            calendar.add(Calendar.DAY_OF_WEEK, -daysToMonday)
            firstDay = calendar.timeInMillis

            calendar.add(Calendar.DAY_OF_WEEK, 6)
            lastDay = calendar.timeInMillis
        }

        return Pair(firstDay, lastDay)
    }

    private fun generateYesterdayDate(): Pair<Long, Long> {
        val firstDay: Long
        val lastDay: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            firstDay = LocalDate.now().minusDays(1).toEpochMilli()
            lastDay = LocalDate.now().minusDays(1).toEpochMilli()
        } else {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            firstDay = calendar.timeInMillis
            lastDay = calendar.timeInMillis
        }
        return Pair(firstDay, lastDay)
    }

    private fun generateCurrentDate(): Pair<Long, Long> {
        val firstDay: Long
        val lastDay: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            firstDay = LocalDate.now().toEpochMilli()
            lastDay = LocalDate.now().toEpochMilli()
        } else {
            firstDay = Date().time
            lastDay = Date().time
        }
        return Pair(firstDay, lastDay)
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            useCase.deleteBook(bookId = bookId).collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> _state.value =
                        _state.value.copy(isLoading = false, isSuccess = true)
                }
            }
        }
    }

    fun downloadReport() {
        viewModelScope.launch {
            useCase.exportDataIntoCsv().collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> _state.value =
                        _state.value.copy(isLoading = false, isSuccess = true, uri = result.data)
                }
            }
        }
    }

    fun onChangeDate(time: String) {
        _selectedDate.value = time
        fetch()
    }

//    fun fetchBooks() {
//        viewModelScope.launch {
//            useCase.fetchAllBook().collect { result ->
//                when (result) {
//                    is Resource.Loading -> _state.value =
//                        _state.value.copy(isLoading = true, error = null)
//
//                    is Resource.Error -> _state.value =
//                        _state.value.copy(isLoading = false, error = result.message)
//
//                    is Resource.Success -> {
//                        val listBooks = result.data
//                        // ** filter the data by the date
//                        val filteredList: List<BookModel> =
//                            filterBooksByDate(listBooks, selectedDate.value)
//
//                        // ** sum the total amount by type of the data
//                        val incomeAmount =
//                            filteredList.filter { it.type == BookTypes.Pemasukan.string }
//                                .sumOf { it.amount }
//                        val expenseAmount =
//                            filteredList.filter { it.type == BookTypes.Pengeluaran.string }
//                                .sumOf { it.amount }
//
//                        // ** grouped the data by it date
//                        val groupedIncome = filteredList.filter {
//                            it.type == Constant.Income
//                        }.groupBy {
//                            it.createdAt
//                        }.toList()
//
//                        val groupedExpense = filteredList.filter {
//                            it.type == Constant.Expense
//                        }.groupBy {
//                            it.createdAt
//                        }.toList()
//
//                        Timber.tag("BookViewmodel").i("$filteredList")
//                        _state.value = _state.value.copy(
//                            isLoading = false,
//                            books = filteredList,
//                            incomeBooks = groupedIncome,
//                            expenseBooks = groupedExpense,
//                            incomeAmount = incomeAmount,
//                            expenseAmount = expenseAmount
//                        )
//                    }
//                }
//            }
//        }
//    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

