package com.vnpay.anlmk.networks.entities.restful

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.data.constants.DatasourceProperties
import com.vnpay.anlmk.utils.AESService

import android.util.Base64
import android.util.Log
import com.vnpay.anlmk.utils.UIModel
import java.lang.Exception


class CreateRequest {
    @SerializedName("m")
    private var m: String? = null
    @SerializedName("e")
    private var e: String? = null

    companion object {

        fun create(strOldBody: String): String {
            try {
                val req = CreateRequest()
                req.e =
                    AESService.getInstance().encrypt(DatasourceProperties.KEY_DEFAULT, strOldBody)

                req.m = Base64.encodeToString(AESService.getInstance().createMac(
                    (""+DatasourceProperties.APP_ID+strOldBody).toByteArray(),
                    DatasourceProperties.KEY_MAC.toByteArray()), Base64.NO_WRAP)
                return  UIModel.getInstance().provideGson().toJson(req)
            }catch (e:Exception){
                Log.wtf("EX", e)
            }
            return strOldBody
        }
    }
}
