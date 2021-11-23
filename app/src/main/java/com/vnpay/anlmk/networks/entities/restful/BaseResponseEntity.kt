package com.vnpay.anlmk.networks.entities.restful

import com.google.gson.annotations.SerializedName

class BaseResponseEntity {
    @SerializedName("e")
    val e: String? = null

    @SerializedName("m")
    val m: String? = null
    @SerializedName("timestamp")
    val timestamp: String? = null

    @SerializedName("code")
    val code = ""

    @SerializedName("desc")
    val desc: String? = null
}
