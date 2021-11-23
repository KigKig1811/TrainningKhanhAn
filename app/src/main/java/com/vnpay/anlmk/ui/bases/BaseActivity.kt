package com.vnpay.anlmk.ui.bases

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.ParseViewer
import com.vnpay.anlmk.data.MyOTTCallBack
import com.vnpay.anlmk.di.Common
import com.vnpay.anlmk.interfaces.OTTButtonClassify
import com.vnpay.anlmk.main.BANK_CODE
import com.vnpay.anlmk.main.getOTTLicense
import com.vnpay.anlmk.main.getXOTTLicense
import com.vnpay.anlmk.networks.AppData
import com.vnpay.anlmk.ui.activities.home.HomeActivity
import com.vnpay.anlmk.ui.activities.login.LoginActivity
import com.vnpay.anlmk.ui.activities.login.OPTActivity
import com.vnpay.anlmk.ui.dialogs.ConfirmDialog
import com.vnpay.anlmk.ui.dialogs.LoadingDialog
import com.vnpay.anlmk.ui.views.NumberOTPView
import com.vnpay.anlmk.utils.extensions.setSafeOnClickListener
import kotlinx.android.synthetic.main.title_bar.*
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {
    val loading by inject<LoadingDialog>()
    val confirm by inject<ConfirmDialog>()

    abstract val model: BaseViewModel

    @get:LayoutRes
    abstract val layoutId: Int

    @get:StringRes
    abstract val titleId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Common.baseActivity = this

        if (Common.tfRoboto_Italics == null)
            Common.tfRoboto_Italics = Typeface.createFromAsset(getAssets(),
                "fonts/Fonti.ttf");
        if (Common.tfRoboto_Regular == null)
            Common.tfRoboto_Regular = Typeface.createFromAsset(
                getAssets(), "fonts/Fontn.ttf");
        if (Common.tfRoboto_Medium == null)
            Common.tfRoboto_Medium = Typeface.createFromAsset(
                getAssets(), "fonts/Fontb.ttf");

        if (layoutId != 0)
            setContentView(layoutId)

        model?.apply {
            isLoading.observe(this@BaseActivity, Observer {
                handleShowLoading(it!!)
            })
            message.observe(this@BaseActivity, Observer {
//                onError(it)
            })
            noInternetConnectionEvent.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(R.string.error_no_network)
            })
            connectTimeoutEvent.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(R.string.error_network_disconnect)
            })
            serverErrorEvent.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(R.string.error_at_server)
            })
            errorToHomeMesssage.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(R.string.error_at_server)
            })
            expireSessionEvent.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(it).addButtonRight {
                    startActivity(
                        Intent(this@BaseActivity, LoginActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                    ActivityCompat.finishAffinity(this@BaseActivity)
                    setResult(Activity.RESULT_OK)
                    finish()
                    confirm.dismiss()
                }.setAction(true)
            })
            activeAnotherDeviceEvent.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(it).addButtonRight {
                    AppData.g().onClearData()
                    startActivity(
                        Intent(this@BaseActivity, LoginActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                    ActivityCompat.finishAffinity(this@BaseActivity)
                    setResult(Activity.RESULT_OK)
                    finish()
                    confirm.dismiss()
                }.setAction(true)
            })
            successFinishMessage.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(it).addButtonRight {
                    setResult(Activity.RESULT_OK)
                    finish()
                    confirm.dismiss()
                }.setAction(true)
            })
            errorFinishMessage.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(it).addButtonRight {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                    confirm.dismiss()
                }.setAction(true)
            })
            errorToHomeMesssage.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(it).addButtonRight {
                    setResult(Activity.RESULT_CANCELED)
                    startActivity(
                        Intent(this@BaseActivity, HomeActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                    )
                    ActivityCompat.finishAffinity(this@BaseActivity)
                    finish()
                    confirm.dismiss()
                }.setAction(true)
            })
            invalidCertificateEvent.observe(this@BaseActivity, Observer {
                confirm.newBuild().setNotice(R.string.error_cerpinning)
            })

        }
    }

    override fun onResume() {
        super.onResume()
        Common.baseActivity = this
        autoOTT()
    }

    fun autoOTT() {
//        val apply = OTTBuilder.Build(this)
//            .setLanguage(OTTCommon.VN_LANG)
////            .setBankAppId(BANK_CODE)
//            .setBankName(getString(R.string.app_name))
//            .setLicense(getXOTTLicense(), getOTTLicense())
//            .setCallBack(MyOTTCallBack::class.java)
//            .setBankIcon(R.mipmap.ic_launcher_round)
//            .setCif(AppData.g().cif)
//            .setPhone(AppData.g().phone)
//            .setButtonClass(OTTButtonClassify::class.java)
//            .apply(AppData.g().accessKey)
    }

    fun onError(it: String?) {
        confirm.newBuild().setNotice(it)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ParseViewer.getInstance().bind(this)
        title_left?.apply {
            setSafeOnClickListener {
                onBackPressed()
            }
            if (titleId != 0) title = getString(titleId).replace("\n", " ")
        }

    }

    fun setLeft(@DrawableRes icon: Int, onClickListener: () -> Unit) {
        title_left.setSafeOnClickListener { onClickListener.invoke() }
        if (icon != 0)
            title_left.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }

    fun setRight(@DrawableRes icon: Int, onClickListener: () -> Unit) {
        title_right.setSafeOnClickListener { onClickListener.invoke() }
        if (icon != 0)
            title_right.setImageDrawable(ContextCompat.getDrawable(this, icon))
    }

    override fun setTitle(text: CharSequence) {
        super.setTitle(text)
        title_name?.text = text.toString().replace("\n", " ")
    }


    override fun setTitle(@StringRes id: Int) {
        super.setTitle(id)
        title_name?.text = getString(id).replace("\n", " ")
    }

    open fun handleShowLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) showLoading() else hideLoading()
        }
    }

    fun hideLoading() {
        loading.dismiss()
    }

    fun showLoading() {
        loading.show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isFinishing) {
            return super.dispatchTouchEvent(ev)
        }
        var end = false
        try {
            val currentFocus = currentFocus
            end = super.dispatchTouchEvent(ev)
            val currentTime = System.currentTimeMillis()

            if (currentFocus != null && (currentFocus is EditText || currentFocus is NumberOTPView)) {
                val w = getCurrentFocus() ?: return end
                val scrcoords = IntArray(2)
                w.getLocationOnScreen(scrcoords)
                val x = ev.rawX + w.left.toFloat() - scrcoords[0].toFloat()
                val y = ev.rawY + w.top.toFloat() - scrcoords[1].toFloat()
                if (ev.action == 1 && (x < w.left.toFloat() || x >= w.right.toFloat() || y < w.top.toFloat() || y > w.bottom.toFloat())) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
                }
            }
        } catch (e: Exception) {

        }

        return end
    }


}
