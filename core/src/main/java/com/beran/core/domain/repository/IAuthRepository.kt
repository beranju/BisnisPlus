package com.beran.core.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.beran.core.common.Resource
import com.beran.core.domain.model.UserModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun signUp(name: String, email: String, password: String): Flow<Resource<Unit>>
    fun signIn(email: String, password: String): Flow<Resource<Unit>>
    fun isLogin(): Boolean
    fun oneTapSignUp(intent: Intent): Flow<Resource<UserModel>>
    fun oneTapSignIn(intent: Intent): Flow<Resource<UserModel>>
    suspend fun getSignUpIntent(): IntentSender?
    suspend fun getSignInIntent(): IntentSender?
    fun updateProfile(userModel: UserModel): Flow<Resource<Unit>>
    suspend fun logOut()
    suspend fun userDetail(): Flow<Resource<UserModel>>
    fun currentUser(): FirebaseUser?
    fun showOnBoard(): Flow<Boolean>
    suspend fun setShowOnBoard(isFirst: Boolean)
}