package com.beran.bisnisplus.ui.screen.home.common

import com.beran.core.domain.model.BookModel
import com.github.mikephil.charting.data.PieEntry

sealed class HomeState<out R> private constructor() {
    data class Success<T>(val data: T) : HomeState<T>()
    data class Error(val message: String) : HomeState<Nothing>()
    object Loading : HomeState<Nothing>()
}

data class HomeStates(
    val listBook: List<BookModel> = emptyList(),
    val pieEntry: List<PieEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
