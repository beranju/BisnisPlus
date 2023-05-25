package com.beran.bisnisplus

import android.app.Activity.RESULT_OK
import android.app.Application
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.beran.bisnisplus.di.authUseCaseModule
import com.beran.bisnisplus.di.bisnisUseCaseModule
import com.beran.bisnisplus.di.bookUseCaseModule
import com.beran.bisnisplus.di.bookViewModelModule
import com.beran.bisnisplus.di.viewModelModule
import com.beran.bisnisplus.ui.component.BottomBar
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.navigation.Screen
import com.beran.bisnisplus.ui.screen.BooksScreen
import com.beran.bisnisplus.ui.screen.HomeScreen
import com.beran.bisnisplus.ui.screen.OnBoardingScreen
import com.beran.bisnisplus.ui.screen.PembayaranScreen
import com.beran.bisnisplus.ui.screen.SettingScreen
import com.beran.bisnisplus.ui.screen.SplashScreen
import com.beran.bisnisplus.ui.screen.StatistikScreen
import com.beran.bisnisplus.ui.screen.auth.LogInScreen
import com.beran.bisnisplus.ui.screen.auth.SetPhotoScreen
import com.beran.bisnisplus.ui.screen.auth.SignDataBisnis
import com.beran.bisnisplus.ui.screen.auth.SignUpScreen
import com.beran.bisnisplus.ui.screen.auth.dataBisnis.BisnisViewModel
import com.beran.bisnisplus.ui.screen.auth.signUp.SignUpViewModel
import com.beran.bisnisplus.ui.screen.auth.signin.SignInViewModel
import com.beran.bisnisplus.ui.screen.home.common.HomeViewModel
import com.beran.bisnisplus.ui.screen.onboarding.OnBoardViewModel
import com.beran.bisnisplus.ui.screen.pembayaran.CreateNewPaymentScreen
import com.beran.bisnisplus.ui.screen.pembukuan.BookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.CreateNewRecordScreen
import com.beran.bisnisplus.ui.screen.pembukuan.EditBookRecordScreen
import com.beran.bisnisplus.ui.screen.pembukuan.component.FinancialStatementScreen
import com.beran.bisnisplus.ui.screen.pembukuan.create.CreateBookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.edit.EditBookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.report.FinancialReportViewModel
import com.beran.bisnisplus.ui.screen.setting.EditProfileUserScreen
import com.beran.bisnisplus.ui.screen.setting.SettingViewModel
import com.beran.bisnisplus.ui.screen.splash.SplashViewModel
import com.beran.core.di.authModule
import com.beran.core.di.bisnisModule
import com.beran.core.domain.model.BusinessModel
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.androidx.compose.koinViewModel
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
                )
            )
        }
    }
}