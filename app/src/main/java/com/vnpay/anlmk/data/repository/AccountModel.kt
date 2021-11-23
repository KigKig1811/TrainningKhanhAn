package com.vnpay.anlmk.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vnpay.anlmk.data.constants.Messages
import com.vnpay.anlmk.data.repository.impl.AccountRepo
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.networks.ServerResponseEntity
import com.vnpay.anlmk.networks.entities.AccountEntity
import com.vnpay.anlmk.networks.entities.request.AccountRequest
import com.vnpay.anlmk.networks.entities.response.AccountResponse
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.utils.UIModel

class AccountModel(val accountRepo: AccountRepo, val resource: ResourceProvider) : BaseViewModel() {

    var accountResponse = MutableLiveData<AccountResponse>()

    var payAccountResponse = MutableLiveData<List<AccountEntity>>()
    var listPayAccount = ArrayList<AccountEntity>()

    var saveAccountResponse = MutableLiveData<List<AccountEntity>>()
    var listSaveAccount = ArrayList<AccountEntity>()

    fun setListAccount(list: List<AccountEntity>?) {

        for (item in list!!) {
            if (item.accountType.equals("D")) {
                listPayAccount.add(item)
            } else {
                listSaveAccount.add(item)
            }
        }
        payAccountResponse.value = listPayAccount
        saveAccountResponse.value = listSaveAccount

    }

    fun account(

    ) =
        launch {
            val account = accountRepo.account(
                AccountRequest(
                )
                    .apply { })
            if (account != null) {
                accountResponse.value = account
            }
        }

    fun isSuccess(code: String?): Boolean {
        if (code.equals("00"))
            return true
        return false
    }
    fun isExpire(code: String?): Boolean {
        if (code.equals("98"))
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
        }
    }
}


