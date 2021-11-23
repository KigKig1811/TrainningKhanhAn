package com.vnpay.anlmk.ui.activities.transfer

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.data.repository.TransferModel
import com.vnpay.anlmk.ui.adapters.transfer.TransferAdapter
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.entites.FunctionEntity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferActivity : BaseActivity(), TransferAdapter.Callback {
    override val titleId: Int = R.string.content_transfer_activity
    override val model: TransferModel by viewModel()
    override val layoutId: Int = R.layout.activity_transfer

    @FindViewer(R.id.rcv_transfer)
    protected lateinit var rcvTransfer: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rcvTransfer.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        model.apply {
            addFunctionTransfer()
            functionLive.observe(this@TransferActivity, Observer {
                rcvTransfer.adapter = TransferAdapter(model.listFunction, this@TransferActivity)
                rcvTransfer.adapter?.notifyDataSetChanged()
            })
        }
    }

    override fun onClick(item: FunctionEntity) {
        when (item.getId()) {
            1 -> {
                var intent = Intent(
                    this,
                    InternalBankTransferActivity::class.java
                )

                startActivity(
                    intent

                )

            }

        }

    }
}