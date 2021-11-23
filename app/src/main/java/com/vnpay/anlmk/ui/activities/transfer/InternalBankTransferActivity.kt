package com.vnpay.anlmk.ui.activities.transfer

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.text.TextUtils
import android.widget.Spinner
import androidx.lifecycle.Observer
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.Tap
import com.vnpay.anlmk.data.repository.InternalTransferModel
import com.vnpay.anlmk.networks.entities.AccountEntity
import com.vnpay.anlmk.ui.adapters.transfer.InternalTransferAdapter
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.views.Button
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.vnpay.anlmk.utils.NumberTextWatcher


class InternalBankTransferActivity : BaseActivity() {

    override val titleId: Int = R.string.content_internal_transfer_activity

    override val model: InternalTransferModel by viewModel()

    override val layoutId: Int = R.layout.activity_internal_bank_transfer

    @FindViewer(R.id.myaccount)
    protected lateinit var sendAccount: Spinner

    @FindViewer(R.id.receiver_account)
    protected var receiverAccount: com.vnpay.anlmk.ui.views.EditText? = null

    @FindViewer(R.id.valueofmoney)
    protected var money: com.vnpay.anlmk.ui.views.EditText? = null

    @FindViewer(R.id.contentofnote)
    protected var noteTransfer: com.vnpay.anlmk.ui.views.EditText? = null

    @FindViewer(R.id.init_internal_transfer)
    protected lateinit var btnInitInteralTransfer: Button

    //    private val blockCharacterSet = "~#^|$%&*!"
//    private val filter =
//        InputFilter { source, start, end, dest, dstart, dend ->
//            if (source != null && blockCharacterSet.contains("" + source)) {
//                ""
//            } else null
//        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        money!!.addTextChangedListener(money?.let { NumberTextWatcher(it) })

        model.apply {
            account()
            payAccountResponse.observe(this@InternalBankTransferActivity, Observer {
                sendAccount.adapter =
                    InternalTransferAdapter(this@InternalBankTransferActivity, it)
                sendAccount.setSelection(0)
            })

            transferResponse.observe(this@InternalBankTransferActivity, Observer {
                if (isTransferSuccess(it.code)) {
                    var intent = Intent(
                        this@InternalBankTransferActivity,
                        ConfirmInternalTransferActivity::class.java
                    )

                    intent.putExtra("otpId", it.otpId)
                    intent.putExtra("beneName", it.beneName)
                    intent.putExtra("fee", it.fee)
                    var selectionItem = sendAccount.selectedItem as AccountEntity
                    intent.putExtra("accountFrom", selectionItem.accountNo)
                    intent.putExtra("accountTo", receiverAccount?.text.toString())
                    intent.putExtra("amount", money?.text.toString())
                    intent.putExtra("content", noteTransfer?.text.toString())
                    startActivity(intent)

                } else {
                    confirm.newBuild()
                        .setNotice(it.des.toString())
                        .addButtonLeft(R.string.cancel)
                }
            })
        }
    }

    @Tap(R.id.init_internal_transfer)
    fun inintTransfer() {
        val receiverAccountValue = receiverAccount?.text.toString()
        val valueofmoney: String = money?.text.toString()
        val noteTransferValue: String = noteTransfer?.text.toString()
        if (TextUtils.isEmpty(receiverAccountValue)) {
            confirm.newBuild()
                .setNotice(getString(R.string.receiverAccount_not_value))
        } else if (receiverAccount != null && receiverAccountValue.length < 6) {
            confirm.newBuild().setNotice(getString(R.string.receiverAccount_not_enough))

        } else if (valueofmoney.isEmpty()) {
            confirm.newBuild().setNotice(R.string.money_not_value)
        } else if (noteTransferValue.isEmpty()) {
            confirm.newBuild().setNotice(R.string.note_not_value)
        } else {
            var selectionItem = sendAccount.selectedItem as AccountEntity
            var ccy = selectionItem.ccy
            var account = selectionItem.accountNo
            model.transfer(
                account,
                ccy,
                receiverAccountValue,
                valueofmoney,
                noteTransferValue
            )
        }
    }
}