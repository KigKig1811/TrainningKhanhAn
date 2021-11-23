package com.vnpay.anlmk.ui.dialogs.dates;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.vnpay.anlmk.R;
import com.vnpay.anlmk.utils.UIModel;

import java.util.Calendar;
import java.util.Locale;


public class DiaglogDatePickerFuture extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog datepicker = null;
    private OnDatePickerListener onDatePickerListener;
    private int year;
    private int month;
    private int date;
    Calendar calendar;
    private long minDate;
    private int type;

    public static DiaglogDatePickerFuture newInstance(OnDatePickerListener onDatePickerListener) {
        DiaglogDatePickerFuture dialog = new DiaglogDatePickerFuture();
        dialog.setListener(onDatePickerListener);
        Calendar c = Calendar.getInstance();
        dialog.year = c.get(Calendar.YEAR);
        dialog.month = c.get(Calendar.MONTH);
        dialog.date = c.get(Calendar.DATE);
        dialog.minDate = System.currentTimeMillis() / 1000;
        return dialog;
    }

    public void setListener(OnDatePickerListener onDatePickerListener) {
        calendar = Calendar.getInstance();
        this.onDatePickerListener = onDatePickerListener;
    }

    public DiaglogDatePickerFuture setMinDate(long minDate) {
        this.minDate = minDate;
        return this;
    }

    public void setDate(int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date = date;
    }

    public void showDialog(FragmentManager mChildFragmentManager) {
        if (this.isAdded()) {
            ////LogVnp.d("Added");
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (datepicker == null || datepicker.getDatePicker() == null || !datepicker.getDatePicker().isShown())
                    this.show(mChildFragmentManager, "B");
            } else {
                if (!this.datepicker.isShowing()) {
                    this.show(mChildFragmentManager, "B");
                }
            }
        } catch (Exception e) {
            ////LogVnp.Shape1(Shape1);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);
            datepicker = new DatePickerDialog(getActivity(), R.style.ThemeDate, this, year, month, date);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                datepicker.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                datepicker.getDatePicker().setMinDate(minDate * 1000);
            }
        } catch (Exception e) {
            ////LogVnp.Shape1(Shape1);
        }
        return datepicker;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.date = dayOfMonth;
        this.onDatePickerListener.setData(UIModel.getInstance().getTypeDate(dayOfMonth, monthOfYear+1, year, "/"), type);
    }

}
