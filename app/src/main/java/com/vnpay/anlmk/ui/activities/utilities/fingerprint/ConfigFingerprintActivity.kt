package com.vnpay.anlmk.ui.activities.utilities.fingerprint

import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import com.vnpay.anlmk.R
import com.vnpay.anlmk.data.constants.Tags
import com.vnpay.anlmk.data.repository.LoginModel
import com.vnpay.anlmk.ui.activities.home.HomeActivity
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.dialogs.FingerConfirmDialog
import com.vnpay.anlmk.ui.dialogs.LoginFingerDialog
import kotlinx.android.synthetic.main.activity_config_finger.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigFingerprintActivity : BaseActivity() {
    override val titleId: Int = R.string.finger_config
    override val model: LoginModel by viewModel()
    override val layoutId: Int = R.layout.activity_config_finger
    val fingerVerifyDialog by inject<FingerConfirmDialog>()
    val fingerinitDialog by inject<LoginFingerDialog>()
    val eventFingerLogin = object :
        LoginFingerDialog.LoginListener {
        override fun cancelOTP() {
            checkState()
        }

        override fun onValidateLogin(password: String) {
            if(TextUtils.isEmpty(password)){
                confirm.newBuild().setNotice(getString(R.string.error_empty_input_common, getString(R.string.app_password))).addButtonRight(R.string.agree){
                    fingerinitDialog.show()
                    confirm.dismiss()
                }.setAction(true)
            }else if(password.length < 6){
                confirm.newBuild().setNotice(getString(R.string.error_password_not_enough)).addButtonRight(R.string.agree){
                    fingerinitDialog.show()
                    confirm.dismiss()
                }.setAction(true)
            }
            else
                model.callRegisterFinger(
                    password,
                    if (finger_finance.isSelected) "1" else "0"
                )
        }
    }
    val eventFingerVerify = object : FingerConfirmDialog.OutsideDialog {
        override fun useKeyBoard() {

        }

        override fun onOutsite() {
            model.finger!!.stopListening()
            checkState()
        }

    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        model.apply {
            initFinger()
            finger_login.isChecked = model.isFingerActived()
            finger_finance.isChecked = model.isFingerFinaneActived()
            if(intent.getBooleanExtra(Tags.IS_FROM_LOGIN, false))
                confirm.newBuild().setNotice(R.string.success_reg_finger)
            fingerLive.observe(this@ConfigFingerprintActivity, Observer {
                when (it) {
                    LoginModel.FINGER_SHOWPOUP -> {
                        fingerVerifyDialog
                            .setTypeDialog(FingerConfirmDialog.CONFIG_TYPE)
                            .setEvent(eventFingerVerify)
                            .show()
                    }
                    LoginModel.FINGER_SUCCESS -> {
                        fingerVerifyDialog.dismissNoAction()
                        fingerinitDialog.init(eventFingerLogin)
                        fingerinitDialog.show()
                    }
                    LoginModel.CHECK_STATE_FINGER,
                    LoginModel.NEW_ENROLLED -> {
                        checkState()
                        fingerVerifyDialog.dismissNoAction()
                    }
                    LoginModel.FINGER_DONE -> {
                        confirm.newBuild()
                            .setNotice(if (finger_finance.isSelected) R.string.success_finance_finger else
                                R.string.success_login_finger)
                        checkState()
                    }
                    else -> {
//                        confirm.newBuild().setNotice(it)
                        if (fingerVerifyDialog != null && fingerVerifyDialog.isShowing)
                            fingerVerifyDialog.setStatusFailed(it)
                    }
                }
            })

        }
        if(isTaskRoot)
            confirm.newBuild().setNotice(R.string.success_reg_finger)
        pullAction()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(isTaskRoot)
            startActivity(Intent(this, HomeActivity::class.java))
    }
    override fun onPause() {
        super.onPause()
        if (model.finger != null && model.finger!!.isStarting) {

            model.finger!!.stopListening()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (model.finger != null && !model.finger!!.isFingerprintAuthAvailable(model.finger!!.ALL_FINGER)) {
                model.clearFingerId()
            } else if (model.finger  != null && model.finger!!.isChangedFinger) {
                model.clearFingerId()
            } else if (model.finger != null && model.finger!!.isNeedResumeFinger) {
                model.startFingerPrint()
            }
        }
        checkState()

    }


    private fun checkState() {
        finger_login.setOnCheckedChangeListener(null)
        finger_finance.setOnCheckedChangeListener(null)
        finger_login.isChecked = model.isFingerActived()
        finger_finance.isChecked = model.isFingerFinaneActived()
        pullAction()
    }

    private fun pullAction() {
        finger_login.setOnCheckedChangeListener(loginListener)
        finger_finance.setOnCheckedChangeListener(financeListener)
    }

    private val loginListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            finger_finance.isSelected = false
            finger_login.isSelected = true
            model.startFingerPrint()
        } else {
            confirm.newBuild().setNotice(R.string.decide_cancel_login_finger)
                .addButtonLeft(R.string.cancel) {
                    checkState()
                    confirm.dismiss()
                }
                .addButtonRight {
                    model.removeLoginFinger()
                    confirm.newBuild().setNotice(R.string.success_remove_finger_login)
                }
                .setAction(true)
        }
    }

    private val financeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            finger_finance.isSelected = true
            finger_login.isSelected = false
            model.startFingerPrint()
        } else {
            confirm.newBuild().setNotice(R.string.decide_cancel_finance_finger)
                .addButtonLeft(R.string.cancel) {
                    checkState()
                    confirm.dismiss()
                }
                .addButtonRight {
                    model.removeFinanceFinger()
                    confirm.dismiss()
                }
                .setAction(true)
        }
    }
}
