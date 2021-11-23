package com.vnpay.anlmk.utils.extensions

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.vnpay.anlmk.BuildConfig
import com.vnpay.anlmk.ui.views.TextInputLayout
import com.vnpay.anlmk.utils.UIModel
import org.json.JSONObject
import java.math.BigDecimal

// # Kotlin Extensions

//- [View](#view)
//- [Context](#context)
//- [Fragment](#fragment)
//- [Activity](#activity)
//- [ViewGroup](#viewgroup)
//- [TextView](#textview)
//- [String](#string)
//- [Other](#other)


// ## View

fun Exception.safeLog() {
    if (BuildConfig.DEBUG) printStackTrace()
}

fun Throwable.safeLog() {
    if (BuildConfig.DEBUG) printStackTrace()
}

fun EditText.safeHint(): String {
    if (parent is TextInputLayout && TextUtils.isEmpty((parent as TextInputLayout).hint))
        return (parent as TextInputLayout).hint.toString()
    return hint?.toString()?:""
}
fun EditText.str(): String {
    return text.toString()
}

fun String.getAmount(): String {
    return UIModel.getInstance().getDotMoney(this)
}
fun String.getAmountVND(): String {

    return UIModel.getInstance().getDotMoney(this) +" VND"
}
fun EditText.getAmountServer(): String {
    return text.toString().replace("[^\\d-.]".toRegex(), "")
}
fun String.getAmountServer(): String {
    return this.replace("[^\\d-.]".toRegex(), "")
}

fun String.getAmountBig(): BigDecimal {
    return BigDecimal(this.getAmountServer())
}

fun String.html(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}
/**
 * Extension method to provide simpler access to {@link View#getResources()#getString(int)}.
 */
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun JSONObject.getSafeString(key:String):String{
    if(this.has(key))
        return getString(key)
    return ""
}

/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        try {
            onSafeClick(it)
        } catch (e: java.lang.Exception) {
            Log.wtf("EX", e)
        }
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

 fun String.convertServerDate(): String {
    return UIModel.getInstance().convertDate("dd/MM/yyyy", "ddMMyyyy", this)
}

fun String.convertSavingDate(): String {
    return UIModel.getInstance().convertDate("yyyyMMdd", "dd/MM/yyyy", this)
}

fun String.convertDynamicDate(from:String, to:String): String {
    return UIModel.getInstance().convertDate(from, to, this)
}

fun String.parseInt():Int{
    return Integer.parseInt(this)
}