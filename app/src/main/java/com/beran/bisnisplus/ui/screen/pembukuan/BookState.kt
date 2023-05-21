package com.beran.bisnisplus.ui.screen.pembukuan

sealed class BookState<out R> private constructor() {
    data class Success<T>(val data: T) : BookState<T>()
    data class Error(val message: String) : BookState<Nothing>()
    object Loading : BookState<Nothing>()
    object Initial : BookState<Nothing>()
}
