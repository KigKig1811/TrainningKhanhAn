package com.vnpay.anlmk.ui.adapters.home

import android.content.Context
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
import kotlinx.android.synthetic.main.adapter_item_homefunction.view.*

class BankingFunctionAdapter(
    private val listOrig: List<FunctionEntity>,
    private val context: Context,

    private val listener: Callback
) :
    RecyclerView.Adapter<BankingFunctionAdapter.ViewHoder>() {

    private val list: MutableList<FunctionEntity>?

    val count: Int
        get() = this.list!!.size

    init {
        list = java.util.ArrayList(listOrig)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {

        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_homefunction, parent, false)
        return ViewHoder(view)
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        val item = getItem(position) as FunctionEntity
        holder.itemView.imgfunction.setImageResource(item.getImage())
        holder.itemView.imgfunction.setBackgroundResource(item.getBackgoundImage())
//        holder.txtfunction.!! text = item . getTitle ().toString()
        holder.txtfunction!!.text = item.getTitle()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    interface Callback {
        fun onClick(item: FunctionEntity)
    }

    fun getItem(i: Int): FunctionEntity {
        return this.list!![i]
    }

    inner class ViewHoder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @FindViewer(R.id.imgfunction)
        var imgFunction: ImageView? = null

        @FindViewer(R.id.txtfunction)
        var txtfunction: TextView? = null

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


