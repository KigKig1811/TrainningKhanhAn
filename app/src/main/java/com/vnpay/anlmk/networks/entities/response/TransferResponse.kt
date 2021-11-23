package com.vnpay.anlmk.networks.entities.response

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.networks.entities.ResponseEntity

data class TransferResponse(
    @SerializedName("otpId") val otpId: String,
    @SerializedName("beneName") val beneName: String,
    @SerializedName("fee") val fee: String
) : ResponseEntity()
