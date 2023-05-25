package com.beran.bisnisplus.ui.screen.pembukuan.report

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.bisnisplus.constant.TimeRange
import com.beran.bisnisplus.ui.screen.pembukuan.BookStates
import com.beran.bisnisplus.utils.toEpochMilli
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class FinancialReportViewModel(private val useCase: BookUseCase) : ViewModel() {
    private var _selectedDate = mutableStateOf(TimeRange.Today.range)
    val selectedDate get() = _selectedDate

    private var _uiState: MutableStateFlow<BookStates<Unit>> =
        MutableStateFlow(BookStates.Loading)
    val iuState get() = _uiState.asStateFlow()

    private var _listBook: MutableStateFlow<BookStates<List<BookModel>>> =
        MutableStateFlow(BookStates.Loading)
    val listBook get() = _listBook.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchBooks() {
        viewModelScope.launch {
            useCase.fetchAllBook().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val list = result.data
                        val filteredList: List<BookModel> = when (selectedDate.value) {
                            TimeRange.Today.range -> {
                                list.filter { it.createdAt == LocalDate.now().toEpochMilli() }
                            }

                            TimeRange.Yesterday.range -> {
                                list.filter {
                                    it.createdAt == LocalDate.now().minusDays(1).toEpochMilli()
                                }
                            }

                            TimeRange.Week.range -> {
                                val daysOfWeek = DayOfWeek.values()
                                val epochDayOfWeek = daysOfWeek.map { day ->
                                    LocalDate.now().with(day).toEpochMilli()
                                }
                                list.filter { it.createdAt in epochDayOfWeek }
                            }

                            TimeRange.Month.range -> {
                                val currentYearMonth = YearMonth.now()
                                val firstDayOfMonth =
                                    LocalDate.of(currentYearMonth.year, currentYearMonth.month, 1)
                                        .toEpochMilli()
                                val lastDayOfMonth = LocalDate.of(
                                    currentYearMonth.year,
                                    currentYearMonth.month,
                                    currentYearMonth.lengthOfMonth()
                                ).toEpochMilli()
                                list.filter { it.createdAt in firstDayOfMonth..lastDayOfMonth }
                            }

                            TimeRange.Custom.range -> {
                                emptyList<BookModel>()
                            }

                            else -> {
                                list.filter { it.createdAt == LocalDate.now().toEpochMilli() }
                            }
                        }
                        _listBook.value = BookStates.Success(filteredList)
                    }

                    is Resource.Error -> _listBook.value = BookStates.Error(result.message)
                    is Resource.Loading -> _listBook.value = BookStates.Loading
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onselectedChange(time: String) {
        _selectedDate.value = time
        fetchBooks()
    }

    fun exportDataIntoCsv(filePath: String) {
        viewModelScope.launch {
            useCase.exportDataIntoCsv(filePath).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = BookStates.Loading
                    is Resource.Error -> _uiState.value = BookStates.Error(result.message)
                    is Resource.Success -> _uiState.value = BookStates.Success(result.data)
                }
            }
        }
    }
}