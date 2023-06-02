package com.beran.core.data.repository

import android.net.Uri
import android.os.Build
import android.os.Environment
import com.beran.core.common.Constant
import com.beran.core.common.Resource
import com.beran.core.common.asMap
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.repository.IBookRepository
import com.beran.core.utils.Utils.writeCsv
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
import java.nio.file.Files
import java.nio.file.Paths

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
                                if (snapshot.documents.isNotEmpty()) {
                                    val bookData = snapshot.map { queryDocumentSnapshot ->
                                        queryDocumentSnapshot.toObject(BookModel::class.java)
                                    }
                                    trySend(Resource.Success(bookData))
                                } else {
                                    trySend(Resource.Success(emptyList()))
                                }
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

    override fun fetchBookByDates(firstDate: Long, lastDate: Long): Flow<Resource<List<BookModel>>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            val listenerRegistration =
                firestore.collection(Constant.bookRef).document(userDocName).collection("books")
                    .whereGreaterThanOrEqualTo(Constant.createdAt, firstDate).whereLessThanOrEqualTo(Constant.createdAt, lastDate)
                    .orderBy(Constant.timeStamp, Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, error ->
                        error?.let {
                            trySend(Resource.Error("onError: ${error.message}"))
                            return@addSnapshotListener
                        }
                        if (snapshot != null && !snapshot.isEmpty) {
                            if (snapshot.documents.isNotEmpty()) {
                                val bookData = snapshot.map { queryDocumentSnapshot ->
                                    queryDocumentSnapshot.toObject(BookModel::class.java)
                                }
                                trySend(Resource.Success(bookData))
                            } else {
                                trySend(Resource.Success(emptyList()))
                            }
                        }
                    }
            // ** stop to listen
            awaitClose {
                listenerRegistration.remove()
                // ** use channel.close() instead cancel() to avoid force close
                channel.close()
            }
        }catch (e: Exception){
            e.printStackTrace()
            trySend(Resource.Error(e.message ?: Constant.UnknownError))
        }
    }

    override fun fetchBookBySingleDate(date: Long): Flow<Resource<List<BookModel>>> = callbackFlow {
        trySend(Resource.Loading)
        try {

        }catch (e: Exception){
            e.printStackTrace()
            trySend(Resource.Error(e.message ?: Constant.UnknownError))
        }
    }


    override suspend fun exportDataIntoCsv(): Flow<Resource<Uri>> =
        flow {
            emit(Resource.Loading)
            try {
                val fileName = "BisnisPlusReport-${System.currentTimeMillis()}.csv"
                val folder =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val path = File(folder, fileName)
                // ** perubahan pada filepath belum ditest dan dicoba
                val writer =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Files.newBufferedWriter(Paths.get(path.absolutePath))
                    } else {
                        FileWriter(path)
                    }
                val bookSnapShot =
                    bookRef.document(userDocName).collection(Constant.books).get().await()
                val books: List<BookModel> = bookSnapShot.documents.map {
                    it.toObject(BookModel::class.java) as BookModel
                }
                writer.writeCsv(books)
                writer.flush()
                writer.close()
                val filePath = Uri.fromFile(path)
                emit(Resource.Success(filePath))
                // ** this code use to convert the data into pdf file
//                val csvData: MutableList<Array<String>> = mutableStateListOf()
//                for (doc in bookSnapShot.documents) {
//                    Timber.tag("REpositoyr").i("convert to pdf...")
//                    Utils.generatePdf(doc, "/storage/emulated/0/Download/bisnisplusdata2.pdf")
//                    Timber.tag("REpositoyr").i("convert to pdf success")
//                    val book: BookModel? = doc.toObject(BookModel::class.java)
//                    Timber.tag("BookRepository").i("buku : $book")
//                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.tag("Bookrepository").e(e)
                emit(Resource.Error(e.message ?: Constant.UnknownError))
            }
        }.flowOn(Dispatchers.IO)

}