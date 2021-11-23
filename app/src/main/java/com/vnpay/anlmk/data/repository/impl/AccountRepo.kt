package com.vnpay.anlmk.data.repository.impl

import com.vnpay.anlmk.data.Service
import com.vnpay.anlmk.networks.entities.request.AccountRequest
import com.vnpay.anlmk.networks.entities.response.AccountResponse

interface AccountRepo {
    suspend fun account(account: AccountRequest): AccountResponse

}
class AccountRepoImpl(val service: Service) : AccountRepo {
    override suspend fun account(account: AccountRequest): AccountResponse {
        return service.account(account)
    }


}
