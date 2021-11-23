package com.vnpay.anlmk.networks.entities.request

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.networks.entities.RequestEntity

data class TransferRequest(
    @SerializedName("accountFrom") val accountFrom :String,
    @SerializedName("ccy") val ccy :String,
    @SerializedName("accountTo") val accountTo :String,
    @SerializedName("amount") val amount :String,
    @SerializedName("content") val content :String
) :RequestEntity()