package com.beran.core.domain.usecase.book

import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface BookUseCase {
    fun createNewBook(bookModel: BookModel): Flow<Resource<Unit>>
    fun fetchAllBook(): Flow<Resource<List<BookModel>>>
}