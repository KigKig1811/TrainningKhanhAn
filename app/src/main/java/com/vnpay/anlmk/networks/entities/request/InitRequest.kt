package com.vnpay.anlmk.networks.entities.request

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.networks.entities.RequestEntity
import com.vnpay.anlmk.networks.entities.ResponseEntity

data class InitRequest  (
    @SerializedName("accountNo")
    val accountFrom: String,
    @SerializedName("ccy")
    val ccy: String,
    @SerializedName("accountTo")
    val accountTo: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("content")
    val content: String

    ): RequestEntity()

class InitResponse( val tranId: String) : ResponseEntity()
