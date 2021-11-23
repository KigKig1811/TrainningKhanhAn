package com.vnpay.anlmk.networks.entities.response

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.networks.entities.ResponseEntity

data class LoginResponse(
    @SerializedName("session") val session : String,
    @SerializedName("deviceId") val deviceId : Long,
    @SerializedName("lastTime") val lastTime : String,
    @SerializedName("fullName") val fullName : String,
    @SerializedName("accountNo") val accountNo : String,
    @SerializedName("avatar") val avatar : String,
    @SerializedName("otpId") val otpId:String,
    @SerializedName("sessionId") val sessionId:String

) : ResponseEntity()

data class RoleUser(
    @SerializedName("accountNo") val accountNo:String,
    @SerializedName("roleGroup") val roleGroup:String //   -- 01 quyền lập lệnh, 02 quyền duyệt lệnh 1, 03 quyền duyệt lệnh 2, 04 quyền tra cứu
)

data class RegisterFingerResponse(@SerializedName("touchId") val touchId: String) : ResponseEntity()