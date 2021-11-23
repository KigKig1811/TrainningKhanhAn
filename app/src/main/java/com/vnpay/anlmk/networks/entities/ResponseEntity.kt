package com.vnpay.anlmk.networks.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by Lvhieu2016 on 10/3/2016.
 */

abstract class ResponseEntity {
    @SerializedName("mid")
    val mid: Int = 0
    @SerializedName("code")
    val code: String? = null
    @SerializedName("des")
    val des: String? = null
    var source: String? = null

    fun isSuccess() : Boolean{
        return "00".equals(code)
    }
}

class ResponseData : ResponseEntity(){

}