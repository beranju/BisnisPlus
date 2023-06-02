package com.beran.bisnisplus.ui.screen.pembukuan.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.bisnisplus.ui.screen.pembukuan.BooksState
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CreateBookViewModel(private val useCase: BookUseCase) : ViewModel() {

    private var _state: MutableState<BooksState> = mutableStateOf(BooksState())
    val state: State<BooksState> get() = _state


    fun createNewBook(bookModel: BookModel) {
        viewModelScope.launch {
            useCase.createNewBook(bookModel).collect { result ->
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}