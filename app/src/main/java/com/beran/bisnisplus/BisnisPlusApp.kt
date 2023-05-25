package com.beran.bisnisplus

import android.app.Application
import com.beran.bisnisplus.di.authUseCaseModule
import com.beran.bisnisplus.di.bisnisUseCaseModule
import com.beran.bisnisplus.di.bookUseCaseModule
import com.beran.bisnisplus.di.bookViewModelModule
import com.beran.bisnisplus.di.homeModelModule
import com.beran.bisnisplus.di.viewModelModule
import com.beran.core.di.authModule
import com.beran.core.di.bisnisModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BisnisPlusApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // ** timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // ** koin
        startKoin {
            AndroidLogger()
            androidContext(this@BisnisPlusApp)
            modules(
                listOf(
                    authModule,
                    authUseCaseModule,
                    viewModelModule,
                    bisnisModule,
                    bisnisUseCaseModule,
                    bookUseCaseModule,
                    bookViewModelModule,
                    homeModelModule,
                )
            )
        }
    }
}