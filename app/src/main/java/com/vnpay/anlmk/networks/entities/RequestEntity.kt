package com.vnpay.anlmk.networks.entities

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.BuildConfig
//import com.vnpay.base.BuildConfig
import com.vnpay.anlmk.networks.AppData

/**
 * Created by Lvhieu2016 on 10/17/2016.
 */

open class RequestEntity {
    @SerializedName("sessionId")
    var sessionId: String? = AppData.g().sessionId;
    @SerializedName("deviceId")
    var deviceId: Long = 0
    @SerializedName("lang")
    var language: String? = null
    @SerializedName("appVersion")
    val appVersion: String = BuildConfig.VERSION_NAME
    @SerializedName("username")
    var username: String? = AppData.g().phone
    @SerializedName("cif")
    val cif: String? = AppData.g().cif
    @SerializedName("branchCode")
    val branchCode: String?
    @SerializedName("cusName")
    val cusName: String? = AppData.g().fullName


    @Transient
    var isRunItBackground: Boolean = false
    @Transient
    var isUnlimitedRequest: Boolean = false

    fun setBackgroundIt(runItBackground: Boolean): RequestEntity {
        this.isRunItBackground = runItBackground
        return this
    }

    init {
        language = "VN"
        deviceId = AppData.g().deviceId
        username = AppData.g().phone
        branchCode = AppData.g().branchCode
    }

}
