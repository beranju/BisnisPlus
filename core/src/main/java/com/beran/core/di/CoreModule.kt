package com.beran.core.di

import com.beran.core.R
import com.beran.core.common.Constant
import com.beran.core.data.repository.AuthRepository
import com.beran.core.data.repository.BisnisRepository
import com.beran.core.data.repository.BookRepository
import com.beran.core.domain.repository.IAuthRepository
import com.beran.core.domain.repository.IBisnisRepository
import com.beran.core.domain.repository.IBookRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { Identity.getSignInClient(androidContext()) }
    single(named(Constant.signIn)) {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(androidApplication().getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
    single(named(Constant.signUp)) {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(androidApplication().getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
    single<IAuthRepository> {
        AuthRepository(
            androidContext(),
            get(),
            get(),
            get(),
            get(),
            get(qualifier = named(Constant.signIn)),
            get(qualifier = named(Constant.signUp))
        )
    }
}

val bisnisModule = module {
    single { FirebaseFirestore.getInstance() }
    single<IBisnisRepository> {
        BisnisRepository(
            get()
        )
    }
    single<IBookRepository> { BookRepository(auth = get(), firestore = get()) }
}