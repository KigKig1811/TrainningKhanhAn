package com.vnpay.anlmk.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import com.vnpay.anlmk.R
import com.vnpay.anlmk.data.b
import com.vnpay.anlmk.networks.AppData
import com.vnpay.anlmk.ui.activities.home.HomeActivity
import com.vnpay.anlmk.ui.activities.login.LoginActivity
import com.vnpay.anlmk.ui.bases.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

open class SplashActivity : BaseActivity() {
    override val titleId: Int = 0
    override val layoutId: Int = 0
    override val model: SplashModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot) {
            finish()
            return
        }
        b.c(this)

        model.install()

        if (AppData.g().hook) {
            confirm.newBuild()
                .setNotice(getString(R.string.alert_root_device_hook))
                .addButtonRight{
                    nextScene()
                    confirm.dismiss()
                }.setAction(true)
        } else if (AppData.g().rooted) {
            confirm.newBuild().setNotice(getString(R.string.label_isRooted))
                .addButtonRight{
                    nextScene()
                    confirm.dismiss()
                }.setAction(true)
        } else
            nextScene()

    }

    private fun nextScene() {
        startActivity(
            Intent(
                this,
                if (model.isActived()) HomeActivity::class.java else LoginActivity::class.java
            )
        )
        finish()
    }
}