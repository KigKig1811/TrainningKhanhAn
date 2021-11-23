package com.vnpay.anlmk.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import com.vnpay.anlmk.R
//import com.vnpay.base.BuildConfig
//import com.vnpay.base.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.Tap
import com.vnpay.anlmk.data.repository.LoginModel
import com.vnpay.anlmk.networks.AppData
import com.vnpay.anlmk.ui.activities.home.HomeActivity
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.dialogs.FingerConfirmDialog
import com.vnpay.anlmk.ui.dialogs.LoginFingerDialog
import com.vnpay.anlmk.ui.views.Button
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {
    override val titleId: Int = 0

    override val model: LoginModel by viewModel()

    @FindViewer(R.id.username)
    protected lateinit var username: EditText

    @FindViewer(R.id.password)
    protected var password: EditText? = null

    @FindViewer(R.id.btnlogin)
    protected lateinit var btnlogin: Button

    val fingerVerifyDialog by inject<FingerConfirmDialog>()
    val fingerinitDialog by inject<LoginFingerDialog>()

    override val layoutId: Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.apply {
            loginResponse.observe(this@LoginActivity, Observer {
                val code = it.code
                var otpId=it.otpId
                Log.d("AAA_Login","viewmodel Login")
                if (model.isLoginSuccess(code)) {
                    if (model.isFirstLogin(code)) {
                        var intentFirstLogin = Intent(
                            this@LoginActivity,
                            OPTActivity::class.java
                        )
                        intentFirstLogin.putExtra("otpId",otpId)

                        startActivity(
                            intentFirstLogin
                        )
                        finish()

                    } else {
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                HomeActivity::class.java
                            )

                        )
                        finish()
                    }
                } else {
                    confirm.newBuild()
                        .setNotice(it.des.toString())
                        .addButtonLeft(R.string.cancel)
                }
            })
        }
    }

    private fun initLogin() {
        fingerinitDialog.init(
            object :
                LoginFingerDialog.LoginListener {
                override fun cancelOTP() {
                }

                override fun onValidateLogin(password: String) {
                    if (TextUtils.isEmpty(password)) {
                        confirm.newBuild().setNotice(
                            getString(
                                R.string.error_empty_input_common,
                                getString(R.string.app_password)
                            )
                        ).addButtonRight(R.string.agree) {
                            fingerinitDialog.show()
                            confirm.dismiss()
                        }.setAction(true)
                    } else if (password.length < 6) {
                        confirm.newBuild()
                            .setNotice(getString(R.string.error_password_not_enough))
                            .addButtonRight(R.string.agree) {
                                fingerinitDialog.show()
                                confirm.dismiss()
                            }.setAction(true)
                    } else
                        model.login(AppData.g().phone!!, password, true)
                }

            })
        fingerinitDialog.show()
    }

    @Tap(R.id.btnlogin)
    fun TapSubmit() {
        Log.d("AAA","tapsubmit")
        val usernamevalue = username.text.toString()
        val passwordValue: String = password?.text.toString()
        if (TextUtils.isEmpty(usernamevalue)) {
            confirm.newBuild()
                .setNotice(getString(R.string.username_not_value))
        } else if (password != null && TextUtils.isEmpty(passwordValue)) {
            confirm.newBuild().setNotice(getString(R.string.password_not_value))

        } else if (password != null && passwordValue.length < 6) {
            confirm.newBuild().setNotice(R.string.error_password_not_enough)
        } else {
            model.login(usernamevalue!!, passwordValue)
        }
        Log.d("AAA",usernamevalue.toString())

    }
}