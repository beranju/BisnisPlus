package com.beran.bisnisplus.di

import com.beran.bisnisplus.ui.screen.auth.dataBisnis.BisnisViewModel
import com.beran.bisnisplus.ui.screen.auth.signUp.SignUpViewModel
import com.beran.bisnisplus.ui.screen.auth.signin.SignInViewModel
import com.beran.bisnisplus.ui.screen.home.HomeViewModel
import com.beran.bisnisplus.ui.screen.onboarding.OnBoardViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.BookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.create.CreateBookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.edit.EditBookViewModel
import com.beran.bisnisplus.ui.screen.pembukuan.report.FinancialReportViewModel
import com.beran.bisnisplus.ui.screen.setting.common.SettingViewModel
import com.beran.bisnisplus.ui.screen.splash.SplashViewModel
import com.beran.bisnisplus.ui.screen.statistic.StatisticViewModel
import com.beran.core.domain.usecase.AuthInteractor
import com.beran.core.domain.usecase.AuthUseCase
import com.beran.core.domain.usecase.bisnis.BisnisInteractor
import com.beran.core.domain.usecase.bisnis.BisnisUseCase
import com.beran.core.domain.usecase.book.BookInteractor
import com.beran.core.domain.usecase.book.BookUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authUseCaseModule = module {
    factory<AuthUseCase> { AuthInteractor(get()) }
}

val bisnisUseCaseModule = module {
    factory<BisnisUseCase> { BisnisInteractor(get()) }
}

val bookUseCaseModule = module {
    factory<BookUseCase> { BookInteractor(get()) }
}

val viewModelModule = module {
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { SettingViewModel(get()) }
    viewModel { StatisticViewModel(get()) }
    viewModel { OnBoardViewModel(get()) }
    viewModel { BisnisViewModel(get(), get()) }
}
val homeModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
}

val bookViewModelModule = module {
    viewModel { EditBookViewModel(get()) }
    viewModel { CreateBookViewModel(get()) }
    viewModel { FinancialReportViewModel(get()) }
    viewModel { BookViewModel(useCase = get(), authUseCase = get()) }
}