package com.beran.core.domain.repository

import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface IBookRepository {
    fun createNewBook(bookModel: BookModel): Flow<Resource<Unit>>
    fun updateBook(bookModel: BookModel): Flow<Resource<Unit>>
    fun deleteBook(bookId: String): Flow<Resource<Unit>>
    fun fetchBookById(bookId: String): Flow<Resource<BookModel>>
    fun fetchAllBook(): Flow<Resource<List<BookModel>>>
    suspend fun exportDataIntoCsv(filePath: String): Flow<Resource<Unit>>

}