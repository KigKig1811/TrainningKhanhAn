package com.vnpay.anlmk.data.repository

import androidx.lifecycle.MutableLiveData
import com.vnpay.anlmk.R
import com.vnpay.anlmk.data.repository.impl.TransferRepo
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.ui.entites.FunctionEntity

class TransferModel(val transferRepo: TransferRepo, val resource: ResourceProvider) :
    BaseViewModel() {
    var functionLive = MutableLiveData<List<FunctionEntity>>()
    var listFunction = ArrayList<FunctionEntity>()

    fun addFunctionTransfer() {
        listFunction.add(
            FunctionEntity(
                1, resource.getString(R.string.internalBank),
                R.drawable.ic_transfer,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                2, resource.getString(R.string.fastTransfer),
                R.drawable.ic_search,
                R.color.white_filled
            )
        )
        listFunction.add(
            FunctionEntity(
                3, resource.getString(R.string.normalTransfer),
                R.drawable.ic_account,
                R.color.white_filled
            )
        )
        functionLive.value = listFunction
    }
}