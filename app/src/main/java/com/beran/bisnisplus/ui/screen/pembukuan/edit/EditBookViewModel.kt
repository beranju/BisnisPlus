package com.beran.bisnisplus.ui.screen.pembukuan.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.bisnisplus.ui.screen.pembukuan.BookStates
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditBookViewModel(private val useCase: BookUseCase) : ViewModel() {

    private var _uiState: MutableStateFlow<BookStates<Unit>> =
        MutableStateFlow(BookStates.Loading)
    val uiState get() = _uiState.asStateFlow()

    private var _book: MutableStateFlow<BookStates<BookModel>> =
        MutableStateFlow(BookStates.Loading)
    val book get() = _book.asStateFlow()

    fun fetchBookById(bookId: String) {
        viewModelScope.launch {
            useCase.fetchBookById(bookId)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> _book.value = BookStates.Loading
                        is Resource.Error -> _book.value = BookStates.Error(result.message)
                        is Resource.Success -> _book.value = BookStates.Success(result.data)
                    }
                }
        }

    }

    fun updateBook(bookModel: BookModel) {
        viewModelScope.launch {
            useCase.updateBook(bookModel).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = BookStates.Loading
                    is Resource.Error -> _uiState.value = BookStates.Error(result.message)
                    is Resource.Success -> _uiState.value = BookStates.Success(result.data)
                }
            }
        }
    }
}