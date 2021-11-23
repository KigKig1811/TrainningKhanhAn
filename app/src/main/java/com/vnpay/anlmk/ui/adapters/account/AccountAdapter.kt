package com.vnpay.anlmk.ui.adapters.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.networks.entities.AccountEntity
import java.util.ArrayList

class AccountAdapter(var listAccount: List<Any>) :
    RecyclerView.Adapter<AccountAdapter.ViewHoder>() {


    private val list: MutableList<Any>?

    val count: Int
        get() = this.list!!.size

    init {
        list = ArrayList(listAccount)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_account, parent, false)
        return ViewHoder(view)
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        val item=getItem(position) as AccountEntity
        holder.numberAccount!!.text=item.accountNo
        holder.accountbalance!!.text=item.balance


    }
    fun getItem(i: Int): Any {
        return this.list!![i]
    }


    override fun getItemCount(): Int {
        return list!!.size
    }
    class ViewHoder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @FindViewer(R.id.txtnumberaccount)
        var numberAccount: com.vnpay.anlmk.ui.views.TextView? = null

        @FindViewer(R.id.valueaccountbalance)
        var accountbalance: com.vnpay.anlmk.ui.views.TextView? = null


        init {
            ParseViewer.getInstance().bind(this, itemView)
        }
    }

}