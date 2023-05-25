package com.beran.bisnisplus.ui.screen.pembukuan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.usecase.AuthUseCase
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BookViewModel(private val useCase: BookUseCase, private val authUseCase: AuthUseCase) :
    ViewModel() {

    private var _uiState: MutableStateFlow<BookStates<Unit>> = MutableStateFlow(BookStates.Loading)
    val uiState get() = _uiState.asStateFlow()

    private var _listBook: MutableStateFlow<BookStates<List<BookModel>>> =
        MutableStateFlow(BookStates.Loading)
    val listBook get() = _listBook.asStateFlow()

    fun fetchBooks() {
        viewModelScope.launch {
            useCase.fetchAllBook().collect { result ->
                when (result) {
                    is Resource.Success -> _listBook.value = BookStates.Success(result.data)
                    is Resource.Error -> _listBook.value = BookStates.Error(result.message)
                    is Resource.Loading -> _listBook.value = BookStates.Loading
                }
            }
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            useCase.deleteBook(bookId = bookId).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.value = BookStates.Loading
                    is Resource.Error -> _uiState.value = BookStates.Error(result.message)
                    is Resource.Success -> _uiState.value = BookStates.Success(result.data)
                }
            }
        }
    }

    var resetUiState = MutableStateFlow(BookState.Initial)
}

