package com.vnpay.anlmk.ui.adapters.transfer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.ui.entites.InfoConfirmEmtity
import com.vnpay.anlmk.ui.views.EditText
import com.vnpay.anlmk.ui.views.TextView
import java.util.ArrayList

class ConfirmInternalTransferAdapter(
    var listinfo: List<InfoConfirmEmtity>
) :
    RecyclerView.Adapter<ConfirmInternalTransferAdapter.ViewHoder>() {
    private val list: MutableList<InfoConfirmEmtity>?

    val count: Int
        get() = this.list!!.size

    init {
        list = ArrayList(listinfo)
    }

    fun getItem(i: Int): InfoConfirmEmtity {
        return this.list!![i]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_info_confirm_internal_transfer, parent, false)
        return ViewHoder(view)
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        val item = getItem(position) as InfoConfirmEmtity
        holder.title!!.text = item.getTitle()
        holder.value!!.setText(item.getValue())
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHoder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @FindViewer(R.id.title_info_transfer)
        var title: TextView? = null

        @FindViewer(R.id.value_info_transfer)
        var value: EditText? = null

        init {
            ParseViewer.getInstance().bind(this, itemView)
        }
    }
}