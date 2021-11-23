package com.vnpay.anlmk.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


class HomeBannerViewPager : ViewPager {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(w / 2, MeasureSpec.EXACTLY))
    }
}
