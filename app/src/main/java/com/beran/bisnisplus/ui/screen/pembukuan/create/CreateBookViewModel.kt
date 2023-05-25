package com.beran.bisnisplus.ui.screen.pembukuan.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.bisnisplus.ui.screen.pembukuan.BookState
import com.beran.bisnisplus.ui.screen.pembukuan.BookStates
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateBookViewModel(private val useCase: BookUseCase): ViewModel() {

    private var _uiState: MutableStateFlow<BookState<Unit>> = MutableStateFlow(BookState.Initial)
    val uiState get() = _uiState.asStateFlow()


    fun createNewBook(bookModel: BookModel) {
        viewModelScope.launch {
            useCase.createNewBook(bookModel).collect { result ->
                when (result) {
                    is Resource.Success -> _uiState.value = BookState.Success(result.data)
                    is Resource.Error -> _uiState.value = BookState.Error(result.message)
                    is Resource.Loading -> _uiState.value = BookState.Loading
                }
            }
        }
    }
}