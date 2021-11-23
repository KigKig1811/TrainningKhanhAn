package com.vnpay.anlmk.ui.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.ui.entites.CommonEntity

class ListAdapter(private val context: Context, private val list: List<CommonEntity>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType== HEADER_TYPE)
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_header_type, parent, false))
        if(viewType== ACCOUNT_LAYOUT)
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_confirm_account, parent, false))
        if(viewType== ONELINE_TYPE)
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_oneline_type_layout, parent, false))
        if(viewType== TITLE_STATE_TYPE)
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_title_error_approval_layout, parent, false))
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_confirm_normal, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getTypeLayout()
    }

    private fun getItem(position: Int): CommonEntity {
        return list[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if(holder.title!=null) {
            holder.title!!.text = item.getTitle()
        }
        if(getItemViewType(position)==TITLE_STATE_TYPE){
            if(TextUtils.isEmpty(item.getDescript()))
                holder.value!!.visibility = View.GONE
            else {
                holder.value!!.text = getStateName(holder.value!!.resources, item.getDescript() ?: "")
                getStateName(holder.value!!, item.getDescript() ?: "")
            }
        }else
        if(holder.value!=null) {
            holder.value!!.text = item.getDescript()
            holder.value!!.isSelected = item.isHighLight()
        }

    }

    fun getStateName(res: Resources, state:String):String{
        return when(state){
            "01"->res.getString(R.string.type1)
            "02"->res.getString(R.string.type2)
            "03"->res.getString(R.string.type3)
            "00",
            "04"->res.getString(R.string.type4)
            "05"->res.getString(R.string.type5)
            "06"->res.getString(R.string.type6)
            "07"->res.getString(R.string.type7)
            "08"->res.getString(R.string.type8)
            else->  "NotSet"
        }
    }

    fun getStateName(text: TextView, state:String){

        text.getBackground().setColorFilter(
            ContextCompat.getColor(text.context,
                when(state){
                    "01"->R.color.type1
                    "02"->R.color.type2
                    "03"->R.color.type3
                    "00",
                    "04"->R.color.type4
                    "05"->R.color.type5
                    "06"->R.color.type6
                    "07"->R.color.type7
                    else->R.color.type1
                }
            ), PorterDuff.Mode.SRC_ATOP);
    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @FindViewer(R.id.title)
        var title: TextView? = null
        @FindViewer(R.id.value)
        var value: TextView? = null

        init {
            ParseViewer.getInstance().bind(this, itemView)
        }
    }

    companion object {
        val TITLE_TRANSACTION_TYPE: Int = 21
        val NORMAL_TYPE = 1
        val ACCOUNT_LAYOUT: Int = 11
        val ONELINE_TYPE = 31
        val HEADER_TYPE = 41
        val TITLE_STATE_TYPE = 51

    }
}
