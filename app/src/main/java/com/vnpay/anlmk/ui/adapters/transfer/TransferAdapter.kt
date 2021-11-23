package com.vnpay.anlmk.ui.adapters.transfer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.ui.entites.FunctionEntity

class TransferAdapter(  // val list: ArrayList<Any>,
    private val listOrig: List<FunctionEntity>,
    private val listener: Callback
) : RecyclerView.Adapter<TransferAdapter.ViewHoder>() {

    private val list: MutableList<FunctionEntity>?

    val count: Int
        get() = this.list!!.size

    init {
        list = java.util.ArrayList(listOrig)
    }

    fun getItem(i: Int): FunctionEntity {
        return this.list!![i]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {

        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_transfer_function, parent, false)

        return ViewHoder(view)
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        val item = getItem(position) as FunctionEntity
        holder.txtfunction!!.text = item.getTitle()
        holder.imgFunction!!.setBackgroundResource(item.getBackgoundImage())
        holder.imgFunction!!.setImageResource(item.getImage())

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    interface Callback {
        fun onClick(item: FunctionEntity)
    }

    inner class ViewHoder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @FindViewer(R.id.txttransfer)
        var txtfunction: TextView? = null

        @FindViewer(R.id.ic_transfer_function)
        var imgFunction: ImageView? = null

        init {
            ParseViewer.getInstance().bind(this, itemView)
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (listener != null && pos >= 0)
                    listener.onClick(getItem(pos))

            }
        }
    }
}
