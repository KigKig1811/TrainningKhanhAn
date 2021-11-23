package com.vnpay.anlmk.data.repository

import android.content.Intent
import android.provider.Settings.Global.getString
import androidx.lifecycle.MutableLiveData
import com.vnpay.anlmk.R
import com.vnpay.anlmk.data.constants.Messages
import com.vnpay.anlmk.data.repository.impl.ConfirmInternalTransferRepo
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.networks.ServerResponseEntity
import com.vnpay.anlmk.networks.entities.request.OtpRequest
import com.vnpay.anlmk.networks.entities.response.OTPResponse
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.ui.entites.InfoConfirmEmtity
import com.vnpay.anlmk.utils.UIModel

class ConfirmInternalTransferModel(
    val resource: ResourceProvider,
    val confirmInternalTransferRepo: ConfirmInternalTransferRepo
) : BaseViewModel() {
    var infoTransferLive = MutableLiveData<List<InfoConfirmEmtity>>()
    var listInfoTransfer = ArrayList<InfoConfirmEmtity>()
    val checkOTPResponse = MutableLiveData<OTPResponse>()

    fun setInfoTransfer(intent: Intent) {

        var beneName = intent.getStringExtra("beneName")
        var fee = intent.getStringExtra("fee")
        var accountFrom = intent.getStringExtra("accountFrom")
        var accountTo = intent.getStringExtra("accountTo")
        var amount = intent.getStringExtra("amount")
        var content = intent.getStringExtra("content")
        listInfoTransfer.add(InfoConfirmEmtity(resource.getString(R.string.accountFrom), accountFrom))
        listInfoTransfer.add(InfoConfirmEmtity(resource.getString(R.string.accounTo), accountTo))
        listInfoTransfer.add(InfoConfirmEmtity(resource.getString(R.string.accountName), beneName))
        listInfoTransfer.add(InfoConfirmEmtity(resource.getString(R.string.money), amount + " " + resource.getString(R.string.VND)))
        listInfoTransfer.add(InfoConfirmEmtity(resource.getString(R.string.fee), fee + " " + resource.getString(R.string.VND)))
        listInfoTransfer.add(InfoConfirmEmtity(resource.getString(R.string.note), content))

        infoTransferLive.value = listInfoTransfer
    }

    fun confirmOTP(
        otpId: String,
        otp: String
    ) =
        launch {
            val checkOTP = confirmInternalTransferRepo.checkOTP(
                OtpRequest(
                    otpId,
                    otp
                )
                    .apply {})
            if (checkOTP != null) {
                checkOTPResponse.value = checkOTP
            }
        }

    fun isOTPSuccess(code: String?): Boolean {
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
            Messages.CheckOTPTRANSFER -> {
                if (code == "00") {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, OTPResponse::class.java)
                    checkOTPResponse.value = res
                    return
                } else {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, OTPResponse::class.java)
                    checkOTPResponse.value = res
                    return
                }
            }
        }
    }


}