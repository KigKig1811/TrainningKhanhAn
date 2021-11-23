package com.vnpay.anlmk.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.vnpay.anlmk.R


/**
 * Created by Lvhieu2016 on 3/23/2017.
 */

class LoginFingerDialog(context: Context) : Dialog(context) {
    internal var password: EditText? = null
    private var listener: LoginListener? = null
    private var selfAction: Boolean = false

    internal var imm: InputMethodManager? = null

    init {
        window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_finger_login)

        password = findViewById<View>(R.id.password) as EditText
        password!!.text = null
        password!!.setOnKeyListener { _, keyCode, event ->
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                submit()
            }
            false
        }
        findViewById<View>(R.id.submit).setOnClickListener { submit() }
        findViewById<View>(R.id.cancel).setOnClickListener {
            dismiss()
            this@LoginFingerDialog.listener!!.cancelOTP()
        }

        setCancelable(true)
        setCanceledOnTouchOutside(false)
        this.setOnCancelListener {
            if (this@LoginFingerDialog.selfAction) {
                this@LoginFingerDialog.selfAction = false
            } else if (listener != null) {
                listener!!.cancelOTP()
            }
            closeKeyBoard()
        }
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        password!!.requestFocus()
        showKeyBoard()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (listener != null)
            listener!!.cancelOTP()
    }

    private fun showKeyBoard() {
        if (imm == null)
            imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.showSoftInput(password, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun closeKeyBoard() {
        if (imm == null)
            imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(password!!.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    private fun submit() {
        if (listener == null)
            return
        dismiss()
        val passValue = password!!.text.toString()
        closeKeyBoard()
        listener!!.onValidateLogin(passValue)
        this@LoginFingerDialog.selfAction = true

    }


    fun init(listener: LoginListener) {
        resetPassword()
        this.listener = listener
    }

    fun resetPassword() {
        if (password != null)
            password!!.text = null
    }

    interface LoginListener {
        fun onValidateLogin(password: String)

        fun cancelOTP()

    }
}