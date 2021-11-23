package com.vnpay.anlmk.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.Observer
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.Tap
import com.vnpay.anlmk.data.repository.OTPModel
import com.vnpay.anlmk.ui.activities.home.HomeActivity
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.views.Button
import com.vnpay.anlmk.ui.views.NumberOTPView
import org.koin.androidx.viewmodel.ext.android.viewModel

class OPTActivity : BaseActivity() {
    override val titleId: Int = 0

    override val model: OTPModel by viewModel()
    override val layoutId: Int = R.layout.activity_required_otp

        @FindViewer(R.id.txtopt)
        protected var optSMS: NumberOTPView? = null

        @FindViewer(R.id.submit_OTP)
        protected lateinit var btnlogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AAA_OTP","on Create Opt")
        model.apply {
            checkOTPResponse.observe(this@OPTActivity, Observer {
                if (isOTPSuccess(it.code)) {
                    startActivity(
                        Intent(
                            this@OPTActivity,
                            HomeActivity::class.java
                        )
                    )
                    finish()
                }
//                else {
//                    confirm.newBuild()
//                        .setNotice(it.des.toString())
//                        .addButtonLeft(R.string.cancel)
//
//                }
            })
        }
    }

    @Tap(R.id.submit_OTP)
    fun TapSubmit() {

        val otpValue: String = optSMS?.text.toString()
        if (TextUtils.isEmpty(otpValue)) {
            confirm.newBuild()
                .setNotice(getString(R.string.otp_not_value))
        } else if (optSMS != null && otpValue.length < 6) {
            confirm.newBuild().setNotice(R.string.otp_not_enough)
        } else {
            var otpId = intent.getStringExtra("otpId")
            model.loginOTP(otpId, otpValue)

        }
    }
}