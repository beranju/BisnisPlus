package com.beran.bisnisplus.ui.screen.home.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val bookUseCase: BookUseCase) : ViewModel() {

    private var _listBook: MutableStateFlow<HomeState<List<BookModel>>> =
        MutableStateFlow(HomeState.Loading)
    val listBook get() = _listBook.asStateFlow()

    init {
        fetchListBook()
    }

    fun fetchListBook() {
        viewModelScope.launch {
            bookUseCase.fetchAllBook().collect { result ->
                when (result) {
                    is Resource.Loading -> HomeState.Loading
                    is Resource.Error -> HomeState.Error(result.message)
                    is Resource.Success -> HomeState.Success(result.data)
                }
            }
        }
    }

}