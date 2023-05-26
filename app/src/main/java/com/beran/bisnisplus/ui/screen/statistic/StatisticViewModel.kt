package com.beran.bisnisplus.ui.screen.statistic

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.bisnisplus.constant.BookTypes
import com.beran.core.common.Resource
import com.beran.core.domain.usecase.book.BookUseCase
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StatisticViewModel(private val bookUseCase: BookUseCase) : ViewModel() {
    private val _state = mutableStateOf(StatisticState())
    val state: State<StatisticState> get() = _state

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            bookUseCase.fetchAllBook().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        /**
                         * disini saya mengelompokkan data yang memiliki timestamp yang sama...
                         * ...kemudian saya melakukan loop untuk menmbahkan data kedalam listDataEntry
                         */
                        val listIncome =
                            result.data.filter { it.type == BookTypes.Pemasukan.string }
                        val listExpense =
                            result.data.filter { it.type == BookTypes.Pengeluaran.string }

                        val groupedIncome = listIncome.groupBy { it.createdAt }.toSortedMap(
                            compareBy { it }
                        )
                        val groupedExpense = listExpense.groupBy { it.createdAt }.toSortedMap(
                            compareBy { it })

                        val listDataIncome = groupedIncome.values.mapIndexed { index, data ->
                            Entry(index.toFloat(), data.sumOf { it.amount }.toFloat())
                        }

                        val listDataExpense = groupedExpense.values.mapIndexed { index, data ->
                            Entry(index.toFloat(), data.sumOf { it.amount }.toFloat())
                        }

                        val incomeStartDate = listIncome.last().createdAt
                        val expenseStartDate = listExpense.last().createdAt

                        _state.value = _state.value.copy(
                            loading = false,
                            listIncomeDate = listDataIncome,
                            listExpenseData = listDataExpense,
                            incomeStartDate = incomeStartDate,
                            expenseStartDate = expenseStartDate
                        )
                    }

                    is Resource.Error -> _state.value =
                        _state.value.copy(error = result.message, loading = false)

                    is Resource.Loading -> _state.value =
                        _state.value.copy(error = null, loading = true)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}