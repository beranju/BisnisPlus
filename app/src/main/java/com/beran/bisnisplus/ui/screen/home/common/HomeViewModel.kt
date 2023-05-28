package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(private val bookUseCase: BookUseCase) : ViewModel() {

    private var _listBook: MutableStateFlow<HomeState<List<BookModel>>> =
        MutableStateFlow(HomeState.Loading)
    val listBook get() = _listBook.asStateFlow()

    private var _state = mutableStateOf(HomeStates())
    val state: State<HomeStates> get() = _state

    init {
        fetchBook()
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
    fun fetchBook() {
        viewModelScope.launch {
            bookUseCase.fetchAllBook().collect { result ->
                when (result) {
                    is Resource.Loading -> _state.value = _state.value.copy(isLoading = true, error = null)
                    is Resource.Error -> _state.value = _state.value.copy(isLoading = false, error = result.message)
                    is Resource.Success -> {
                        Timber.tag("HomeViewModel").i("data: ${result.data}")
                        _state.value = _state.value.copy(isLoading = false, listBook = result.data)
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