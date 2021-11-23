package com.vnpay.anlmk.networks.entities.restful

import android.os.Build
import android.provider.Settings
import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.BuildConfig
//import com.vnpay.base.BuildConfig
import com.vnpay.anlmk.di.Common
import com.vnpay.anlmk.networks.AppData

class BaseRequestResfulEntity(@field:Transient val mid: Int) {
    @SerializedName("method")
    private var method: String? = null
    @SerializedName("version")
    private var version: String? = null
    @SerializedName("deviceType")
    private var deviceType: String? = null
    @SerializedName("imei")
    private var imei: String? = null
    @SerializedName("deviceModel")
    private var deviceModel: String? = null
    @SerializedName("sdkVersion")
    private var sdkVersion: String? = null
    @SerializedName("data")
    private var data: Any? = null
    @SerializedName("phone")
    private var phone: String? = null
    @SerializedName("timestamp")
    private var timestamp: Long = 0

    @Transient
    val isBackground: Boolean = false


    private val androidId: String
        get() {
            try {
                return Settings.Secure.getString(
                    Common.baseActivity.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return "NoAndroidId"
        }

    init {
        version = BuildConfig.VERSION_NAME
        deviceType = "1"
        imei = androidId
        deviceModel = Build.MANUFACTURER + " " + Build.MODEL
        sdkVersion = Build.VERSION.SDK_INT.toString()
        timestamp = System.currentTimeMillis()
        phone = AppData.g().phone
        method = "POST"
    }

    fun setData(data: Any) {
        this.data = data
    }
}
