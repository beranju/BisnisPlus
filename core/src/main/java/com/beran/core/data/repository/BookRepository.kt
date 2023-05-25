package com.beran.core.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.beran.core.common.Constant
import com.beran.core.common.Resource
import com.beran.core.common.asMap
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.repository.IBookRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.opencsv.CSVWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.File
import java.io.FileWriter

class BookRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : IBookRepository {

    private val bookRef = firestore.collection(Constant.bookRef)
    private val currentUser = auth.currentUser
    private val userDocName = "${currentUser?.uid}-${currentUser?.email}"

    /**
     * using runCatching instead try-catch blok cause its more concise...
     * ... and functional way to handle errors
     */
    override fun createNewBook(bookModel: BookModel): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            try {
                val userQuerySnapshot =
                    firestore.collection(Constant.userRef).whereEqualTo("uid", currentUser?.uid)
                        .get().await()
                val listDoc = userQuerySnapshot.documents.map {
                    it.toObject(BookModel::class.java)
                }
                bookModel.bisnisId = listDoc.first()?.bisnisId.toString()
                bookRef.document(userDocName).collection("books").add(bookModel).await()
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Something went wrong!"))
            }
        }.flowOn(Dispatchers.IO)

    override fun updateBook(bookModel: BookModel): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val mappedData = bookModel.asMap()
            val bookQuerySnapshot = bookRef.document(userDocName).collection(Constant.books)
                .whereEqualTo(Constant.bookId, bookModel.bookId).get().await()
            val bookDocId: String = bookQuerySnapshot.documents.map {
                it.id
            }.first()
            // ** update the book after get the doc id
            bookRef.document(userDocName).collection(Constant.books).document(bookDocId)
                .update(mappedData).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: Constant.UnknownError))
        }
    }

    override fun deleteBook(bookId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val bookQuerySnapshot = bookRef.document(userDocName).collection(Constant.books)
                .whereEqualTo(Constant.bookId, bookId).get().await()
            for (doc in bookQuerySnapshot.documents) {
                doc.reference.delete().await()
                emit(Resource.Success(Unit))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: Constant.UnknownError))
        }
    }

    override fun fetchBookById(bookId: String): Flow<Resource<BookModel>> = flow {
        emit(Resource.Loading)
        try {
            val bookQuerySnapshot = bookRef.document(userDocName).collection(Constant.books)
                .whereEqualTo(Constant.bookId, bookId).get().await()
            val book =
                bookQuerySnapshot.documents.mapNotNull { doc -> doc.toObject(BookModel::class.java) }
            if (book.isNotEmpty()) {
                emit(Resource.Success(book.first()))
            } else {
                emit(Resource.Error("No Data"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.message ?: Constant.UnknownError))
        }
    }.flowOn(Dispatchers.Default)

    override fun fetchAllBook(): Flow<Resource<List<BookModel>>> =
        callbackFlow {
            trySend(Resource.Loading)
            try {
                val listenerRegistration =
                    firestore.collection(Constant.bookRef).document(userDocName).collection("books")
                        .orderBy(Constant.timeStamp, Query.Direction.DESCENDING)
                        .addSnapshotListener { snapshot, error ->
                            error?.let {
                                trySend(Resource.Error("onError: ${error.message}"))
                                return@addSnapshotListener
                            }
                            if (snapshot != null && !snapshot.isEmpty) {
                                val bookData = snapshot.map { queryDocumentSnapshot ->
                                    queryDocumentSnapshot.toObject(BookModel::class.java)
                                }
                                trySend(Resource.Success(bookData))
                            }
                        }
                // ** stop to listen
                awaitClose {
                    listenerRegistration.remove()
                    // ** use channel.close() instead cancel() to avoid force close
                    channel.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(Resource.Error(e.message ?: "Something went wrong!"))
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun exportDataIntoCsv(filePath: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            try {
                val file = File(filePath)
                if (!file.exists()) {
                    file.parentFile?.mkdirs() // Create parent directories if they don't exist
                    file.createNewFile() // Create the actual file
                }
                val writer = CSVWriter(FileWriter(file))
//                    val headers = arrayOf(
//                        "Book ID",
//                        "Business ID",
//                        "Amount",
//                        "Mitra",
//                        "Note",
//                        "Category",
//                        "Type",
//                        "State",
//                        "Stock",
//                        "Created At",
//                        "Updated At"
//                    )
//                    writer.writeNext(headers)
                writer.writeNext(arrayOf("id"))

                val bookSnapShot =
                    bookRef.document(userDocName).collection(Constant.books).get().await()
                for (doc in bookSnapShot.documents) {
                    writer.writeNext(arrayOf("${doc.data?.get("bookId")}"))
//                        val book: BookModel? = doc.toObject(BookModel::class.java)
//                        Timber.tag("BookRepository").i("buku : $book")
//                        if (book != null) {
//                            val bookRow = arrayOf(
//                                book.bookId.toString(),
//                                book.bisnisId.toString(),
//                                book.amount.toString(),
//                                book.mitra.toString(),
//                                book.note.toString(),
//                                book.category.toString(),
//                                book.type.toString(),
//                                book.state.toString(),
//                                book.listStock?.stocks.toString(),
//                                Utils.convertToDate(book.createdAt ?: 0).toString(),
//                                Utils.convertToDate(book.updatedAt ?: 0).toString()
//                            )
//                            writer.writeNext(bookRow)
//                        }else{
//                            Timber.tag("BookRepository").i("Empty data for ${doc.id}")
//                        }
                }
                writer.close()
                emit(Resource.Success(Unit))

            } catch (e: Exception) {
                e.printStackTrace()
                Timber.tag("Bookrepository").e(e)
                emit(Resource.Error(e.message ?: Constant.UnknownError))
            }
        }.flowOn(Dispatchers.IO)
}