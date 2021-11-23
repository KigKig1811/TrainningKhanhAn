package com.vnpay.anlmk.data.repository.impl

import com.vnpay.anlmk.data.Service
import com.vnpay.anlmk.networks.entities.request.OtpRequest
import com.vnpay.anlmk.networks.entities.response.OTPResponse

interface ConfirmInternalTransferRepo {
    suspend fun checkOTP(optSMS: OtpRequest): OTPResponse

}

class ConfirmInternalTransferImpl(val service: Service) : ConfirmInternalTransferRepo {
    override suspend fun checkOTP(optSMS: OtpRequest): OTPResponse {
       return service.confirm(optSMS)
    }


}
