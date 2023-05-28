package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.bisnisplus.constant.BookTypes
import com.beran.bisnisplus.constant.IncomeCategory
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.book.BookUseCase
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val bookUseCase: BookUseCase) : ViewModel() {

    private var _state = mutableStateOf(HomeStates())
    val state: State<HomeStates> get() = _state

    init {
        fetchBook()
    }

    fun fetchBook() {
        viewModelScope.launch {
            bookUseCase.fetchAllBook().collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value =
                        _state.value.copy(isLoading = true, error = null)

                    is Resource.Error -> _state.value =
                        _state.value.copy(isLoading = false, error = result.message)

                    is Resource.Success -> {
                        val listBook = result.data.sortedByDescending { it.createdAt }.take(6)
                        val listIncome =
                            result.data.filter { it.type == BookTypes.Pemasukan.string }
                        val (earn, capital, other) = listIncome
                            .groupBy { it.category }
                            .mapValues { (_, values) -> values.sumOf { it.amount } }
                            .run {
                                val earningsValue = getOrDefault(IncomeCategory.Earnings.title, 0.0)
                                val capitalValue = getOrDefault(IncomeCategory.Capital.title, 0.0)
                                val otherValue =
                                    getOrDefault(IncomeCategory.OtherEarnings.title, 0.0)
                                Triple(earningsValue, capitalValue, otherValue)
                            }
                        val dataEntry = listOf(
                            PieEntry(earn.toFloat(), IncomeCategory.Earnings.title),
                            PieEntry(capital.toFloat(), IncomeCategory.Capital.title),
                            PieEntry(other.toFloat(), "Lainnya"),
                        )
                        _state.value = _state.value.copy(
                            isLoading = false,
                            listBook = listBook,
                            pieEntry = dataEntry
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}