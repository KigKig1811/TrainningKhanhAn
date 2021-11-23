package com.vnpay.anlmk.ui.adapters.transfer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.networks.entities.AccountEntity
import com.vnpay.anlmk.ui.views.TextView

class InternalTransferAdapter(val context: Context, var dataSource: List<AccountEntity>): BaseAdapter() {

    override fun getCount(): Int {
        if(this.dataSource==null)
        {
            return 0
        }
        return this.dataSource.size
    }

    override fun getItem(position: Int): Any? {
        return dataSource.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_account_number, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.accountNumber?.text=dataSource.get(position).accountNo

        return view
    }

    private class ItemHolder(row: View) {
        @FindViewer(R.id.account_number)
        var accountNumber: TextView?=null
        init {
            ParseViewer.getInstance().bind(this, row)
        }


    }
}
