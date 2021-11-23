package com.vnpay.anlmk.networks.entities.request

import com.vnpay.anlmk.networks.entities.RequestEntity

data class ConfirmRequest(
    val tranId: String,
    val otp: String
) : RequestEntity()
