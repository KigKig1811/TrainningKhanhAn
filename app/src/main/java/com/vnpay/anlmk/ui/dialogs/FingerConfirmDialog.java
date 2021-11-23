package com.vnpay.anlmk.ui.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.vnpay.anlmk.R;


/**
 * Created by Lvhieu2016 on 12/12/2016.
 */

public class FingerConfirmDialog extends Dialog {
    private ImageView closeBtn;
    private ImageView fingerBtn;
    private TextView statusText;
    private TextView loginBtn;
    private TextView cancel;
    private OutsideDialog event;
    private int type = 0;
    public static final int LOGIN_TYPE = 0;
    public static final int CONFIRM_TYPE = 1;
    public static final int CONFIG_TYPE = 3;

    public FingerConfirmDialog(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public FingerConfirmDialog setEvent(OutsideDialog event) {
        this.event = event;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_otp_main);
        closeBtn = findViewById(R.id.close_btn);
        fingerBtn = findViewById(R.id.finger_btn);
        statusText = findViewById(R.id.text_status);
        loginBtn = findViewById(R.id.login_btn);
        cancel = findViewById(R.id.cancel);
        setCanceledOnTouchOutside(false);
        if (type != LOGIN_TYPE) {
            loginBtn.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            closeBtn.setVisibility(View.INVISIBLE);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event != null)
                    event.useKeyBoard();
                cancel();
            }
        });

       setOnCancelListener(new OnCancelListener() {
           @Override
           public void onCancel(DialogInterface dialog) {
               if (event != null)
                   event.onOutsite();
           }
       });


    }

    public FingerConfirmDialog setTypeDialog(int type) {
        try {
            this.type = type;
            if (type != LOGIN_TYPE && loginBtn != null) {
                loginBtn.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                closeBtn.setVisibility(View.INVISIBLE);
            }
            if (statusText != null)
                statusText.setText(Html.fromHtml(getStatus()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void show() {
        onCloseDialog();
        super.show();
    }

    private void onCloseDialog() {
        if (fingerBtn != null) {
            fingerBtn.setSelected(false);
            statusText.setSelected(false);
            statusText.setText(Html.fromHtml(getStatus()));
        }
    }

    public void setStatusFailed(@StringRes int idText) {
        setStatusFailed(getString(idText));
    }

    public void setStatusFailed(String text) {
        try {
            if (statusText != null && fingerBtn != null) {
                fingerBtn.setSelected(true);
                statusText.setSelected(true);
                if (TextUtils.isEmpty(text))
                    text = getString(R.string.finger_error);
                statusText.setText(text);
            }
        } catch (Exception e) {
        }
    }

    private String getString(@StringRes int id) {
        return getContext().getString(id);
    }

    public String getStatus() {
        switch (type) {
            case LOGIN_TYPE:
            case CONFIG_TYPE:
                return getString(R.string.use_finger_login);
            case CONFIRM_TYPE:
                return getString(R.string.use_finger_transaction);
        }
        return getString(R.string.use_finger_login);
    }

    public void dismissNoAction() {
        try {
            dismiss();
        } catch (Exception e) {
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }

    public interface OutsideDialog {
        void onOutsite();

        void useKeyBoard();

    }
}
