package com.vnpay.anlmk.ui.activities.notifications

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.Tap
import com.vnpay.anlmk.data.repository.HomeModel
import com.vnpay.anlmk.ui.adapters.notify.BankNotificationAdapter
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.utils.UIModel
import com.vnpay.ott.*
import kotlinx.android.synthetic.main.title_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class BankNotificationActivity : BaseActivity() {
    override val titleId: Int = R.string.infor_bank

    override val model: HomeModel by viewModel()

    override val layoutId: Int = R.layout.activity_bank_notification

    lateinit var  items: RecyclerView
    lateinit var search: EditText
    lateinit var selectedView: View
    lateinit var notFound: TextView
    lateinit var loadingView: View
    private var adapter: BankNotificationAdapter? = null
    private var page = BankNotificationAdapter.ALL
    private var isShowing = false
    var run: Runnable = Runnable {
        if (adapter != null)
            adapter!!.filter.filter(search!!.getText().toString())
    }
    private var popup: PopupMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRight(R.drawable.more_24_x_24){
            TapMenuBar()
        }
        autoOTT()
        items = findViewById(R.id.items)
        search = findViewById(R.id.search)
        loadingView = findViewById(R.id.loading)
        selectedView = findViewById(R.id.first)
        selectedView!!.isSelected = true
        notFound = findViewById(R.id.not_found_view)
        onGetMessages()
        initAction()
    }

    private fun initAction() {
        search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                search!!.removeCallbacks(run)
                search!!.postDelayed(run, 300)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }
    fun onGetMessages() {
        try {
            val messages = OTTData.g().getAllMesssagesExcludeType(-1, false)
            if (messages != null) {
                onAllMessages(messages)
//                OTTData.g().counterNotification = 0
               // OTTData.g().confirmReadedExcludeType(-1)
            } else {
                if (!OTTBuilder.isNotInitialize()) {
                    autoOTT()
                }
            }
        } catch (e: Exception) {
        }

    }
    override fun onKeyDown(keycode: Int, e: KeyEvent): Boolean {
        when (keycode) {
            KeyEvent.KEYCODE_MENU -> {
                TapMenuBar()
                return true
            }
        }

        return super.onKeyDown(keycode, e)
    }

    private fun TapMenuBar() {
        if (popup == null) {
            val contextThemeWrapper = ContextThemeWrapper(this, R.style.PopupMenuOverlapAnchor)
            popup = PopupMenu(contextThemeWrapper,title_right)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popup!!.gravity = Gravity.END
            }
            popup!!.inflate(R.menu.notice_settings)
            popup!!.setOnDismissListener(PopupMenu.OnDismissListener { isShowing = false })
            popup!!.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.delete_all->{
                        confirm.newBuild().setNotice(R.string.decide_remove_all_message)
                        .addButtonLeft(R.string.cancel)
                            .addButtonRight(R.string.agree){
                                items.adapter = null
                                if(adapter!=null)
                                OTTData.g().deleteMessages(adapter!!.getItems())
                                loadingView.visibility = View.VISIBLE;
                                confirm.dismiss();
                            }
                        return@setOnMenuItemClickListener true
                    }
                    R.id.setting -> {
                        startActivity(
                            Intent(
                                this@BankNotificationActivity,
                                SettingNotificationActivity::class.java
                            )
                        )
                        return@setOnMenuItemClickListener true
                    }
                }
                return@setOnMenuItemClickListener false
            }
        }
        if (!isShowing) {
            popup!!.show()
            isShowing = true
        } else {
            popup!!.dismiss()
        }

    }
    private fun getDateFromLong(chatTime: Long): String {
        try {
            val c = Calendar.getInstance()
            val today = UIModel.getInstance().getTypeDate(c, "/")
            c.add(Calendar.DATE, -1)
            val yesterday = UIModel.getInstance().getTypeDate(c, "/")
            c.timeInMillis = chatTime
            val date = UIModel.getInstance().getTypeDate(c, "/")
            if (date == today) {
                return getString(R.string.today) + ", " + date
            } else if (date == yesterday) {
                return getString(R.string.yesterday) + ", " + date
            }
            return date
        } catch (e: Exception) {
        }

        return chatTime.toString()
    }
    private fun onAllMessages(list: List<MessageEntity>?) {
        try {
            if(list==null)
                return
            val listReal = ArrayList<MessageEntity>(list)
                listReal.removeAll(OTTData.g().getMesssages(OTTCommon.TYPE_HONG_BAO))

                var lastDate = ""
                var isLineReadAdded = false
                for (j in listReal.indices.reversed()) {
                    if (!isLineReadAdded && listReal[j].isRead) {
                        val m = MessageEntity()
                        m.setStatus(BankNotificationAdapter.END_TYPE)
                        listReal.add(j + 1, m)
                        isLineReadAdded = true
                    }
                    val currentDate = getDateFromLong(listReal[j].chatTime)
                    if (!TextUtils.isEmpty(lastDate) && lastDate != currentDate) {
                        listReal.add(j + 1, MessageTitle().setDate(lastDate))
                    }
                    lastDate = currentDate
                }
                listReal.add(0, MessageTitle().setDate(lastDate))
            
            runOnUiThread {
                    adapter = BankNotificationAdapter(
                        this@BankNotificationActivity,
                        listReal,
                        items,
                        model,
                        page
                    )
                    items.setAdapter(adapter)
                    items.scrollToPosition(items.getAdapter()!!.itemCount - 1)

                loadingView.setVisibility(View.GONE)
                onShow(!listReal.isEmpty())
            }
        } catch (e: Exception) {
            Log.wtf("EXC", e)
        }

    }
    @Tap(R.id.third, R.id.second, R.id.first)
    fun TapMenu(v: View) {
        try {
            selectedView.setSelected(false)
            v.isSelected = true
            selectedView = v
            val i = v.id
            if (i == R.id.first) {
                page = BankNotificationAdapter.ALL
            } else if (i == R.id.second) {
                page = BankNotificationAdapter.TRANSACTION

            } else if (i == R.id.third) {
                page = BankNotificationAdapter.OTHER

            }
            if (adapter != null) {
                adapter!!.typeSelected = page
                search.postDelayed(run, 300)
            }
            items.postDelayed(Runnable {
                try {
                    if (items.getAdapter() != null)
                        items.scrollToPosition(items.getAdapter()!!.itemCount - 1)
                } catch (e: Exception) {
                }
            }, 600)
        } catch (e: Exception) {
            Log.wtf("EXC", e)
        }

    }

    fun onShow(b: Boolean) {
        if (b) {
            notFound.visibility = View.GONE
        } else {
            notFound.visibility = View.VISIBLE
        }
    }

    @Throws(Exception::class)
    fun addMessages(messages: List<MessageEntity>) {
        onGetMessages()
    }
  fun onResultRemove(result:Boolean) {
        if (isFinishing())
            return;
        if (!result) {
            kotlin.run{
                confirm.newBuild().setNotice(R.string.type7)
            }
        } else {
          kotlin.run{
              loadingView!!.visibility = View.GONE
          }
        }
        onGetMessages();
    }
}
