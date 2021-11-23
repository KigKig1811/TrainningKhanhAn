package com.vnpay.anlmk.di

import com.vnpay.anlmk.ui.dialogs.ConfirmDialog
import com.vnpay.anlmk.ui.dialogs.FingerConfirmDialog
import com.vnpay.anlmk.ui.dialogs.LoadingDialog
import com.vnpay.anlmk.ui.dialogs.LoginFingerDialog
import org.koin.dsl.module

val dialogs = module {
    factory { createLoadingDialog() }
    factory { createConfirmDialog() }
    factory { createFingerConfirmDialog() }
    factory { createLoginFingerDialog() }
}

fun createLoadingDialog(): LoadingDialog {
    return LoadingDialog(Common.baseActivity)
}

fun createConfirmDialog(): ConfirmDialog {
    return ConfirmDialog(Common.baseActivity)
}

fun createFingerConfirmDialog(): FingerConfirmDialog {
    return FingerConfirmDialog(Common.baseActivity)
}

fun createLoginFingerDialog(): LoginFingerDialog {
    return LoginFingerDialog(Common.baseActivity)
}