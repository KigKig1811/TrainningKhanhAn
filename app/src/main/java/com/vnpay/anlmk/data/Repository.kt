package com.vnpay.anlmk.data

import com.vnpay.anlmk.networks.entities.RequestEntity
import com.vnpay.anlmk.networks.entities.request.*
import com.vnpay.anlmk.networks.entities.response.AccountResponse
import com.vnpay.anlmk.networks.entities.response.LoginResponse
import com.vnpay.anlmk.networks.entities.response.OTPResponse
import com.vnpay.anlmk.networks.entities.response.TransferResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Service {
    @POST("login")
    suspend fun login(@Body query: LoginRequest): LoginResponse
    @POST("required_login_active")
    suspend fun otp(@Body query: OtpRequest): OTPResponse


    @POST("account")
    suspend fun  account(@Body query: RequestEntity): AccountResponse

    @POST("transfer")
    suspend fun  transfer(@Body query: TransferRequest): TransferResponse
//    @POST("confirm")
//    suspend fun confirm(@Body confirmRequest: ConfirmRequest): ResponseEntity
    @POST("confirm")
    suspend fun confirm(@Body confirmRequest: OtpRequest): OTPResponse


    @POST("init")
    suspend fun init(@Body initRequest: InitRequest): InitResponse

}
