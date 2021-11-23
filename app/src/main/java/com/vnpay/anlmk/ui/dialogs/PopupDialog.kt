package com.vnpay.anlmk.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.vnpay.anlmk.R
import com.vnpay.anlmk.interfaces.ICombo
import com.vnpay.anlmk.ui.adapters.transactions.SearchItemAdapter
import com.vnpay.anlmk.utils.extensions.setSafeOnClickListener

import java.util.ArrayList

open class PopupDialog(var mContext: Context) :
    Dialog(mContext), SearchItemAdapter.Callback {
     internal var title: TextView? = null
    internal var search: EditText? = null
    internal var items: RecyclerView? =null
    internal var wrapMain: LinearLayout? =null
    internal var iWatcher: TextWatcher? = null
    internal var imm: InputMethodManager? = null
    internal var runSearch: Runnable? =null
    private var titleValue = ""
    private var mAdapter: SearchItemAdapter? = null
    private var listener: ICombo? = null
    private var list: MutableList<Any>? = null
    private var notFound: TextView? = null
    private var type: Int = 0
    private var notFoundText: String? = null
    private val selected = SparseArray<Any>()
    private var defaultFirst: Boolean = false
    private  var hideSearch :Boolean = false
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        window!!.setGravity(Gravity.NO_GRAVITY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_popup)

        iniView()
        iniBackData()
        initAction()
    }

    private fun iniBackData() {
        if (this.list == null) {
            this.list = ArrayList()
        }
        mAdapter =
            SearchItemAdapter(mContext, this.list!!, this)

        if (selected[type] != null)
            mAdapter!!.setSelected(selected[type])

        items!!.adapter = mAdapter
        if (selected.indexOfKey(type)>=0) {
            mAdapter!!.setSelected(selected[type])
        }
    }

    private fun iniView() {
        title = findViewById(R.id.title_text)
        notFound = findViewById(R.id.not_found_view)
        search = findViewById(R.id.search)

        if (!TextUtils.isEmpty(titleValue))
            title!!.text = titleValue
        items = findViewById(R.id.items)
        wrapMain = findViewById(R.id.wrap_main)
        setCancelable(true)

        val cancel = findViewById<View>(R.id.close)
        cancel.setSafeOnClickListener {
            if (search != null) {
                preventShowKeyboard()
            }
            dismiss()
        }
        search!!.visibility = if (hideSearch) View.GONE else View.VISIBLE
    }

    private fun initAction() {

        setOnCancelListener {
            if (search != null) {
                preventShowKeyboard()
            }
        }
        runSearch = Runnable {
            if (mAdapter != null && search != null) {
                mAdapter!!.filter.filter(search!!.text.toString())
            }
        }
        search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (search != null) {
                    search!!.removeCallbacks(runSearch)
                    search!!.postDelayed(runSearch, 300)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun preventShowKeyboard() {
        if (imm == null)
            imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(search!!.windowToken, 0)
    }

    override fun onStart() {
        super.onStart()
        if (mAdapter != null)
            mAdapter!!.filter.filter(null)

        if (this.list == null || this.list!!.isEmpty()) {
            changeNotFound(true)
            if (TextUtils.isEmpty(notFoundText)) {
                notFoundText = context.getString(R.string.not_found)
            }
            notFound!!.text = notFoundText
        } else {
            //            search.setVisibility(this.list.size() < 3 ? View.GONE : View.VISIBLE);
            changeNotFound(false)
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun preventDismissKeyboard() {
        if (imm == null)
            imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.showSoftInput(title, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun changeNotFound(isNotFound: Boolean) {
        try {
            if (isNotFound) {
                if (notFound != null) {
                    notFound!!.text = context.getString(R.string.not_found)
                    notFound!!.visibility = View.VISIBLE
                    if (search!!.visibility == View.VISIBLE)
                        search!!.visibility = View.GONE

                }

            } else {
                if (notFound != null) {
                    notFound!!.visibility = View.GONE
                }
                //                if (search.getVisibility() != View.VISIBLE && this.list.size() >= 8)
                //                    search.setVisibility(View.VISIBLE);

            }
        } catch (e: Exception) {
        }

    }

    fun showDialog(title: String, list: List<Any>, listener: ICombo, type: Int, objectSelected: Any?) {
        if (objectSelected != null)
            selected.put(type, objectSelected)
        showDialog(title, list, listener, type)
    }

    fun showDialog(title: String, list: List<Any>?, listener: ICombo, type: Int = 0) {
        this.type = type
        try {
            this.listener = listener
            if (this.title != null) {
                this.title!!.text = title
            }
            this.titleValue = title
            if (selected[type] == null && list != null && !list.isEmpty() && defaultFirst) {
                selected.put(type, list[0])
            }
            if (mAdapter != null) {
                mAdapter!!.setSelected(selected[type])
            }
            if (this.list == null)
                this.list = ArrayList()
            else
                this.list!!.clear()
            if (list != null) {
                this.list!!.addAll(list)
            }
            if (search != null) {
                search!!.setText(null)
                search!!.visibility = if (hideSearch) View.GONE else View.VISIBLE
            }

            show()
        } catch (ex: Exception) {
            Log.wtf("EX", ex)
        }

    }

    fun setHideSearch(hideSearch: Boolean): PopupDialog {
        this.hideSearch = hideSearch
        if (search != null)
            search!!.visibility = if (hideSearch) View.GONE else View.VISIBLE

        return this
    }

    override fun show(isShown: Boolean) {
        if (isShown) {
            notFound!!.visibility = View.GONE
        } else {
            notFound!!.visibility = View.VISIBLE
        }
    }

    override fun onClick(item: Any) {
        try {
            if (listener != null) {
                selected.put(type, item)
                listener!!.actionItem(item, type)
                dismiss()
            }
        } catch (e: Exception) {
            Log.wtf("EXC", e)
        }

    }

    fun setAutoDefaultItemFirst(defaultFirst: Boolean): PopupDialog {
        this.defaultFirst = defaultFirst
        return this
    }

    fun setSelectedItem(selectedItem: Any?, type: Int): PopupDialog {
        if(selectedItem==null)
            return this
        selected.put(type, selectedItem)
        return this
    }
}
