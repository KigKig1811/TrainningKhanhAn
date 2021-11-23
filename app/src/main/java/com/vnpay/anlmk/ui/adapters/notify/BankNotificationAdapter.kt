package com.vnpay.anlmk.ui.adapters.notify

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import com.vnpay.anlmk.R
import com.vnpay.anlmk.data.repository.HomeModel
import com.vnpay.anlmk.ui.activities.notifications.BankNotificationActivity
import com.vnpay.anlmk.utils.UIModel
import com.vnpay.anlmk.utils.extensions.setSafeOnClickListener
import com.vnpay.ott.*
import java.util.*
import kotlin.collections.ArrayList


class BankNotificationAdapter(
    val context: Context,
    val list: ArrayList<MessageEntity>,
    val items: RecyclerView,
    val model: HomeModel,
    var typeSelected: Int = ALL
) : RecyclerView.Adapter<BankNotificationAdapter.CommonViewHolder>(), Filterable {


    companion object {
        val ALL = 0
        val TRANSACTION = 1
        val OTHER = 2

        val TILTE_TYPE = 18525
        val REMOVED_TYPE = 18526
        val END_TYPE = 18527


    }

    private val listOrig: List<MessageEntity>
    private val SEPERATOR_NEW_ELEMENT = String(byteArrayOf(1.toByte()))


    init {
        listOrig = ArrayList<MessageEntity>(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        if (viewType == TILTE_TYPE)
            return CommonViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.adapter_bank_notification_title, parent, false)
            )
        if (viewType == REMOVED_TYPE)
            return CommonViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.adapter_bank_notification_removed, parent, false)
            )
        if (viewType == END_TYPE)
            return CommonViewHolder(
                LayoutInflater.from(context)
                    .inflate(R.layout.adapter_bank_notification_item_seen, parent, false)
            )
        return CommonViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.adapter_bank_notification_item,
                parent,
                false
            )
        );

    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        if (getItemViewType(position) == END_TYPE) {
            return
        }
        val item = getItem(position)
        if (holder.time != null) {
            holder.time!!.setText(getTimeMessage(item.chatTime))
        }
        if (item.state == MessageState.DELETED)
            return
        if (item is MessageTitle) {
            holder.content!!.text = item.date
            return
        }
        if (holder.payment != null) {

        }

        if (holder.content != null)
            holder.content!!.text = item.content
        val url = item.urlImg
        if (holder.image != null && !TextUtils.isEmpty(url) && url.startsWith("http")) {
            providePicasso(context).load(url).into(holder.image)
        }

    }

    internal var picasso: Picasso? = null


    protected fun providePicasso(context: Context): Picasso {
        if (picasso == null)
            picasso = Picasso.Builder(context)
                .downloader(OkHttp3Downloader(context.cacheDir, 250000000))
                .memoryCache(LruCache(409600000))
                .build()
        return picasso!!
    }

    private fun getTimeMessage(createdTime: Long): String {
        val c = Calendar.getInstance()
        c.timeInMillis = createdTime
        return (getBeautyNumber(c.get(Calendar.HOUR_OF_DAY)) + ":"
                + getBeautyNumber(c.get(Calendar.MINUTE)))
    }

    private fun getBeautyNumber(n: Int): String {
        return if (n > 9) "" + n else "0$n"
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val list = ArrayList<MessageEntity>()
                if (typeSelected != ALL) {
                    for (item in listOrig) {
                        if (item is MessageTitle)
                            list.add(item)
                        else if (typeSelected == TRANSACTION && item.type == OTTCommon.TYPE_MESSAGE_BDSD && isValidSearch(
                                item,
                                constraint
                            )
                        ) {
                            list.add(item)
                        } else if (typeSelected == OTHER && anotherType(item.type) && isValidSearch(
                                item,
                                constraint
                            )
                        ) {
                            list.add(item)
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(constraint))
                        list.addAll(listOrig)
                    else {
                        for (item in listOrig) {
                            if (item is MessageTitle)
                                list.add(item)
                            else if (isValidSearch(item, constraint))
                                list.add(item)
                        }
                    }
                }
                var beforeATitle = true
                for (j in list.indices.reversed()) {
                    val currentState = list[j] is MessageTitle
                    if (beforeATitle && currentState) {
                        list.removeAt(j)
                    }
                    beforeATitle = currentState
                }
                results.values = list
                results.count = list.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list.clear()
                if (results?.values != null)
                    list.addAll(results.values as Collection<MessageEntity>)
                (context as BankNotificationActivity).onShow(list.isNotEmpty())
                notifyDataSetChanged()
                items.scrollToPosition(0)
            }

        }
    }

    private fun isValidSearch(item: MessageEntity, charSequence: CharSequence?): Boolean {
        return TextUtils.isEmpty(charSequence) || removeAccentLower(item.content).contains(
            removeAccentLower(charSequence.toString())
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getItem(position: Int): MessageEntity {
        return list[position]
    }

    override fun getItemViewType(position: Int): Int {
        try {
            if (getItem(position).state == END_TYPE)
                return END_TYPE
            if (getItem(position) is MessageTitle)
                return TILTE_TYPE
            return if (getItem(position).state == MessageState.DELETED) REMOVED_TYPE else getItem(
                position
            ).type
        } catch (e: Exception) {
        }

        return super.getItemViewType(position)
    }

    private fun anotherType(type: Int): Boolean {
        return type != OTTCommon.TYPE_MESSAGE_BDSD
    }

    protected fun removeAccentLower(str: String?): String {
        var str = str
        if (TextUtils.isEmpty(str))
            return ""
        str = str!!.toLowerCase()

        str = str.replace("[àáạảãâầấậẩẫăằắặẳẵ]".toRegex(), "a")
        str = str.replace("[éèẽẹêẻềếệểễ]".toRegex(), "e")
        str = str.replace("[ìíịỉĩ]".toRegex(), "i")
        str = str.replace("[òóọỏõôồốộổỗơờớợởỡ]".toRegex(), "o")
        str = str.replace("[ùúụủũưừứựửữ]".toRegex(), "u")
        str = str.replace("[ỳýỵỷỹ]".toRegex(), "y")
        str = str.replace("đ".toRegex(), "d")

        return str
    }

    fun getItems(): List<MessageEntity> {
        return listOrig
    }


    inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var time: TextView? = null
        var content: TextView? = null
        var image: ImageView? = null
        var payment: TextView? = null
        var popup: PopupMenu? = null

        init {
            time = itemView.findViewById(R.id.time)
            content = itemView.findViewById(R.id.content)
            image = itemView.findViewById(R.id.image)
            payment = itemView.findViewById(R.id.payment)
            if (payment != null) {
                payment!!.setSafeOnClickListener {
                    val item = getItem(adapterPosition)
                    TapItem(item)
                }
            }
            itemView.setOnLongClickListener {
                TapIcon(it)
                UIModel.getInstance().Vibrate()
                return@setOnLongClickListener false
            }
        }

        protected fun TapIcon(icon: View) {
            val pos = adapterPosition
            if (pos == -1)
                return
            val viewType = itemViewType
            if (viewType == END_TYPE || viewType == TILTE_TYPE)
                return
            val item = getItem(pos)
            if (item is MessageTitle)
                return
            if (popup == null) {
                popup = PopupMenu(context, icon, Gravity.BOTTOM or Gravity.RIGHT)
                popup!!.inflate(R.menu.menu_bank_notification)
                popup!!.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
                    val pos = adapterPosition
                    if (pos < 0)
                        return@OnMenuItemClickListener true
                    val item = getItem(pos)
                    val i = menuItem.itemId
                    if (i == R.id.copy) {
                        try {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(
                                context.getString(R.string.copy),
                                content!!.text
                            )
                            if (clipboard != null) {
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.success_copy),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Log.wtf("EXC", e)
                        }
                    } else if (i == R.id.delete) {
                        OTTData.g().deleteMessage(item.messageId)
                        item.setStatus(MessageState.DELETED)
                        notifyItemChanged(pos)
                    }
                    false
                })
            }
            popup!!.show()
        }
    }

    private fun TapItem(item: MessageEntity) {

    }
}