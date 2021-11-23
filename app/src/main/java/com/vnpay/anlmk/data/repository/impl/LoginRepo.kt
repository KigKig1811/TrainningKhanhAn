package com.vnpay.anlmk.data.repository.impl

import com.vnpay.anlmk.data.Service
import com.vnpay.anlmk.networks.entities.request.*
import com.vnpay.anlmk.networks.entities.response.LoginResponse

interface LoginRepo {
    suspend fun login(login: LoginRequest): LoginResponse

}

class LoginRepoImpl(val service: Service) : LoginRepo {


    override suspend fun login(login: LoginRequest): LoginResponse {
        return service.login(login)
    }
}


