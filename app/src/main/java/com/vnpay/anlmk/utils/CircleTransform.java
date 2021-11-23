package com.vnpay.anlmk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

/**
 * Created by LeHieu on 9/29/2017.
 */

public class CircleTransform implements Transformation {
    private final Context context;
    private final Paint paint;

    public CircleTransform(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int max = Math.max(source.getWidth(), source.getHeight());
        int width = (source.getWidth() - max) / 2;
        int height = (source.getHeight() - max) / 2;
        float scale = 1f;
        if (width > height) {
            scale = width / height;
        } else if (height > width) {
            scale = height / width;
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        BitmapShader shader =
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        source.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}
