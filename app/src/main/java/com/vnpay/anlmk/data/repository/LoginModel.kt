package com.vnpay.anlmk.data.repository

import android.content.Intent
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.vnpay.anlmk.R
//import com.vnpay.base.R
import com.vnpay.anlmk.data.constants.Messages
import com.vnpay.anlmk.data.constants.Tags
import com.vnpay.anlmk.data.repository.impl.LoginRepo
import com.vnpay.anlmk.di.Common
import com.vnpay.anlmk.di.ResourceProvider
import com.vnpay.anlmk.networks.AppData
import com.vnpay.anlmk.networks.ServerResponseEntity
import com.vnpay.anlmk.networks.entities.request.*
import com.vnpay.anlmk.networks.entities.response.LoginResponse
import com.vnpay.anlmk.ui.bases.BaseViewModel
import com.vnpay.anlmk.utils.UIModel
import com.vnpay.finger.FingerAsymm
import com.vnpay.supersecure.Storage

class LoginModel(val loginRepo: LoginRepo, val resource: ResourceProvider) :
    BaseViewModel() {
    var tokenOTP: String? = null
    val ACTIVED: Int = 1
    val ACTIVING: Int = 2
    val INPUT_PHONE: Int = 3
    val loginResponse = MutableLiveData<LoginResponse>()

    //
//    val checkOTPResponse = MutableLiveData<OTPResponse>()
    val fingerLive = MutableLiveData<String>()

    val userLive = MutableLiveData<String>()

    var finger: FingerAsymm? = null
    var isTapResend: Boolean = false

    companion object {
        const val CHECK_STATE_FINGER = "CHECK_STATE_FINGER"
        const val FINGER_SHOWPOUP = "showPopup"
        const val FINGER_SUCCESS = "FINGER_SUCCESS"
        const val FINGER_REGLOGIN_SHOWPOUP = "FINGER_INIT_SHOWPOUP"
        const val FINGER_DONE = "FINGER_DONE"
        const val CANCEL_SUCCESS = "CANCEL_SUCCESS"
        const val NEW_ENROLLED = "NEW_ENROLLED"
        const val MAX_DATE_FINGER = "MAX_DATE_FINGER"
        const val MAX_USAGE_FINGER = "MAX_USAGE_FINGER"

    }

    fun login(
        userName: String,
        password: String,
        isActiveFinger: Boolean = false,
        isTouch: Boolean = false
    ) =
        launch {
            val login = loginRepo.login(LoginRequest(
                password,
                isActiveFinger,
                isTouch
            )
                .apply { username = userName })
            if (login != null) {
                AppData.g().deviceId = login.deviceId
                AppData.g().sessionId = login.sessionId
                Storage.g().setString(Tags.DEVICE_ID, login.deviceId.toString())

                loginResponse.value = login

            }
        }

    override fun onError(mid: Int, code: String?, response: ServerResponseEntity?) {
        super.onError(mid, code, response)
        when (mid) {

            Messages.LOGIN -> {
                if (code == "23") {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, LoginResponse::class.java)
                    loginResponse.value = res
                    return
                }
                if (code == "22") {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, LoginResponse::class.java)
                    loginResponse.value = res
                    return
                } else {
                    val res = UIModel.getInstance().provideGson()
                        .fromJson(response!!.source, LoginResponse::class.java)
                    loginResponse.value = res
                    AppData.g().sessionId = res.sessionId
                    Storage.g().setString(Tags.DEVICE_ID, res.deviceId.toString())
                    return
                }
            }
        }
    }


    fun isFirstLogin(code: String?): Boolean {
        if (code.equals("05"))
            return true
        return false
    }

    fun isLoginSuccess(code: String?): Boolean {
        if (code.equals("00") || code.equals("05"))
            return true
        return false
    }

    fun isActiveMB(): Boolean {
        return Storage.g().contains(Tags.PHONE) && Storage.g().getLong(
            Tags.CLIENT_ID, 0L
        ) != 0L
    }

    fun wasActiveOtherDevice(): Boolean {
        return Storage.g().getLong(Tags.CLIENT_ID, 0L) != 0L
    }

    fun getStateLogin(): Int {
        if (isActiveMB())
            return ACTIVED
        return INPUT_PHONE
    }

    fun startFingerPrint() {
        if (finger == null || !finger!!.isFingerprintAuthAvailable(finger!!.SUPPORT_FINGER)) {
            message.value = resource.getString(R.string.not_support_finger)
            fingerLive.value = CHECK_STATE_FINGER
        } else if (finger == null || !finger!!.isFingerprintAuthAvailable(finger!!.NO_FINGER)) {
            message.value = resource.getString(R.string.register_pin)
            fingerLive.value = CHECK_STATE_FINGER
        } else {
            if (!finger!!.isStarting)
                finger!!.startListening()
            fingerLive.value = FINGER_SHOWPOUP
            finger!!.isNeedResumeFinger = false
        }
    }

//


    fun initFinger() {
        val callback = object : FingerAsymm.Callback {
            override fun onAuthenticated() {
                fingerLive.value = FINGER_SUCCESS

            }

            override fun onFail() {
                fingerLive.value = resource.getString(R.string.finger_error)
            }

            override fun onAuthenticationHelp(p0: String?) {
                fingerLive.value = p0
            }

            override fun onNewEnroll() {
                if (isFingerActived() || isFingerFinaneActived())
                    message.value = resource.getString(R.string.finger_change)
                fingerLive.value = NEW_ENROLLED
                if (finger != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    finger!!.createKeyPair(true)
                }
                clearFingerId()
            }

            override fun onError(p0: Int, p1: String?) {
                fingerLive.value = p1
            }

        }
        if (this.finger == null)
            this.finger = FingerAsymm.newInstance(Common.baseActivity, callback)
    }

    fun clearAllData() {
        clearFingerId()
    }

    fun clearFingerId() {
        Storage.g().remove(Tags.F_LOGIN)
        Storage.g().remove(Tags.F_FINANCE)
    }

    fun isFingerActived(): Boolean {
        return Storage.g().contains(Tags.F_LOGIN)
    }

    fun getFingerLogin(): String {
        return Storage.g().getString(Tags.F_LOGIN)
    }

    fun isFingerFinaneActived(): Boolean {
        return Storage.g().contains(Tags.F_FINANCE)
    }

    fun callRegisterFinger(password: String, type: String) = launch {
//        val reg = loginRepo.registerFinger(RegisterFingerRequest(password, type))
//        Storage.g().setString(if ("0" == type) Tags.F_LOGIN else Tags.F_FINANCE, reg?.touchId)
//        if ("0" == type) {
//            Storage.g().setInt(Tags.COUNT_FPR, 0)
//            Storage.g().setLong(Tags.DATE_START_FPR, System.currentTimeMillis())
//        }
//        if (!TextUtils.isEmpty(reg?.touchId))
//            fingerLive.value = FINGER_DONE
    }

    fun removeLoginFinger() {
        Storage.g().remove(Tags.F_LOGIN)
    }

    fun removeFinanceFinger() = launch {
//        val res = loginRepo.unregisterFinger(UnRegisterFingerRequest(Storage.g().getString(Tags.F_FINANCE)))
//        if (res != null) {
//            Storage.g().remove(Tags.F_FINANCE)
//            fingerLive.value = CANCEL_SUCCESS
//            message.value = resource.getString(R.string.success_removed_finger_finance)
//        }
    }

    fun isActivingAgainOrFirst(intent: Intent?): Boolean {
        return intent != null && intent.hasExtra(Tags.PHONE)
    }

    fun isValidTimeExpire(): Boolean {
        if (!Storage.g().contains(Tags.COUNT_FPR)) {
            Storage.g().setInt(Tags.COUNT_FPR, 0)
            Storage.g().setLong(Tags.DATE_START_FPR, System.currentTimeMillis())

            return true
        }
        val last = Storage.g().getInt(Tags.COUNT_FPR, 0)
        val lastTime = Storage.g().getLong(Tags.DATE_START_FPR, System.currentTimeMillis())

        if ((System.currentTimeMillis() - lastTime) / 86400000 >= 15) {
            fingerLive.value = MAX_DATE_FINGER
            return false
        }
        if (last >= 15) {
            fingerLive.value = MAX_USAGE_FINGER
            return false
        }
        return true
    }

}
