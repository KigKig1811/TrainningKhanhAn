package com.vnpay.anlmk.networks.entities.request

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.networks.entities.RequestEntity

data class OtpRequest(
    @SerializedName("otpId") val otpId :String ,
    @SerializedName("otp") val otp :String

):RequestEntity()
