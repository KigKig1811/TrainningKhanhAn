package com.vnpay.anlmk.networks.entities.response

import com.google.gson.annotations.SerializedName
import com.vnpay.anlmk.networks.entities.AccountEntity
import com.vnpay.anlmk.networks.entities.ResponseEntity

data class AccountResponse (
    @SerializedName("list")
     val list: List<AccountEntity>? = null
    ) : ResponseEntity()
