package com.vnpay.anlmk.networks.entities.response

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.networks.entities.ResponseEntity

data class GetOTPResponse(
    @SerializedName("otpToken") val otpToken :String
    ) : ResponseEntity()