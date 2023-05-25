package com.beran.core.domain.usecase

import android.content.Intent
import android.content.IntentSender
import com.beran.core.common.Resource
import com.beran.core.domain.model.UserModel
import com.beran.core.domain.repository.IAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class AuthInteractor(private val repository: IAuthRepository) : AuthUseCase {
    override fun signUp(name: String, email: String, password: String): Flow<Resource<Unit>> =
        repository.signUp(name, email, password)

    override fun signIn(email: String, password: String): Flow<Resource<Unit>> =
        repository.signIn(email, password)

    override fun isLogin(): Boolean = repository.isLogin()

    override fun oneTapSignUp(intent: Intent): Flow<Resource<UserModel>> =
        repository.oneTapSignUp(intent)

    override suspend fun getSignUpIntent(): IntentSender? = repository.getSignUpIntent()

    override fun oneTapSignIn(intent: Intent): Flow<Resource<UserModel>> =
        repository.oneTapSignIn(intent)

    override suspend fun getSignInIntent(): IntentSender? = repository.getSignInIntent()
    override fun updateProfile(userModel: UserModel): Flow<Resource<Unit>> =
        repository.updateProfile(userModel)

    override suspend fun logOut() = repository.logOut()
    override suspend fun userDetail(): Flow<Resource<UserModel>> = repository.userDetail()

    override fun currentUser(): FirebaseUser? = repository.currentUser()
    override fun showOnBoard(): Flow<Boolean> = repository.showOnBoard()

    override suspend fun setShowOnBoard(isFirst: Boolean) {
        repository.setShowOnBoard(isFirst)
    }
}