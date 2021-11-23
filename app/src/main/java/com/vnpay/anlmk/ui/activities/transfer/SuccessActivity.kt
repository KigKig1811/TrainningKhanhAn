package com.vnpay.anlmk.ui.activities.transfer

import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.Tap
import com.vnpay.anlmk.data.repository.SuccessTransferModel
import com.vnpay.anlmk.ui.adapters.transfer.ConfirmInternalTransferAdapter

import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.views.Button
import com.vnpay.anlmk.ui.views.TextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuccessActivity : BaseActivity() {
    override val titleId: Int = R.string.toolbarsuccess
    override val model: SuccessTransferModel by viewModel()
    override val layoutId: Int = R.layout.activity_transfer_result

    @FindViewer(R.id.rcv_info_transfer)
    protected lateinit var rcvInfoTransfer: RecyclerView

    @FindViewer(R.id.tv_money_transfer)
    protected var transferValue: TextView? = null

    @FindViewer(R.id.new_transaction)
    protected lateinit var btnNewTransaction: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rcvInfoTransfer.layoutManager = LinearLayoutManager(this)

        model.apply {
            setTransferInfo(intent)
            setTransferValue(intent)
            transferInfoLive.observe(this@SuccessActivity, Observer {
                rcvInfoTransfer.adapter = ConfirmInternalTransferAdapter(it)
                rcvInfoTransfer.adapter?.notifyDataSetChanged()
            })
            transferValueLive.observe(this@SuccessActivity, Observer {
                transferValue!!.text = it
            })

        }
    }

    @Tap(R.id.new_transaction)
    fun NewTransaction() {

        // set the clear flags
        var intent = Intent(this, InternalBankTransferActivity::class.java)
        intent.flags= FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

}