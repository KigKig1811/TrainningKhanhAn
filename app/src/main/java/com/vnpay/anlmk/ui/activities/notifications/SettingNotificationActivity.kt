package com.vnpay.anlmk.ui.activities.notifications

import android.os.Bundle
import com.vnpay.anlmk.R
import com.vnpay.anlmk.data.constants.Tags
import com.vnpay.anlmk.data.repository.HomeModel
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.supersecure.Storage
import kotlinx.android.synthetic.main.activity_bank_notification_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingNotificationActivity  : BaseActivity() {
    override val titleId: Int = R.string.setting_notification

    override val model: HomeModel by viewModel()

    override val layoutId: Int = R.layout.activity_bank_notification_settings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btn_active.isChecked = Storage.g().getBoolean(
            Tags.HIDE_NOTIFICATION,
            true
        )
        btn_active.setOnCheckedChangeListener { _, isChecked ->
            Storage.g().setBoolean(Tags.HIDE_NOTIFICATION, isChecked)
        }
    }
}