package com.beran.core.domain.repository

import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface IBookRepository {
    fun createNewBook(bookModel: BookModel): Flow<Resource<Unit>>
    fun fetchAllBook(): Flow<Resource<List<BookModel>>>
}