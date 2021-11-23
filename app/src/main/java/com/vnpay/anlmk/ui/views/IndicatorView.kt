package com.vnpay.anlmk.ui.views

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.vnpay.anlmk.R
import com.vnpay.anlmk.utils.UIModel


/**
 * Created by LeHieu on 8/23/2017.
 */

class IndicatorView : View {
    private var paint: Paint? = null
    private var position: Int = 0
    private val COUNTER_PAGE = 3

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init()
    }

    private fun init() {
        paint = Paint()
        paint!!.color = ContextCompat.getColor(context, R.color.indicator_selected)
        paint!!.flags = Paint.ANTI_ALIAS_FLAG
        paint!!.style = Paint.Style.FILL
        paint!!.strokeWidth = 1f

    }

    fun setSelected(position: Int) {
        this.position = position
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val sizeIcon = UIModel.getInstance().getDPtoPX(context, 3.5f).toFloat()
        var posDif: Float
        for (i in 0 until COUNTER_PAGE) {
            posDif = sizeIcon * (-(COUNTER_PAGE.toFloat() - 1) / 2 + i) * COUNTER_PAGE.toFloat() * 2f
            val lcl = if(position == i )  R.color.indicator_selected else R.color.indicator_unselected
            paint!!.color =ContextCompat.getColor(context, lcl)
            canvas.drawCircle(
                canvas.width / 2 + posDif, (canvas.height / 2).toFloat(), sizeIcon,
                paint!!
            )
        }
    }
}
