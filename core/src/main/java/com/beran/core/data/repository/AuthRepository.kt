package com.beran.core.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.beran.core.common.Constant
import com.beran.core.common.Resource
import com.beran.core.common.asMap
import com.beran.core.domain.model.UserModel
import com.beran.core.domain.repository.IAuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException


private val Context.onBoardSession: DataStore<Preferences> by preferencesDataStore(Constant.onBoard)

class AuthRepository(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private var oneTapClient: SignInClient,
    private var signInRequest: BeginSignInRequest,
    private var signUpRequest: BeginSignInRequest,
) : IAuthRepository {

    private val isFirstLaunch = booleanPreferencesKey(Constant.isFirst)
    override fun signUp(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            try {
                val signUpResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userData = signUpResult.user
                userData?.let { data ->
                    val profileUpdate = userProfileChangeRequest {
                        displayName = name
                    }
                    data.updateProfile(profileUpdate).await()
                    val userModel: UserModel = userData.let {
                        UserModel(
                            uid = it.uid,
                            name = it.displayName.orEmpty(),
                            email = it.email.orEmpty(),
                            createdAt = System.currentTimeMillis()
                        )
                    }
                    fireStore.collection(Constant.userRef).add(userModel).await()
                }
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)


    override fun signIn(email: String, password: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }
        }.flowOn(Dispatchers.IO)


    override fun oneTapSignUp(intent: Intent): Flow<Resource<UserModel>> =
        flow {
            emit(Resource.Loading)
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            try {
                val signUpResult = auth.signInWithCredential(googleCredential).await()
                val userData = signUpResult.user
                userData?.let { user ->
                    val data = UserModel(
                        uid = user.uid,
                        name = user.displayName.orEmpty(),
                        email = user.email.orEmpty(),
                        createdAt = System.currentTimeMillis()
                    )
                    val userSnapshot =
                        fireStore.collection(Constant.userRef).whereEqualTo(Constant.uid, data.uid)
                            .get().await()
                    val userDoc: UserModel = if (userSnapshot.isEmpty) {
                        val snapshot =
                            fireStore.collection(Constant.userRef).add(data).await().get().await()
                        snapshot.toObject(UserModel::class.java) as UserModel
                    } else {
                        val userDocument = userSnapshot.documents.first()
                        userDocument.toObject(UserModel::class.java) as UserModel
                    }
                    emit(Resource.Success(userDoc))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong!"))
            }
        }.flowOn(Dispatchers.IO)

    override fun oneTapSignIn(intent: Intent): Flow<Resource<UserModel>> =
        flow {
            emit(Resource.Loading)
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken
            val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            try {
                val signInResult = auth.signInWithCredential(googleCredential).await()
                val userData = signInResult.user
                userData?.let { user ->
                    val data = UserModel(
                        uid = user.uid,
                        name = user.displayName.orEmpty(),
                        email = user.email.orEmpty()
                    )
                    val userQuerySnapshot = fireStore.collection(Constant.userRef).whereEqualTo(Constant.uid, data.uid).get().await()
                    if (userQuerySnapshot.documents.isEmpty()){
                        fireStore.collection(Constant.userRef).add(data).await()
                    }
                    emit(Resource.Success(data))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong!"))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getSignUpIntent(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                signUpRequest
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }


    override suspend fun getSignInIntent(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                signInRequest
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    private suspend fun getImgUrl(
        storageRef: StorageReference,
        filePath: String,
        uid: String
    ): String {
        val charArray = filePath.toCharArray()
        return if (charArray.first() == 'c' || charArray.first() == 'C') {
            storageRef.child(uid)
                .putFile(Uri.parse(filePath))
                .await().storage.downloadUrl.await().toString()
        } else {
            filePath
        }

    }

    override fun updateProfile(userModel: UserModel): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            /**
             * The withContext(Dispatchers.IO) block is used to perform the image upload...
             * ...and Firestore update operations concurrently. This utilizes the IO dispatcher,...
             * ...which is optimized for performing IO-bound tasks.
             */
            val storageRef = storage.reference
            val filePath = userModel.photoUrl
            val imgUrl: String? = if (filePath != null) {
                getImgUrl(storageRef, filePath, userModel.uid.orEmpty())
            } else null
            val updatedData: UserModel = userModel.copy(photoUrl = imgUrl)
            val dataAsMap = updatedData.asMap()
            // ** Deferred is used to asynchronously await the userQuerySnapshot result.
            val userQuerySnapshotDeffered = withContext(Dispatchers.IO) {
                fireStore.collection(Constant.userRef).whereEqualTo("uid", userModel.uid).get()
                    .asDeferred()
            }
            val userQuerySnapshot = userQuerySnapshotDeffered.await()
            val userDocId: String = userQuerySnapshot.documents.map { it.id }.first()
            withContext(Dispatchers.IO) {
                fireStore.collection(Constant.userRef).document(userDocId).update(dataAsMap).await()
            }
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is IOException -> {
                    emit(Resource.Error("Cek data yang anda inputkan, pastikan koneksi internet andan baik"))
                }

                is IllegalArgumentException -> {
                    emit(Resource.Error(Constant.UnknownError))
                }

                else -> {
                    emit(Resource.Error(Constant.UnknownError))
                }
            }
        }
    }
        .flowOn(Dispatchers.IO)


    override fun isLogin(): Boolean = auth.currentUser != null

    override suspend fun logOut() {
        oneTapClient.signOut().await()
        auth.signOut()
    }

    override suspend fun userDetail(): Flow<Resource<UserModel>> =
        callbackFlow<Resource<UserModel>> {
            trySend(Resource.Loading)
            try {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    val listenerRegistration =
                        fireStore.collection(Constant.userRef).whereEqualTo("uid", user.uid)
                            .addSnapshotListener { userQuerySnapshot, error ->
                                if (error != null) {
                                    trySend(Resource.Error(error.message ?: Constant.UnknownError))
                                }
                                if (userQuerySnapshot != null) {
                                    if (userQuerySnapshot.documents.isNotEmpty()) {
                                        val userDoc = userQuerySnapshot.documents.first()
                                        val userToModel = userDoc.toObject(UserModel::class.java)
                                        val userModel = UserModel(
                                            uid = userToModel?.uid,
                                            name = userToModel?.name,
                                            email = userToModel?.email,
                                            photoUrl = userToModel?.photoUrl,
                                            phoneNumber = userToModel?.phoneNumber,
                                            bisnisId = userToModel?.bisnisId,
                                            createdAt = userToModel?.createdAt,
                                        )
                                        trySend(Resource.Success(userModel))
                                    }
                                }
                            }
                    awaitClose {
                        listenerRegistration.remove()
                        channel.close()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                trySend(Resource.Error(e.message ?: Constant.UnknownError))
            }
        }.flowOn(Dispatchers.IO)

    override fun currentUser(): FirebaseUser? = auth.currentUser

    override fun showOnBoard(): Flow<Boolean> = context.onBoardSession.data.map {
        it[isFirstLaunch] ?: true
    }

    override suspend fun setShowOnBoard(isFirst: Boolean) {
        context.onBoardSession.edit { mutablePreferences ->
            mutablePreferences[isFirstLaunch] = isFirst
        }
    }


}