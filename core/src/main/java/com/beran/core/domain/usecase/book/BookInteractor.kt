package com.beran.core.domain.usecase.book

import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.repository.IBookRepository
import kotlinx.coroutines.flow.Flow

class BookInteractor(private val repository: IBookRepository) : BookUseCase {
    override fun createNewBook(bookModel: BookModel): Flow<Resource<Unit>> =
        repository.createNewBook(bookModel)

    override fun updateBook(bookModel: BookModel): Flow<Resource<Unit>> =
        repository.updateBook(bookModel)

    override fun deleteBook(bookId: String): Flow<Resource<Unit>> = repository.deleteBook(bookId)
    override fun fetchBookById(bookId: String): Flow<Resource<BookModel>> =
        repository.fetchBookById(bookId)

    override fun fetchAllBook(): Flow<Resource<List<BookModel>>> =
        repository.fetchAllBook()

    override suspend fun exportDataIntoCsv(filePath: String): Flow<Resource<Unit>> = repository.exportDataIntoCsv(filePath)
}