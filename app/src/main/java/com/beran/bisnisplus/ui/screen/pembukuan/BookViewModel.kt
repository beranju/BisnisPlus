package com.beran.bisnisplus.ui.screen.pembukuan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.model.UserModel
import com.beran.core.domain.usecase.AuthUseCase
import com.beran.core.domain.usecase.book.BookUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(private val useCase: BookUseCase, private val authUseCase: AuthUseCase) :
    ViewModel() {

    val currentUser: UserModel? = authUseCase.currentUser()

    private var _uiState: MutableStateFlow<BookState<Unit>> = MutableStateFlow(BookState.Initial)
    val uiState get() = _uiState.asStateFlow()

    private var _listBook: MutableStateFlow<BookState<List<BookModel>>> =
        MutableStateFlow(BookState.Initial)
    val listBook get() = _listBook.asStateFlow()


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

    fun fetchBooks() {
        viewModelScope.launch {
            useCase.fetchAllBook().collect { result ->
                when (result) {
                    is Resource.Success -> _listBook.value = BookState.Success(result.data)
                    is Resource.Error -> _listBook.value = BookState.Error(result.message)
                    is Resource.Loading -> _listBook.value = BookState.Loading
                }
            }
        }
    }

    var resetUiState = MutableStateFlow(BookState.Initial)
}