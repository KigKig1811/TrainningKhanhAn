package com.vnpay.anlmk.ui.adapters.transactions

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.ui.entites.CommonEntity
import com.vnpay.anlmk.utils.UIModel
import java.util.*

class SearchItemAdapter(
    private val context: Context,
    private val listOrig: List<Any>,
    private val listener: Callback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    companion object {
        val BANK_NAPAS_TYPE = 2
        val ONE_LINE = 9
        val TEMPLATE_TYPE = 10
    }


    private var selected: Any? = null

    private val list: MutableList<Any>?

    val count: Int
        get() = this.list!!.size

    init {
        list = ArrayList(listOrig)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ONE_LINE -> return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.adapter_item_one_line_dialog,
                    parent,
                    false
                )
            )
            else -> return ViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.adapter_item_in_dialog,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holderOrig: RecyclerView.ViewHolder, position: Int) {
        val tt = getItem(position)
        val holder = holderOrig as ViewHolder
        if (holder.icon != null && selected != null) {
            holder.icon!!.isSelected = tt === selected
        } else if (holder.title != null) {
            holder.title!!.isSelected = tt === selected
        }
        if (tt is String) {
            val item = tt as String
            holder.title!!.text = item
        } else if (tt is CommonEntity) {
            val item = tt as CommonEntity
            if (holder.descript != null) {
                holder.descript!!.text = item.getDescript()
                holder.descript!!.setTextColor(ContextCompat.getColor(context, R.color.color_hint))
            }
            if (item.getIcon() != 0)
                holder.descript!!.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        context,
                        item.getIcon()
                    ), null, null, null
                )
            if (TextUtils.isEmpty(item.getTitle())) {
                holder.title!!.visibility = View.GONE
            } else {
                holder.title!!.visibility = View.VISIBLE
                holder.title!!.text = item.getTitle()
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val tt = getItem(position)
        if (tt is CommonEntity) {
            return (tt as CommonEntity).getTypeLayout()
        }
        return if (tt is String ) ONE_LINE else 0
    }

    override fun getItemCount(): Int {
        return list!!.size
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                if (TextUtils.isEmpty(constraint)) {
                    return null
                } else {
                    val results = FilterResults()
                    val temps = ArrayList<Any>()
                    val key = Normalization(constraint.toString())
                    for (ob in listOrig) {
                        if (ob is String) {
                            val item = ob as String
                            if (Normalization(item).contains(key) || Normalization(item).contains(
                                    key
                                )
                            ) {
                                temps.add(ob)
                            }
                        }else if (ob is CommonEntity) {
                            val item = ob as CommonEntity
                            if (Normalization(item.getTitle()).contains(key) || Normalization(
                                    item.getDescript()
                                ).contains(key)
                            ) {
                                temps.add(ob)
                            }
                        }
                    }
                    results.values = temps
                    return results
                }
            }

            override fun publishResults(arg0: CharSequence?, results: FilterResults?) {
                list!!.clear()
                if (results?.values == null) {
                    list.addAll(listOrig)
                } else {
                    list.addAll(results.values as ArrayList<Any>)
                }
                listener.show(list.isNotEmpty())
                notifyDataSetChanged()
            }
        }
    }

    private fun Normalization(str: String?): String {
        var str: String? = str ?: return ""
        str = UIModel.getInstance().removeAccentNormalize(str)
        return str
    }

    fun getItem(i: Int): Any {
        return this.list!![i]
    }

    fun setSelected(selected: Any?) {
        var selected = selected
        if (selected == null && list != null && list.isNotEmpty()) {
            selected = list[0]
        }
        this.selected = selected
    }

    interface Callback {
        fun show(isShown: Boolean)

        fun onClick(item: Any)

        fun dismiss()

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @FindViewer(R.id.title)
        var title: TextView? = null
        @FindViewer(R.id.descript)
        var descript: TextView? = null
        @FindViewer(R.id.icon)
        var icon: ImageView? = null


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
