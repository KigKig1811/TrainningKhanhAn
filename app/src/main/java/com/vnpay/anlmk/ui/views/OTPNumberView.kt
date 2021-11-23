package com.vnpay.anlmk.ui.views

import android.content.Context
import android.graphics.*
import android.text.InputFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.vnpay.anlmk.R
import com.vnpay.anlmk.utils.UIModel


class NumberOTPView : AppCompatEditText {
    private var LENGHT_OTP = 6

    private var paint: Paint? = null
    private var rect: Rect = Rect()
    private var sizePadding: Int = 0
    private var rectF: RectF = RectF()
    private var otp: String? = null
    private var isPassword:Boolean =false
    private var bitmapIcon: Bitmap? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        rectF = RectF()
        paint = Paint()
        paint!!.isAntiAlias = true
        paint!!.textSize = resources.getDimension(R.dimen.font_otp_size)
        paint!!.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint!!.getTextBounds("0", 0, 1, rect)
        paint!!.textAlign = Paint.Align.CENTER
        sizePadding = UIModel.getInstance().getDPtoPX(context, 4f)
        setMeasuredDimension(measuredWidth, rect!!.height() + sizePadding * 2)
        isFocusable = true
        isFocusableInTouchMode = true
        val filters = ArrayList<InputFilter>()

        filters.add(InputFilter.LengthFilter(6))
        setBackgroundColor(Color.TRANSPARENT)
        setFilters(filters.toTypedArray())

        setTextColor(Color.WHITE)
        isCursorVisible = false
        isLongClickable = false
        setTextIsSelectable(false)
        bitmapIcon = UIModel.getInstance().getBitmapFromDrawable(
            context,
            ContextCompat.getDrawable(context, R.drawable.icon_hoa_pass)
        )
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
         isPassword = isVisiblePasswordInputType(inputType)
    }

    fun isVisiblePasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return variation == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD
    }

    fun getOtp(): String {
        return otp ?: ""
    }

    fun setOtp(otp: String) {
        setText(otp)
        this.otp = otp
        invalidate()
    }


    fun setOTPMaxLength(length: Int) {
        val filters = ArrayList<InputFilter>()

        filters.add(InputFilter.LengthFilter(length))

        val ips = arrayOfNulls<InputFilter>(filters.size)
        setFilters(filters.toTypedArray())
        LENGHT_OTP = length
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(canvas==null)
            return
        val width = width
        val height = height
        if (text != null)
            otp = text.toString()
        if (!TextUtils.isEmpty(otp)) {
            val textLength = getOtp().length
            val number = if (textLength > LENGHT_OTP) textLength else LENGHT_OTP
            if(isPassword)
                onDrawSign(canvas!!, textLength, number)
            else
                onDrawNumber(canvas, textLength, number)
            if (textLength < LENGHT_OTP)
                onDrawLine(canvas!!, textLength)
        } else {
            onDrawLine(canvas!!, 0)
        }
    }

    private fun onDrawSign(canvas: Canvas, textLength: Int, numberMax: Int) {
        val append = UIModel.getInstance().getDPtoPX(context, 100f)
        val width = width - append
        val margin = (getWidth() - width) / 2
        val height = height

        for (i in 0 until textLength) {
            canvas.drawBitmap(
                bitmapIcon!!,
                width * (i + 0.5f) / numberMax - rect!!.width() / 2f - sizePadding / 2f + margin,
                height / 2f - bitmapIcon!!.height / 2f,
                null
            )
        }
    }
    private fun onDrawNumber(canvas: Canvas?, textLength: Int, number: Int) {
        for (i in 0 until textLength) {
            paint!!.color = ContextCompat.getColor(context, R.color.colorPrimary)

            canvas!!.drawText(
                otp!!.substring(i, i + 1),
                width * i / number + width / 12f,
                height - sizePadding / 2f,
                paint!!
            )
        }
    }

    private fun onDrawLine(canvas: Canvas, textLength: Int) {
        val width = canvas.getWidth()
        val height = canvas.getHeight()
        for (i in textLength until LENGHT_OTP) {
            if (i == textLength) {
                paint!!.color = ContextCompat.getColor(context, R.color.colorPrimary)
            } else {
                paint!!.color = ContextCompat.getColor(context, R.color.notp_color)
            }
            rectF!!.set(
                (sizePadding + width * i / 6 + sizePadding).toFloat(),
                (height - sizePadding * 3 / 4).toFloat(),
                (sizePadding + width * (i + 1) / 6 - sizePadding).toFloat(),
                height.toFloat()
            )
            canvas.drawRoundRect(rectF, sizePadding / 3f, sizePadding / 3f, paint!!)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (paint != null) {
            paint!!.getTextBounds("0", 0, 1, rect)
            setMeasuredDimension(widthMeasureSpec, rect!!.height() + sizePadding * 7)
        }
    }

//    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
//        if(outAttrs!=null) {
//            outAttrs.actionLabel = null
//            outAttrs.inputType = InputType.TYPE_CLASS_NUMBER
//            outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE
//        }
//        return super.onCreateInputConnection(outAttrs)
//    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN && isEnabled) {
            requestFocus()
            val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
            return true
        }
        return super.onTouchEvent(event)
    }

}
