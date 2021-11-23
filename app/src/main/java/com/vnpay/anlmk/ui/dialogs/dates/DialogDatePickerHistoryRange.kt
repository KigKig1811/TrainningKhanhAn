package com.vnpay.anlmk.ui.dialogs.dates

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.os.Build
import android.app.Dialog
import android.os.Bundle
import android.content.DialogInterface
import com.vnpay.anlmk.utils.UIModel
import android.text.TextUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.vnpay.anlmk.R
import java.util.*


class DialogDatePickerHistoryRange : DialogFragment(), DatePickerDialog.OnDateSetListener {
    internal var datepicker: DatePickerDialog? = null
    private var onDatePickerListener: OnDatePickerListener? = null
    private var year: Int = 0
    private var month: Int = 0
    private var date: Int = 0
    private var minDate: Long = 0
    private var maxDate: Long = 0
    internal var calendar: Calendar = Calendar.getInstance()
    private var type: Int = 0

    fun setListener(
        onDatePickerListener: OnDatePickerListener,
        type: Int
    ): DialogDatePickerHistoryRange {
        this.year = calendar.get(Calendar.YEAR)
        this.month = calendar.get(Calendar.MONTH) + 1
        this.date = calendar.get(Calendar.DATE)
        this.type = type
        this.onDatePickerListener = onDatePickerListener
        return this
    }

    fun showDialog(mChildFragmentManager: FragmentManager) {
        try {
            if (this.isAdded()) {
                ////LogVnp.d("Added");
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (datepicker == null || datepicker!!.datePicker == null || !datepicker!!.datePicker.isShown)
                    this.show(mChildFragmentManager, "DATEP")
            } else {
                if (!this.datepicker!!.isShowing) {
                    this.show(mChildFragmentManager, "DATEP")
                }
            }
        } catch (e: Exception) {
            ////LogVnp.Shape1(Shape1);
        }

    }

    fun setSelectedDate(datetime: String): DialogDatePickerHistoryRange {
        var datetime = datetime
        try {
            if (TextUtils.isDigitsOnly(datetime)) {
                datetime = getFormDate(datetime)
            }
            val dts = UIModel.getInstance().parseDate(datetime)
            this.year = dts[2]
            this.month = dts[1] - 1
            this.date = dts[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return this
    }

    fun setSelectedDateYYYYMMdd(datetime: String) {
        var datetime = datetime
        try {
            if (TextUtils.isDigitsOnly(datetime)) {
                datetime = datetime.substring(6) + "/" + datetime.substring(
                    4,
                    6
                ) + "/" + datetime.substring(0, 4)
            }
            val dts = UIModel.getInstance().parseDate(datetime)
            this.year = dts[2]
            this.month = dts[1] - 1
            this.date = dts[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getFormDate(date: String): String {
        return date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4)
    }

    fun setDefaultMonthAgo(): DialogDatePickerHistoryRange {
        val c0 = Calendar.getInstance()
        c0.add(Calendar.MONTH, -1)
        year = c0.get(Calendar.YEAR)
        month = c0.get(Calendar.MONTH) + 1
        date = c0.get(Calendar.DAY_OF_MONTH)
        return this
    }

    fun setMinDateMonthAgo(): DialogDatePickerHistoryRange {
        return this

    }

    fun setMinDateYearAgo(): DialogDatePickerHistoryRange {
        try {
            val c0 = Calendar.getInstance()
            c0.add(Calendar.YEAR, -1)
            this.minDate = c0.getTimeInMillis() / 1000
        } catch (e: Exception) {
            ////LogVnp.Shape1(Shape1);
        }

        return this
    }

    /*From Date */
    fun setMinDate(range: Int, type: Int): DialogDatePickerHistoryRange {
        val calendar = Calendar.getInstance()
        this.maxDate = calendar.timeInMillis / 1000
        calendar.add(type, -range)
        this.minDate = calendar.timeInMillis / 1000
        return this
    }

    fun setMinDateFuture(range: Int, type: Int): DialogDatePickerHistoryRange {
        val calendar = Calendar.getInstance()
        this.minDate = calendar.timeInMillis / 1000
        calendar.add(type, range)
        this.maxDate = calendar.timeInMillis / 1000
        return this
    }

    /*To Date */
    fun setMinDate(
        date: String,
        range: Int,
        type: Int,
        pattern: String
    ): DialogDatePickerHistoryRange {
        this.minDate = UIModel.getInstance().getDateTime(date, pattern)
        val calendar = Calendar.getInstance()
        this.maxDate = calendar.timeInMillis / 1000
        calendar.timeInMillis = minDate * 1000
        calendar.add(type, range)
        this.year = calendar.get(Calendar.YEAR)
        this.month = calendar.get(Calendar.MONTH)
        this.date = calendar.get(Calendar.DATE)

        val timeMax = calendar.timeInMillis / 1000
        if (timeMax < this.maxDate)
            this.maxDate = timeMax
        return this
    }

    fun setMinDateFuture(
        date: String,
        range: Int,
        type: Int,
        pattern: String
    ): DialogDatePickerHistoryRange {
        this.minDate = UIModel.getInstance().getDateTime(date, pattern)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = minDate * 1000
        calendar.add(type, range)
        this.year = calendar.get(Calendar.YEAR)
        this.month = calendar.get(Calendar.MONTH)
        this.date = calendar.get(Calendar.DATE)

        val timeMax = calendar.timeInMillis / 1000
        this.maxDate = timeMax
        return this
    }


    override fun onDismiss(dialog: DialogInterface) {
        try {
            this.maxDate = calendar.getTimeInMillis() / 1000
        } catch (e: Exception) {
            ////LogVnp.Shape1(Shape1);
        }

        super.onDismiss(dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        try {
            datepicker = DatePickerDialog(getActivity()!!,  R.style.ThemeDate,this, year, month, date)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                datepicker!!.datePicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS
                if (maxDate > minDate)
                    datepicker!!.datePicker.minDate = minDate * 1000
                datepicker!!.datePicker.maxDate = maxDate * 1000
            }
            return datepicker!!

        } catch (e: Exception) {
        }
        return super.onCreateDialog(savedInstanceState)

    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if (onDatePickerListener != null)
            this.onDatePickerListener!!.setData(
                UIModel.getInstance().getTypeDate(
                    dayOfMonth,
                    monthOfYear + 1,
                    year,
                    "/"
                ), type
            )
    }

    companion object {

        val FROM_DATE = 1
        val TO_DATE = 2

        fun newInstance(
            onDatePickerListener: OnDatePickerListener,
            type: Int
        ): DialogDatePickerHistoryRange {
            val dialog = DialogDatePickerHistoryRange()
            dialog.calendar = Calendar.getInstance()
            dialog.maxDate = dialog.calendar.getTimeInMillis() / 1000
            dialog.setListener(onDatePickerListener, type)
            return dialog
        }
    }

}
