package com.beran.core.data.repository

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.beran.core.common.Constant
import com.beran.core.common.Resource
import com.beran.core.domain.model.UserModel
import com.beran.core.domain.repository.IAuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await


private val Context.onBoardSession: DataStore<Preferences> by preferencesDataStore(Constant.onBoard)

class AuthRepository(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
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
                            email = it.email.orEmpty()
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
                        email = user.email.orEmpty()
                    )
                    emit(Resource.Success(data))
                    fireStore.collection(Constant.userRef).add(data).await()
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


    override fun isLogin(): Boolean = auth.currentUser != null

    override suspend fun logOut() {
        oneTapClient.signOut().await()
        auth.signOut()
    }

    override fun currentUser(): UserModel? = auth.currentUser?.run {
        UserModel(
            uid = uid,
            name = displayName.orEmpty(),
            email = displayName.orEmpty(),
            phoneNumber = phoneNumber,
            photoUrl = photoUrl.toString()
        )
    }

    override fun showOnBoard(): Flow<Boolean> = context.onBoardSession.data.map {
        it[isFirstLaunch] ?: true
    }

    override suspend fun setShowOnBoard(isFirst: Boolean) {
        context.onBoardSession.edit { mutablePreferences ->
            mutablePreferences[isFirstLaunch] = isFirst
        }
    }


}