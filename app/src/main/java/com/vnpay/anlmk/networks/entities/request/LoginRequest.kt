package com.vnpay.anlmk.networks.entities.request

import com.vnpay.anlmk.networks.entities.RequestEntity
import com.google.gson.annotations.SerializedName


data class LoginRequest(
    @SerializedName("password") val password :String,
    @SerializedName("isTouch") val isTouch :Boolean = false,
    @SerializedName("registerTouchId") val isFingerRegister :Boolean = false
) :RequestEntity()
