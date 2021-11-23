package com.vnpay.anlmk.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.vnpay.anlmk.R;

public class ConfirmDialog extends Dialog implements View.OnClickListener {
    private String mContent;
    private String mLeftTitle;
    private String mRightTitle;
    private TextView mBtLeft;
    private TextView mBtRight;
    private TextView mTvContent;
    private View.OnClickListener leftOnClick;
    private View.OnClickListener rightOnClick;
    private Context mContext;
    private boolean needActionAll;

    public ConfirmDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice);
//        mTvTitle =  findViewById(R.id.tv_title);
        mTvContent = findViewById(R.id.tv_content);
        mBtRight = findViewById(R.id.bt_right);
        mBtLeft = findViewById(R.id.bt_left);

        if (mLeftTitle != null) {
            if (mRightTitle != null)
                mBtRight.setText(mRightTitle);
            mBtRight.setOnClickListener(rightOnClick != null ? rightOnClick : this);
            if (mBtLeft.getVisibility() != View.VISIBLE)
                mBtLeft.setVisibility(View.VISIBLE);
            mBtLeft.setOnClickListener(leftOnClick != null ? leftOnClick : this);
            mBtLeft.setText(mLeftTitle);
        } else {
            mBtLeft.setVisibility(View.GONE);
            mBtRight.setOnClickListener(rightOnClick != null ? rightOnClick : this);
            if (mRightTitle != null)
                mBtRight.setText(mRightTitle);
        }

        setContent(mContent);
        setTouchArea(true);
        setOnCancelListener(
                new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        actionDialog();
                    }
                }
        );
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        //actionDialog();
    }

    private void actionDialog() {
        if (needActionAll && rightOnClick != null) {
            if (leftOnClick != null && !(leftOnClick instanceof ConfirmDialog))
                leftOnClick.onClick(mBtLeft);
            else
                rightOnClick.onClick(mBtRight);
        }
    }


    public void show(String content) {
        if (mTvContent == null) {
            mContent = content;
        } else {
            mTvContent.setText(content);
            mBtRight.setOnClickListener(this);
        }
        setTouchArea(true);
        if (!isShowing())
            show();
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }

    public void setAction(boolean needActionAll) {
        this.needActionAll = needActionAll;
    }

    public void setTouchArea(boolean b) {
        setCancelable(b);
        setCanceledOnTouchOutside(b);
    }

    public ConfirmDialog newBuild() {
        if (mBtLeft != null) {
            mBtLeft.setVisibility(View.GONE);
            mBtLeft.setOnClickListener(this);
        }
        this.needActionAll = false;
        leftOnClick = null;
        rightOnClick = this;
        mLeftTitle = null;
        mRightTitle = getContext().getString(R.string.agree);

        if (mBtRight != null) {
            mBtRight.setOnClickListener(rightOnClick);
            mBtRight.setText(mRightTitle);
        }
        return this;
    }


    public ConfirmDialog setNotice(String content) {
        try {
            setContent(content);
            if (!isShowing())
                show();
        }catch (Exception e){}
        return this;
    }

    private void setContent(String content) {
        mContent = content;
        if (mTvContent != null) {
            if (mContent.contains("<strong>") || mContent.contains("</a>"))
                mTvContent.setText(Html.fromHtml(mContent));
            else {
                mTvContent.setText(mContent);
                try {
                    Linkify.addLinks(mTvContent, Linkify.ALL);
                } catch (Exception e) {
                    android.util.Log.wtf("EXX", e);
                }
            }
        }
    }

    public ConfirmDialog setNotice(@StringRes int idContent) {
        mContent = getContext().getString(idContent);
        setContent(mContent);
        try {
            if (!isShowing())
                show();
        } catch (Exception e) {
        }
        return this;
    }

    public ConfirmDialog addButtonLeft(@StringRes int id) {
        return addButtonLeft(getContext().getString(id));
    }

    public ConfirmDialog addButtonLeft(View.OnClickListener onLeftOnClick) {
        if (mBtLeft != null) {
            mBtLeft.setOnClickListener(onLeftOnClick != null ? onLeftOnClick : this);
            mBtLeft.setVisibility(View.VISIBLE);
            mBtLeft.setText(getContext().getString(R.string.agree));
        } else {
            mLeftTitle = getContext().getString(R.string.cancel);
            leftOnClick = onLeftOnClick != null ? onLeftOnClick : this;
        }
        return this;
    }

    public ConfirmDialog addButtonLeft(String title) {
        leftOnClick = this;
        if (mBtLeft != null) {
            mBtLeft.setOnClickListener(leftOnClick);
            mBtLeft.setVisibility(View.VISIBLE);
            mBtLeft.setText(title);
        } else {
            mLeftTitle = title;
        }
        return this;
    }

    public ConfirmDialog addButtonLeft(int title, View.OnClickListener onLeftOnClick) {
        addButtonLeft(getContext().getString(title), onLeftOnClick);
        return this;
    }

    public ConfirmDialog addButtonLeft(String title, View.OnClickListener onLeftOnClick) {
        leftOnClick = onLeftOnClick != null ? onLeftOnClick : this;
        if (mBtLeft != null) {
            mBtLeft.setOnClickListener(leftOnClick);
            mBtLeft.setVisibility(View.VISIBLE);
            mBtLeft.setText(title);
        } else {
            mLeftTitle = title;
        }
        return this;
    }

    public ConfirmDialog addButtonRight(View.OnClickListener onRightClick) {
        rightOnClick = onRightClick != null ? onRightClick : this;
        if (mBtRight != null) {
            mBtRight.setOnClickListener(rightOnClick);
            mBtRight.setText(R.string.agree);
        } else {
            mRightTitle = getContext().getString(R.string.agree);
        }
        return this;
    }

    public ConfirmDialog addButtonRight(int title, View.OnClickListener onRightClick) {
        addButtonRight(getContext().getString(title), onRightClick);
        return this;
    }

    public ConfirmDialog addButtonRight(int title) {
        addButtonRight(getContext().getString(title), null);
        return this;
    }

    public ConfirmDialog addButtonRight(String title, View.OnClickListener onRightClick) {
        rightOnClick = onRightClick != null ? onRightClick : this;
        if (mBtRight != null) {
            mBtRight.setOnClickListener(rightOnClick);
            mBtRight.setText(title);
        } else {
            mRightTitle = title;
        }
        return this;
    }

    public void removeActionAll() {
        needActionAll = false;
    }

    public interface OnClickListener {
        void onClick();
    }

}
