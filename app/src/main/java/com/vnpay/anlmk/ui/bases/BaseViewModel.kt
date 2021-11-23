package com.vnpay.anlmk.ui.bases

import android.text.TextUtils
import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.vnpay.anlmk.data.constants.Messages
import com.vnpay.anlmk.networks.ServerResponseEntity
import com.vnpay.anlmk.networks.convertToBaseException
import com.vnpay.anlmk.networks.entities.response.AccountResponse
import com.vnpay.anlmk.utils.SingleLiveEvent
import com.vnpay.anlmk.utils.UIModel
import com.vnpay.anlmk.utils.extensions.safeLog
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException


abstract class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>().apply { value = false }

    val message = MutableLiveData<String>()
    val noInternetConnectionEvent = SingleLiveEvent<Unit>()
    val connectTimeoutEvent = SingleLiveEvent<Unit>()
    val invalidCertificateEvent = SingleLiveEvent<Unit>()
    val serverErrorEvent = SingleLiveEvent<Unit>()
    val successFinishMessage = MutableLiveData<String>()
    val expireSessionEvent = MutableLiveData<String>()
    val activeAnotherDeviceEvent = MutableLiveData<String>()

    val errorFinishMessage = MutableLiveData<String>()
    val errorToHomeMesssage = MutableLiveData<String>()
    val errorSoftOTPInvalidate = MutableLiveData<String>()
    val firstLogin = MutableLiveData<String>()


    @CallSuper
    override fun onCleared() {
        super.onCleared()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            throwable.safeLog()
            withContext(Dispatchers.Main) {
                when (throwable) {
                    is UnknownHostException -> {
                        noInternetConnectionEvent.call()
                    }
                    is SocketTimeoutException -> {
                        connectTimeoutEvent.call()
                    }
                    is SSLPeerUnverifiedException -> {
                        invalidCertificateEvent.call()
                    }
                    else -> {
                        val baseException = convertToBaseException(throwable)

                        if (baseException.serverErrorResponse == null || TextUtils.isEmpty(
                                baseException.serverErrorResponse.des
                            )
                        ) {
                            serverErrorEvent.call()
                        } else if (isInRespCodes(
                                baseException.code,
                                "DEVICESTATUS-",
                                arrayListOf(
                                    "02",
                                    "03",
                                    "04",
                                    "05",
                                    "06",
                                    "07",
                                    "08",
                                    "10",
                                    "11",
                                    "12"
                                )
                            ) || "SESSION-2" == baseException.code
                        ) {
                            expireSessionEvent.value = baseException.serverErrorResponse.des
                            return@withContext
                        } else if (baseException.code == "DEVICESTATUS-09" || "SESSION-3" == baseException.code) {
                            activeAnotherDeviceEvent.value = baseException.serverErrorResponse.des
                            return@withContext
                        } else if (baseException.code == "DEVICE_ROOT-01") {
                            errorSoftOTPInvalidate.value = baseException.serverErrorResponse.des
                            return@withContext
                        } else {
                            message.value = baseException.serverErrorResponse.des
                        }
                        onError(
                            baseException.mid,
                            baseException.code,
                            baseException.serverErrorResponse
                        )
                    }
                }
                hideLoading()
            }
        }
    }

    private fun isInRespCodes(code: String?, state: String, list: ArrayList<String>): Boolean {
        if (code?.startsWith(state) != true)
            return false
        list.forEach {
            if (state + it == code) {
                return true
            }
        }
        return false
    }

    open fun onError(mid: Int, code: String?, source: ServerResponseEntity?) {
        when (code) {

            "98" -> {
                val res = UIModel.getInstance().provideGson()
                    .fromJson(source!!.source, ServerResponseEntity::class.java)
                expireSessionEvent.value = res.des
                return
            }

        }
    }

    fun hideLoading() {
        isLoading.value = false
    }

    fun showLoading() {
        isLoading.value = true
    }

    val network = viewModelScope + exceptionHandler

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        showLoading()
        return network.launch {
            block.invoke(network)
            withContext(Dispatchers.Main) {
                hideLoading()
            }
            return@launch
        }
    }


}