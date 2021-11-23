package com.vnpay.anlmk.data

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import com.google.firebase.messaging.RemoteMessage
import com.vnpay.anlmk.di.Common
import com.vnpay.anlmk.ui.activities.notifications.BankNotificationActivity
import com.vnpay.ott.MessageEntity
import com.vnpay.ott.interfaces.AppCallBack
import java.io.File

class MyOTTCallBack : AppCallBack{
    override fun onRealtimeMessage(p0: MessageEntity, p1: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onInitDone() {
    }

    override fun onNewMessages(p0: MutableList<MessageEntity>) {
    }

    override fun onChangeCounterBank(p0: Int) {
    }

    override fun getCurrentContext(): Context {
        return Common.baseActivity
    }

    override fun onDeleteMessage(result: Boolean, des: String?, messId: String?) {
        try {
            if (Common.baseActivity is BankNotificationActivity) {
                (Common.baseActivity as BankNotificationActivity).onResultRemove(result)
            }
        } catch (e: Exception) {
        }

    }

    override fun onResponseAvatar(s: String?) {

    }

    fun OTTui(act: Activity?) {
        if(act!=null)
        Common.baseActivity = act
    }

    override fun onDeleteAllMessage(p0: Boolean, p1: String?) {

    }

    override fun onBeautyContentMessage(p0: Context?, p1: MessageEntity?): String {
        TODO("Not yet implemented")
    }

    override fun isHideNotification(p0: MessageEntity?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onFirebaseMessage(p0: Context?, p1: RemoteMessage?) {
        TODO("Not yet implemented")
    }

    override fun isDontNeedSendWelcomOTT(): Boolean {
        TODO("Not yet implemented")
    }

    override fun doAppSelfUploadAvatar(p0: File?) {
        TODO("Not yet implemented")
    }

    override fun getTypeFaces(p0: Boolean): Typeface {
        TODO("Not yet implemented")
    }

    override fun onNewToken(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onRemoveAvatarSuccess() {
    }

}
