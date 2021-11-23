package com.vnpay.anlmk.di


import com.vnpay.anlmk.data.repository.*
import com.vnpay.anlmk.data.repository.impl.*
import com.vnpay.anlmk.ui.activities.login.HomeRepo
import com.vnpay.anlmk.ui.activities.login.HomeRepoImpl
import com.vnpay.anlmk.ui.activities.splash.SplashModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val models = module {

    viewModel {
        SplashModel(get())
    }
    viewModel {
        OTPModel(get(),get())
    }

    viewModel {
        LoginModel(get(), get())
    }
    viewModel {
        HomeModel(get(), get(), get())
    }
    viewModel {
        AccountModel(get(),get())
    }

    viewModel {
        TransferModel(get(),get())
    }

    viewModel {
        InternalTransferModel(get(),get(),get())
    }

    viewModel {
        ConfirmInternalTransferModel(get(),get())
    }

    viewModel {
        SuccessTransferModel(get(),get())
    }
}

val impls = module {
    single<LoginRepo>  {
        LoginRepoImpl(
            get()
        )
    }

//    mycode
    single<OtpRepo>  {
        OtpRepoImpl(
            get()
        )
    }
    //
    single<HomeRepo>  {
        HomeRepoImpl(
            get()
        )
    }
    single<AccountRepo>  {
        AccountRepoImpl(
            get()
        )
    }

    single<TransferRepo>  {
        TransferRepoImpl(
            get()
        )
    }

    single<InternalTransferRepo>  {
        InternalTransferRepoImpl(
            get()
        )
    }
    single<ConfirmInternalTransferRepo>  {
        ConfirmInternalTransferImpl(
            get()
        )
    }

    single<SuccessfulTransferRepo>  {
        SuccessfulTransferRepoImpl(
            get()
        )
    }

    single<CommonRepo>  {
        CommonRepoImpl(
            get()
        )
    }
}
val utilities = module{
    single<ResourceProvider> { AndroidResourceProvider(get()) }
}

val modules = listOf(utilities, networks, models, dialogs, impls)