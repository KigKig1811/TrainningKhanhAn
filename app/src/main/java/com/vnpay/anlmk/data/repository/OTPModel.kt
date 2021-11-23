package com.vnpay.anlmk.data.repository

import androidx.lifecycle.MutableLiveData
import com.vnpay.anlmk.data.constants.Messages
import com.vnpay.anlmk.data.repository.impl.OtpRepo
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.networks.ServerResponseEntity
import com.vnpay.anlmk.networks.entities.request.OtpRequest
import com.vnpay.anlmk.networks.entities.response.OTPResponse
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.utils.UIModel

class OTPModel(val resource: ResourceProvider, val optRepo: OtpRepo) : BaseViewModel() {

    val checkOTPResponse = MutableLiveData<OTPResponse>()

    fun loginOTP(
        otpId: String,
        otp: String
    ) =
        launch {
            val checkOTP = optRepo.checkOTP(
                OtpRequest(otpId, otp)
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

    override fun onError(mid: Int, code: String?, response: ServerResponseEntity?) {
        super.onError(mid, code, response)

        when (mid) {

            Messages.CheckOTP -> {
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