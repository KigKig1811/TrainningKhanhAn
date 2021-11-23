package com.vnpay.anlmk.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.vnpay.anlmk.R


class GroupTextView : LinearLayout {
    private var mainText: TextView? = null
    private var hintText: TextView? = null
    private var icon: ImageView? = null
    var type: Int = 0
    var gravityIcon: Int = 0
    var value: String? = null
    private var hint: String? = null
    private var text: String? = null
    private var drawable: Int = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.GroupTextView)
        hint = a.getString(R.styleable.GroupTextView_gr_hint)
        text = a.getString(R.styleable.GroupTextView_gr_text)
        drawable = a.getResourceId(R.styleable.GroupTextView_gr_icon, R.drawable.down_red)
        if (a.hasValue(R.styleable.GroupTextView_gr_icon_pos))
            gravityIcon = a.getInt(R.styleable.GroupTextView_gr_icon_pos, Gravity.CENTER)

        a.recycle()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        this.hintText = findViewById(R.id.hint)
        this.mainText = findViewById(R.id.text)
        this.icon = findViewById(R.id.icon)
        if (!TextUtils.isEmpty(hint))
            this.hintText!!.text = hint
        if (!TextUtils.isEmpty(text))
            setText(text)
        if (drawable != android.R.color.transparent)
            this.icon!!.setImageResource(drawable)
        else
            this.icon!!.setImageDrawable(null)
        if (gravityIcon != 0) {
            val params = this.icon!!.layoutParams as LinearLayout.LayoutParams
            params.gravity = gravityIcon
            this.icon!!.layoutParams = params
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled) {
            icon!!.visibility = View.INVISIBLE
        } else {
            icon!!.visibility = View.VISIBLE
        }
    }

    fun setText(value: String?) {
        if (TextUtils.isEmpty(this.hintText!!.text)) {
            this.hintText!!.visibility = View.GONE
            this.mainText!!.visibility = View.VISIBLE
            val top = context.resources.getDimensionPixelSize(R.dimen.padding_item)
            val bottom = context.resources.getDimensionPixelSize(R.dimen.padding_item)
            val left = 0
            this.setPadding(left, top, left, bottom)
        } else if (!TextUtils.isEmpty(value)) {
            //            if (TextUtils.isEmpty(this.mainText.getPanMask().toString()))
            //                UIModel.getInstance().Slide(hintText, true);
            this.mainText!!.visibility = View.VISIBLE
            this.hintText!!.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(R.dimen.descript_size)
            )
            val top = context.resources.getDimensionPixelSize(R.dimen.padding_item)
            val left = 0
            val bottom = context.resources.getDimensionPixelSize(R.dimen.padding_item)
            this.setPadding(left, top, left, bottom)
        } else {
            //            UIModel.getInstance().Slide(hintText, false);
            this.mainText!!.visibility = View.GONE
            this.hintText!!.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                context.resources.getDimension(R.dimen.main_size)
            )
            val top = context.resources.getDimensionPixelSize(R.dimen.padding_item)
            val bottom = context.resources.getDimensionPixelSize(R.dimen.padding_item)
            val left = 0
            this.setPadding(left, top, left, bottom)

        }
        this.mainText!!.text = value

    }

    fun setHint(@StringRes id: Int) {
        this.hintText!!.setText(id)
    }

    fun setHint(value: String) {
        this.hintText!!.text = value
    }

    fun getHint(): String {
        return if (TextUtils.isEmpty(this.hintText!!.text)) "" else this.hintText!!.text.toString()
    }

    fun getText(): String {
        return if (this.mainText!!.text == null) "" else this.mainText!!.text.toString()
            .trim { it <= ' ' }
    }

    fun setIcon(drawable: Int) {
        icon!!.setImageResource(drawable)
    }

    public override fun onSaveInstanceState(): Parcelable? {

        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        val b = Bundle()
        b.putString("text", getText())
        b.putString("value", value)
        ss.b = b
        return ss
    }


    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        val b = ss.b
        if (b != null && !TextUtils.isEmpty(b.getString("text"))) {
            setText(b.getString("text"))
            value = b.getString("value")
        }
    }

    fun setDrawableLeft(drawableLeft: Drawable) {
        this.mainText!!.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
    }


    internal class SavedState : View.BaseSavedState {
        var b: Bundle? = null

        constructor(superState: Parcelable?) : super(superState) {}

        private constructor(`in`: Parcel) : super(`in`) {
            b = `in`.readBundle()

        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeBundle(b)
        }
    }
}
