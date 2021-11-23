package com.vnpay.anlmk.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vnpay.anlmk.data.constants.Messages
import com.vnpay.anlmk.data.repository.impl.AccountRepo
import com.vnpay.anlmk.data.repository.impl.InternalTransferRepo
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.networks.ServerResponseEntity
import com.vnpay.anlmk.networks.entities.AccountEntity
import com.vnpay.anlmk.networks.entities.request.AccountRequest
import com.vnpay.anlmk.networks.entities.request.TransferRequest
import com.vnpay.anlmk.networks.entities.response.AccountResponse
import com.vnpay.anlmk.networks.entities.response.TransferResponse
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.utils.UIModel

class InternalTransferModel(
    val accountRepo: AccountRepo,
    val internalTransferRepo: InternalTransferRepo,
    val resource: ResourceProvider
) :
    BaseViewModel() {
    var accountResponse = MutableLiveData<AccountResponse>()
    var payAccountResponse = MutableLiveData<List<AccountEntity>>()
    var transferResponse = MutableLiveData<TransferResponse>()
    fun account() =
        launch {
            val account = accountRepo.account(
                AccountRequest(
                )
                    .apply { })
            if (account != null) {
                accountResponse.value = account
                payAccountResponse.value = account.list
            }
        }

    fun transfer(
        accountFrom: String,
        ccy: String,
        accountTo: String,
        amount: String,
        content: String
    ) =
        launch {
            val transfer = internalTransferRepo.transfer(
                TransferRequest(
                    accountFrom,
                    ccy,
                    accountTo,
                    amount,
                    content
                ).apply { })
            if (transfer != null) {

                transferResponse.value = transfer
            }
        }

    fun isTransferSuccess(code: String?): Boolean {
        if (code.equals("00"))
            return true
        return false
    }
    override fun onError(mid: Int, code: String?, response: ServerResponseEntity?) {
        super.onError(mid, code, response)
        when (mid) {
            Messages.ACCOUNT -> {
                if (code == "00") {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, AccountResponse::class.java)
                    accountResponse.value = res

                    return
                } else {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, AccountResponse::class.java)
                    accountResponse.value = res

                    return
                }
            }

            Messages.TRANSFER -> {
                if (code == "00") {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, TransferResponse::class.java)
                    transferResponse.value = res
                    return
                } else {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, TransferResponse::class.java)
                    transferResponse.value = res
                    return
                }
            }
        }
    }
}

