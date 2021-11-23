package com.vnpay.anlmk.ui.activities.Account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
//import com.vnpay.base.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.data.repository.AccountModel
import com.vnpay.anlmk.networks.AppData
import com.vnpay.anlmk.ui.activities.login.LoginActivity
import com.vnpay.anlmk.ui.adapters.account.AccountAdapter
import com.vnpay.anlmk.ui.bases.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountActivity() : BaseActivity() {
    override val titleId: Int = R.string.account

    override val model: AccountModel by viewModel()
    override val layoutId: Int = R.layout.activity_account


    @FindViewer(R.id.rcvPayAccount)
    protected lateinit var rcvPayAccount: RecyclerView

    @FindViewer(R.id.rcvSaveAccount)
    protected lateinit var rcvSaveAccount: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sessionId = AppData.g().sessionId.toString()

        rcvPayAccount.layoutManager = LinearLayoutManager(this)
        rcvSaveAccount.layoutManager = LinearLayoutManager(this)

        model.apply {
            account()

            accountResponse.observe(this@AccountActivity, Observer {
                if(isSuccess(it.code))
                {
                    var list = it.list
                    setListAccount(list)
                }
                else if (isExpire(it.code)) {
                    confirm.newBuild().setNotice(it.des.toString()).addButtonRight {
                        startActivity(
                            Intent(
                                this@AccountActivity,
                                LoginActivity::class.java
                            ).addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            )
                        )
                        ActivityCompat.finishAffinity(this@AccountActivity)
                        setResult(Activity.RESULT_OK)
                        finish()
                        confirm.dismiss()
                    }.setAction(true)
                } else {
                    confirm.newBuild()
                        .setNotice(it.des.toString())
                        .addButtonLeft(R.string.cancel)
                }

            })
            payAccountResponse.observe(this@AccountActivity, Observer {
                rcvPayAccount.adapter = AccountAdapter(it)
                rcvPayAccount.adapter?.notifyDataSetChanged()
            })
            saveAccountResponse.observe(this@AccountActivity, Observer {
                rcvSaveAccount.adapter = AccountAdapter(it)
                rcvSaveAccount.adapter?.notifyDataSetChanged()
            })


        }
    }

}