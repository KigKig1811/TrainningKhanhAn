package com.vnpay.anlmk.ui.adapters.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.ui.entites.FunctionEntity
import com.vnpay.anlmk.ui.views.TextView
import kotlinx.android.synthetic.main.adapter_item_homefunction.view.*
import kotlinx.android.synthetic.main.adapter_item_oneline_homefunction.view.*

class FinanceFunctionAdapter(
    private val listFunction: List<FunctionEntity>
) : RecyclerView.Adapter<FinanceFunctionAdapter.ViewHoder>() {

    private val list: MutableList<FunctionEntity>?

    val count: Int
        get() = this.list!!.size

    init {
        list = java.util.ArrayList(listFunction)
    }

    fun getItem(i: Int): FunctionEntity {
        return this.list!![i]
    }

    class ViewHoder(items: View) : RecyclerView.ViewHolder(items) {
        @FindViewer(R.id.txtvalue)
        var function: TextView? = null

        @FindViewer(R.id.ic_function)
        var imgFunction: ImageView? = null

        init {
            ParseViewer.getInstance().bind(this, itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHoder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_oneline_homefunction, parent, false)
        return ViewHoder(view)
    }

    override fun onBindViewHolder(holder: ViewHoder, position: Int) {
        val item = getItem(position)
        holder.function!!.text = item.getTitle()
        holder.imgFunction!!.setBackgroundResource(item.getBackgoundImage())
        holder.imgFunction!!.setImageResource(item.getImage())

    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}