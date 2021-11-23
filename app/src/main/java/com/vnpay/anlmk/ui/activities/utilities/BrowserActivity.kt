package com.vnpay.anlmk.ui.activities.utilities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.vnpay.anlmk.R
import com.vnpay.anlmk.bind.FindViewer
import com.vnpay.anlmk.ui.bases.BaseActivity
import com.vnpay.anlmk.ui.bases.BaseViewModel

/**
 * Created by Lvhieu2016 on 1/12/2017.
 */

class BrowserActivity : BaseActivity() {
    override val titleId: Int =0
    @FindViewer(R.id.webs)
    protected lateinit var webs: WebView
    @FindViewer(R.id.title_left)
    protected lateinit var left: ImageView
    @FindViewer(R.id.title_name)
    protected lateinit var title: TextView
    override val layoutId = R.layout.activity_browser
    override val model: BaseViewModel = object:BaseViewModel(){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var url: String? = "http://vnpay.vn"
        val b = intent.extras
        if (b != null && b.containsKey("url"))
            url = b.getString("url")
        if (b != null && b.containsKey("title")) {
            title.text = b.getString("title")
        }
        webs.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webs.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY

        webs.setInitialScale(1)
        webs.settings.loadWithOverviewMode = true
        webs.settings.useWideViewPort = true
        webs.settings.loadWithOverviewMode = true
        webs.settings.useWideViewPort = true
        webs.settings.setSupportZoom(true)
        webs.settings.builtInZoomControls = true
        webs.settings.allowFileAccess = false
        webs.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webs.settings.javaScriptEnabled = false
        webs.settings.displayZoomControls = false
        webs.loadUrl(url)

    }


    companion object {

        fun start(context: Context, title: String, url: String) {
            val i = Intent(context, BrowserActivity::class.java)
            i.putExtra("title", title)
            i.putExtra("url", url)
            context.startActivity(i)
        }
    }
}
