package com.beran.bisnisplus.ui.screen.pembukuan

import android.net.Uri
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.model.UserModel

data class BooksState(
    val book: BookModel = BookModel(),
    val books: List<BookModel> = emptyList(),
    val incomeBooks: List<Pair<Long?, List<BookModel>>> = emptyList(),
    val expenseBooks: List<Pair<Long?, List<BookModel>>> = emptyList(),
    val incomeAmount : Double = 0.0,
    val expenseAmount : Double = 0.0,
    val uri: Uri = Uri.EMPTY,
    val user: UserModel? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
