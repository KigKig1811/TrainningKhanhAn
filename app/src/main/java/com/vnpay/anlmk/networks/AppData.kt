package com.vnpay.anlmk.networks

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
//import com.google.android.gms.auth.api.signin.internal.Storage
//import com.google.firebase.iid.FirebaseInstanceId
import com.vnpay.anlmk.data.constants.Tags
import com.vnpay.anlmk.data.b
import com.vnpay.anlmk.di.Common
import com.vnpay.anlmk.ui.activities.home.HomeActivity
import com.vnpay.anlmk.utils.*
import com.vnpay.anlmk.utils.captures.IMoneyReader
import com.vnpay.anlmk.utils.captures.MoneyReader
import com.vnpay.anlmk.utils.captures.MoneyUSReader

import com.vnpay.supersecure.Storage

class AppData {
    var avatar: Bitmap? = null
    var phoneOTP: String? = null
    var cif: String? = null
    var accessKey: String? = null
    var branchCode: String? = null
    var sessionId: String? = null
    var deviceId: Long = 0
    var language: String? = null
    var phone: String? = null
    var hook: Boolean = b.hook
    var rooted: Boolean = b.rooted > 0
    var location: Location? = null
    var fullName: String? = null
    private var currentOTTToken: String? = null

    companion object {
        private var instance: AppData? = null
        fun g(): AppData {
            if (instance == null)
                instance = AppData()
            return instance!!
        }
    }

    fun isLogined(): Boolean {
        return !TextUtils.isEmpty(AppData.g().sessionId)
    }

    fun getAppToken(): String? {

        if (!TextUtils.isEmpty(currentOTTToken))
            return currentOTTToken!!
        try {
//            currentOTTToken = FirebaseInstanceId.getInstanceId().getToken()
        } catch (e: Exception) {
            Log.wtf("EXX", e)
        }
        return currentOTTToken
    }

    fun isHook(): String {
        return if (hook) "1" else "0"
    }

    fun isRooted(): String {
        return if (rooted) "1" else "0"
    }

    fun getGps(): String? {
        return if (location != null) "${location!!.latitude},${location!!.longitude}" else "0,0"
    }

    fun getImei(): String? {
        try {
            return Settings.Secure.getString(
                Common.baseActivity.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
        }
        return "NoAndroidId"
    }


    fun setFirebaseToken(s: String?) {
        currentOTTToken = s
    }


    fun isNotPermission(act: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(
            act,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            act, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

    }

    fun logout() {
        sessionId = null
        val intent = Intent(Common.baseActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        Common.baseActivity.startActivity(intent)
        Common.baseActivity.overridePendingTransition(0, 0);
        ActivityCompat.finishAffinity(Common.baseActivity);
        Common.baseActivity.finish()
    }

    fun getMoneyReader(resources: Resources, amount: String): String {
        if (UIModel.getInstance().isVietNamese()) {
            val moneyReader = MoneyReader(resources)
            return moneyReader.convert(amount)
        } else {
            return MoneyUSReader.newInstance().convert(amount)
        }
    }

    fun getMoneyReader(resources: Resources): IMoneyReader {
        return if (UIModel.getInstance().isVietNamese) {
            MoneyReader(resources)
        } else {
            MoneyUSReader.newInstance()
        }
    }


    private fun clearFingerId() {
        com.vnpay.supersecure.Storage.g().remove(Tags.F_LOGIN)
        com.vnpay.supersecure.Storage.g().remove(Tags.F_FINANCE)
    }

    fun onClearData() {
        phoneOTP = null
        phone = null
        clearFingerId()
        Storage.g().remove(Tags.CLIENT_ID)
        Storage.g().remove(Tags.PHONE)
        instance = null
    }

    fun gotoStore(context: Activity) {
        try {
            val appId = context.getPackageName()
            val rateIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appId")
            )
            var marketFound = false
            val otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0)
            for (otherApp in otherApps) {
                if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {
                    val otherAppActivity = otherApp.activityInfo
                    val componentName = ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                    )
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    rateIntent.component = componentName
                    context.startActivity(rateIntent)
                    marketFound = true
                    break

                }
            }


            if (!marketFound) {
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appId")
                )
                context.startActivity(webIntent)
            }
        } catch (e: Exception) {
            Log.wtf("EX", e)
        }
    }

}
