package com.beran.core.domain.usecase.book

import android.net.Uri
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface BookUseCase {
    fun createNewBook(bookModel: BookModel): Flow<Resource<Unit>>
    fun updateBook(bookModel: BookModel): Flow<Resource<Unit>>
    fun deleteBook(bookId: String): Flow<Resource<Unit>>
    fun fetchBookById(bookId: String): Flow<Resource<BookModel>>
    fun fetchAllBook(): Flow<Resource<List<BookModel>>>
    fun fetchBookByDates(firstDate: Long, lastDate: Long): Flow<Resource<List<BookModel>>>
    suspend fun exportDataIntoCsv(): Flow<Resource<Uri>>
}