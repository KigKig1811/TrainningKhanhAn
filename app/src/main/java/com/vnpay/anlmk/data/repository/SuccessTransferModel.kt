package com.vnpay.anlmk.data.repository

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.vnpay.anlmk.R
import com.vnpay.anlmk.data.repository.impl.TransferRepo
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.ui.entites.InfoConfirmEmtity

class SuccessTransferModel(val transferRepo: TransferRepo, val resource: ResourceProvider) :
    BaseViewModel() {
    var transferInfoLive = MutableLiveData<List<InfoConfirmEmtity>>()
    var transferValueLive = MutableLiveData<String>()
    var transferInfo = ArrayList<InfoConfirmEmtity>()

    fun setTransferInfo(intent: Intent) {
        val listItemCount: MutableList<InfoConfirmEmtity> =
            intent.getSerializableExtra("InfoTransfer") as MutableList<InfoConfirmEmtity>
        transferInfo.addAll(listItemCount)
        transferInfoLive.value = transferInfo
    }

    fun setTransferValue(intent: Intent) {
        transferValueLive.value = intent.getStringExtra("TransferValue") + " " + resource.getString(
            R.string.VND
        )
    }
}