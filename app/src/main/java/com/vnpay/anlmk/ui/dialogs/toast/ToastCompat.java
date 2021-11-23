package com.vnpay.anlmk.ui.dialogs.toast;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;


import com.vnpay.anlmk.R;

import java.lang.reflect.Field;

/**
 * A Toast to fix the bad token exception on API 25 devices.
 */
public final class ToastCompat extends Toast {

    /**
     * Base toast used by this toast compat.
     */
    private final @NonNull Toast mToast;

    public ToastCompat(Context context, @NonNull Toast base) {
        super(context);

        this.mToast = base;
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context The context to use.
     * @param text The text to show. Can be formatted text.
     * @param duration The duration for the toast, either {@link Toast#LENGTH_SHORT}
     *                 or {@link Toast#LENGTH_LONG}.
     */
    public static ToastCompat makeText(Context context, CharSequence text, int duration) {
        @SuppressLint("ShowToast")
        Toast toast = Toast.makeText(context, text, duration);
        setToastContext(toast.getView(), new ToastContext(context, toast));
        return new ToastCompat(context, toast);
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context The context to use.
     * @param resId The resource id of the string resource to use. Can be formatted text.
     * @param duration The duration for the toast, either {@link Toast#LENGTH_SHORT}
     *                 or {@link Toast#LENGTH_LONG}.
     */
    public static Toast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * Sets the toast context to fix bad token exception.
     *
     * @param view The view used by the toast
     * @param context The context used by the toast.
     */
    private static void setToastContext(@NonNull View view, @NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            try {
                Field field = View.class.getDeclaredField("mContext");
                field.setAccessible(true);
                field.set(view, context);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void show() {
        mToast.show();
    }

    @Override
    public void setDuration(int duration) {
        mToast.setDuration(duration);
    }

    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mToast.setGravity(gravity, xOffset, yOffset);
    }

    @Override
    public void setMargin(float horizontalMargin, float verticalMargin) {
        mToast.setMargin(horizontalMargin, verticalMargin);
    }

    @Override
    public void setText(int resId) {
        mToast.setText(resId);
    }

    @Override
    public void setText(CharSequence s) {
        mToast.setText(s);
    }

    @Override
    public void setView(View view) {
        mToast.setView(view);
        setToastContext(view, new ToastContext(view.getContext(), this));
    }

    @Override
    public float getHorizontalMargin() {
        return mToast.getHorizontalMargin();
    }

    @Override
    public float getVerticalMargin() {
        return mToast.getVerticalMargin();
    }

    @Override
    public int getDuration() {
        return mToast.getDuration();
    }

    @Override
    public int getGravity() {
        return mToast.getGravity();
    }

    @Override
    public int getXOffset() {
        return mToast.getXOffset();
    }

    @Override
    public int getYOffset() {
        return mToast.getYOffset();
    }

    @Override
    public View getView() {
        return mToast.getView();
    }

    public @NonNull Toast getToast() {
        return mToast;
    }

    public static @NonNull Toast make(@NonNull Context context, @Nullable CharSequence text,
                                      @DrawableRes int icon,  int duration) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            context = context.getApplicationContext();
        }
        ToastCompat toast = new ToastCompat(context, new Toast(context));
        View toastLayout = LayoutInflater.from(context).inflate(
                R.layout.layout_toats, new LinearLayout(context), false);
        ImageView toastIcon = toastLayout.findViewById(R.id.adt_toast_icon);
        TextView toastText = toastLayout.findViewById(R.id.adt_toast_text);

        if (icon != 0) {
            toastIcon.setImageDrawable(ContextCompat.getDrawable(context, icon));
        } else {
            toastIcon.setVisibility(View.GONE);
        }

        toastText.setText(text);

        toast.setDuration(duration);
        toast.setView(toastLayout);

        return toast;
    }
}