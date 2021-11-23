package com.vnpay.anlmk.data.repository.impl

import com.vnpay.anlmk.data.Service
import com.vnpay.anlmk.networks.entities.request.OtpRequest
import com.vnpay.anlmk.networks.entities.response.OTPResponse

//tạo optRepo interface
interface OtpRepo {
    suspend fun checkOTP(optSMS: OtpRequest): OTPResponse

}

class OtpRepoImpl(val service: Service) : OtpRepo {
    override suspend fun checkOTP(optSMS: OtpRequest): OTPResponse {
        return service.otp(optSMS)
    }


}