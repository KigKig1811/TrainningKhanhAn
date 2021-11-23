package com.vnpay.anlmk.walks;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneCapture implements TextWatcher {
    private String beforeText;
    private EditText mEditext;
    private int caretPoint;

    public PhoneCapture(EditText mEditext) {
        this.mEditext = mEditext;

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        this.beforeText = s.toString().trim();
        caretPoint = mEditext.getSelectionEnd();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String p = s.toString().trim();
        if (!p.isEmpty()) {
            String f = this.beforeText == null ? "" : this.beforeText;
            if (!p.startsWith("0")) {
                setValueText("");
            } else if (p.startsWith("01")) {
                if (p.length() > 11) {
                    setValueText(f);
                }
            } else if (p.startsWith("0")) {
                if (p.length() > 10) {
                    setValueText(f);
                }
            } else if (p.length() > 1) {
                setValueText(f);
            }
        }
    }

    private void setValueText(String data) {
        this.mEditext.removeTextChangedListener(this);
        this.mEditext.setText(data);
        if (caretPoint > data.length())
            caretPoint = data.length();
        if (caretPoint < 0)
            caretPoint = 0;
        this.mEditext.setSelection(caretPoint);
        this.mEditext.addTextChangedListener(this);
    }
}
