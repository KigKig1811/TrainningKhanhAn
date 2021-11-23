package com.vnpay.anlmk.data.repository

import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.utils.SingleLiveEvent

abstract class MidAccountModel() : BaseViewModel() {
//    var accountSelected: AccountEntity? = null
    val accountLive = SingleLiveEvent<Unit>()

    companion object {
//        var listPayments: MutableList<AccountEntity>? = null
    }

    fun loadAccount() = launch {

    }
}
