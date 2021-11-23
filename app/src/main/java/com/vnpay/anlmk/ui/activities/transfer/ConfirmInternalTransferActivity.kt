package com.vnpay.anlmk.ui.activities.transfer

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.Tap
import com.vnpay.anlmk.data.repository.ConfirmInternalTransferModel
import com.vnpay.anlmk.ui.adapters.transfer.ConfirmInternalTransferAdapter
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.views.Button
import com.vnpay.anlmk.ui.views.NumberOTPView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable
import android.app.Activity
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.app.ActivityCompat
import com.vnpay.anlmk.ui.activities.login.LoginActivity


class ConfirmInternalTransferActivity : BaseActivity() {

    override val titleId: Int = R.string.content_confirm_internal_transfer_activity

    override val model: ConfirmInternalTransferModel by viewModel()
    override val layoutId: Int = R.layout.activity_confirm_internal_transfer

    @FindViewer(R.id.rcv_info_transfer)
    protected lateinit var rcvInfoTransfer: RecyclerView

    @FindViewer(R.id.valueopt)
    protected var valueOTP: NumberOTPView? = null

    @FindViewer(R.id.btn_cancel)
    protected lateinit var btnCancel: Button

    @FindViewer(R.id.btn_next)
    protected lateinit var btnNext: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rcvInfoTransfer.layoutManager = LinearLayoutManager(this)
        model.setInfoTransfer(intent)
        model.apply {
            checkOTPResponse.observe(this@ConfirmInternalTransferActivity, Observer {
                if (isOTPSuccess(it.code)) {
                    var intentConfirm = Intent(
                        this@ConfirmInternalTransferActivity,
                        SuccessActivity::class.java
                    )
                    var transferValue = intent.getStringExtra("amount")
                    intentConfirm.putExtra("TransferValue", transferValue)
                    model.infoTransferLive.observe(this@ConfirmInternalTransferActivity, Observer {
                        intentConfirm.putExtra("InfoTransfer", it as Serializable)
                    })
                    startActivity(intentConfirm)

                } else if (isExpire(it.code)) {
                    confirm.newBuild().setNotice(it.des.toString()).addButtonRight {
                        startActivity(
                            Intent(
                                this@ConfirmInternalTransferActivity,
                                LoginActivity::class.java
                            ).addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            )
                        )
                        ActivityCompat.finishAffinity(this@ConfirmInternalTransferActivity)
                        setResult(Activity.RESULT_OK)
                        finish()
                        confirm.dismiss()
                    }.addButtonLeft(R.string.cancel).setAction(true)
                } else {
                    confirm.newBuild()
                        .setNotice(it.des.toString())
                        .addButtonLeft(R.string.cancel)
                }
            })
            infoTransferLive.observe(this@ConfirmInternalTransferActivity, Observer {
                rcvInfoTransfer.adapter = ConfirmInternalTransferAdapter(it)
                rcvInfoTransfer.adapter?.notifyDataSetChanged()
            })
        }
    }

    @Tap(R.id.btn_next)
    fun ConfirmTransfer() {

        val otpValue: String = valueOTP?.text.toString()
        if (TextUtils.isEmpty(otpValue)) {
            confirm.newBuild()
                .setNotice(getString(R.string.otp_not_value))
        } else if (valueOTP != null && otpValue.length < 6) {
            confirm.newBuild().setNotice(R.string.otp_not_enough)
        } else {
            var otpId = intent.getStringExtra("otpId")

            model.confirmOTP(otpId, otpValue)

        }
    }

    @Tap(R.id.btn_cancel)
    fun cancelTransfer() {
        var intent = Intent(this, InternalBankTransferActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
