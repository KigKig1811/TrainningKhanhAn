package com.vnpay.anlmk.data.repository.impl

import com.vnpay.anlmk.data.Service
import com.vnpay.anlmk.networks.entities.request.TransferRequest
import com.vnpay.anlmk.networks.entities.response.TransferResponse


interface InternalTransferRepo {
    suspend fun transfer(transfer: TransferRequest): TransferResponse


}

class InternalTransferRepoImpl(val service: Service) : InternalTransferRepo {
    override suspend fun transfer(transfer: TransferRequest): TransferResponse {
        return service.transfer(transfer)
    }


}