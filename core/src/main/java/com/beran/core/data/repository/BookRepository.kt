package com.beran.core.data.repository

import com.beran.core.common.Constant
import com.beran.core.common.Resource
import com.beran.core.domain.model.BookModel
import com.beran.core.domain.repository.IBookRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

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
                                    queryDocumentSnapshot.toObject(BookModel::class.java).also {
                                        it.bookId = queryDocumentSnapshot.id
                                    }
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
}