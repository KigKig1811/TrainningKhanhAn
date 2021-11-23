package com.vnpay.anlmk.ui.adapters.transactions

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.ui.entites.CommonEntity
import com.vnpay.anlmk.utils.extensions.setSafeOnClickListener

/**
 * Created by Lvhieu2017 on 9/12/2017.
 */

class ConfirmAdapter(private val context: Context, private val list: List<CommonEntity>) :
    RecyclerView.Adapter<ConfirmAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == AMOUNT_LAYOUT)
            return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.adapter_confirm_amount_type_layout,
                    parent, false
                )
            )
        if (viewType == ONELINE_ITEM)
            return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.adapter_oneline_type_layout,
                    parent, false
                )
            )
        if (viewType == DETAIL_TRANSACTION)
            return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.adapter_detail_transaction,
                    parent, false
                )
            )
        if (viewType == ONELINE_ITEM_TOP)
            return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.adapter_oneline_top_type_layout,
                    parent, false
                )
            )
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.adapter_confirm_type_layout,
                parent, false
            )
        )

    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getTypeLayout()
    }

    private fun getItem(position: Int): CommonEntity {
        return list[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.getTypeLayout()) {
            DETAIL_TRANSACTION -> {
                //BUTTON VIEW DETAIL, NOT SET
            }
            AMOUNT_LAYOUT -> {
                if (holder.value != null)
                    holder.title!!.setText(item.getTitle())
                if (holder.value != null)
                    holder.value!!.setText(item.getDescript())
                if (holder.value != null)
                    holder.third!!.setText(item.getDataToolTip())
            }

            ONELINE_ITEM_TOP, ONELINE_ITEM -> {
                holder.title!!.text = Html.fromHtml(item.getTitle())

                if (holder.value != null) {
                    holder.value!!.isSelected = item.isHighLight()

                    holder.value!!.text = item.getDescript()
                }
            }

            else -> {
                holder.title!!.text = item.getTitle()
                if (holder.value != null) {
                    holder.value!!.text = item.getDescript()
                    holder.value!!.isSelected = item.isHighLight()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @FindViewer(R.id.title)
        var title: TextView? = null
        @FindViewer(R.id.value)
        var value: TextView? = null
        @FindViewer(R.id.third)
        var third: TextView? = null

        init {
            ParseViewer.getInstance().bind(this, itemView)
            itemView.setSafeOnClickListener {
                val item = getItem(adapterPosition)

            }
        }
    }

    companion object {
        val AMOUNT_LAYOUT = 2
        val ONELINE_ITEM = 3
        val ONELINE_ITEM_TOP = 4
        val DETAIL_TRANSACTION: Int = 5
    }

}
