package com.vnpay.anlmk.ui.views

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

import com.vnpay.anlmk.R
import com.vnpay.anlmk.utils.UIModel


class TextInputLayout : FrameLayout {
    private var SPACE: Int = 0
    private val typeLogin: Boolean = false
    internal var edit: EditText? = null

    var hint: CharSequence? = null
        internal set
    internal var p = Paint()
    internal var rect = Rect()

    internal var point = Point()
    internal var lastTouch: Long = 0
    internal var maxLoop = 3

    private val topWriteText: Float
        get() {
            if (edit!!.isFocused || !TextUtils.isEmpty(edit!!.text!!.toString())) {
                p.color = ContextCompat.getColor(context, R.color.default_color)
                p.textSize = resources.getDimensionPixelSize(R.dimen.descript_size).toFloat()
                transTo(SPACE / 4f)
                return edit!!.top - edit!!.paddingTop + rect.height() - SPACE / 4f
            }
            p.color = ContextCompat.getColor(context, R.color.color_hint)
            p.textSize = resources.getDimensionPixelSize(R.dimen.main_size).toFloat()
            transTo(0f)
            return (edit!!.bottom-paddingBottom).toFloat()
        }

    private val marginLeft: Float
        get() {
            val drawables = edit!!.compoundDrawables
            return if (drawables != null && drawables.size > 0 && drawables[0] != null) (drawables[0].intrinsicWidth + edit!!.paddingLeft + edit!!.compoundDrawablePadding).toFloat() else paddingLeft.toFloat()
        }

    //    private void action() {
    //        if (edit != null && edit.isEnabled() && UIModel.getInstance().isOpenKeyboard()) {
    //            preventDismissKeyboard();
    //            return;
    //        }
    //        if (edit != null && edit.isEnabled()) {
    //            if (!edit.hasFocus())
    //                edit.requestFocus();
    //            preventDismissKeyboard();
    //        }
    //    }


    internal var imm: InputMethodManager? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.TextInputLayout)
            hint = a.getText(R.styleable.TextInputLayout_android_hint)
            a.recycle()
        }
        SPACE = UIModel.getInstance().getDPtoPX(context, 10f)
        setWillNotDraw(false)
        p.isAntiAlias = true
        p.color = ContextCompat.getColor(context, R.color.color_hint)
        p.textSize = resources.getDimensionPixelSize(R.dimen.main_size).toFloat()
        p.getTextBounds("aaa", 0, 1, rect)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isClicked(event) && edit != null && !edit!!.isFocused) {
            edit!!.requestFocus()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && edit!!.showSoftInputOnFocus) {
                preventDismissKeyboard()
            }
        } else if (event.action == KeyEvent.ACTION_DOWN) {
            point.set(event.x.toInt(), event.y.toInt())
            lastTouch = System.currentTimeMillis()
        }
        return if (edit!!.isFocused) false else super.onTouchEvent(event)
    }

    private fun isClicked(event: MotionEvent): Boolean {
        return event.action == KeyEvent.ACTION_UP && System.currentTimeMillis() - lastTouch < 100
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (getChildAt(0) !is EditText)
            throw Exception("Child of TextInputLayout might be a EditText")
        edit = getChildAt(0) as EditText
        if (edit != null && !TextUtils.isEmpty(edit!!.hint)) {
            hint = edit!!.hint
            edit!!.setHintTextColor(Color.TRANSPARENT)
        }

        if (edit != null && edit!!.isEnabled) {
            edit!!.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                invalidate()
                requestLayout()
            }
        }

        onLope()
    }

    private fun onLope() {
        postDelayed(Runnable {
            if (visibility != View.VISIBLE || maxLoop < 0)
                return@Runnable
            if (edit != null && edit!!.height > 0)
                requestLayout()
            else
                onLope()
            maxLoop--
        }, 100)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (getVisibility() == View.VISIBLE) {
            requestLayout()
            maxLoop = 3
            onLope()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val c = hint
        if (c != null) {
            canvas.drawText(c.toString(), marginLeft, topWriteText, p)
        }

    }

    private fun transTo(i: Float) {
        if (edit is View) {
            val view = edit as View?
            view!!.animate().translationY(i)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var heightEdit = 0
        if (edit != null) {
            heightEdit = edit!!.height
        }
        val height = Math.max(
            context.resources.getDimensionPixelSize(R.dimen.min_height),
            context.resources.getDimensionPixelSize(if (typeLogin) R.dimen.padding_item else R.dimen.padding_item) * 2 + heightEdit
        )

        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), height)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (!enabled)
            alpha = 0.5f
        else
            alpha = 1f
    }

    private fun preventDismissKeyboard() {
        if (imm == null)
            imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (edit != null)
            imm!!.showSoftInput(edit as View?, InputMethodManager.SHOW_FORCED)
    }

    fun setHint(hint: String) {
        this.hint = hint
        invalidate()
    }
}
