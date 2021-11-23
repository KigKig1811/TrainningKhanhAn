package com.vnpay.anlmk.ui.activities.splash

import com.vnpay.anlmk.data.constants.Tags
import com.vnpay.anlmk.data.repository.impl.LoginRepo
import com.vnpay.anlmk.networks.AppData
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.supersecure.Storage

class SplashModel(val api: LoginRepo) : BaseViewModel() {

    fun install() {
        Storage.g()
            .get(Tags.PHONE)
            .setFlowable {
                AppData.g().phone = it
            }
        AppData.g().deviceId = Storage.g().getLong(Tags.DEVICE_ID, 0L)

    }

    fun isActived(): Boolean {
        return Storage.g().contains(Tags.PHONE)
    }

}
