package com.vnpay.anlmk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.vnpay.anlmk.BuildConfig;
import com.vnpay.anlmk.di.Common;

import org.jetbrains.annotations.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UIModel {
    public static final String EN_LANG = "EN";
    public static final String VN_LANG = "VI";
    private static UIModel instance;
    private final String CURRENCY = "VND";
    private Typeface FONT_BOLD;
    private Typeface FONT_ITALIC;
    private Typeface FONT_NORMAL;
    private Typeface FONT_LIGHT;
    private Typeface FONT_REGULAR;
    private Gson gson;
    private String language = VN_LANG;
    public final String TAG = "TAG";
    public final int PERMISSION_ACCESS_FINE_LOCATION = 3;
    public String CurrencyDefault = "VND";
    public final String DATE_FORMAT_ddmmyyyy = "dd/MM/yyyy";
    private final String LICENSE_VEXEMPHIM_TEST = "ybJXDTQWJxnkAGuAAK5z2z7W4ONYCaSiai2JSpFupzXzx9Kl84v08KV+g3/QChFdGb04w/1FnHKv2qdpR6yZfMT8BkFCOxeSSMN5PacHO2ylBtTdZ" +
            "+ZAofpdqg94VfEoCDHsCc8yj6dT9IlQXZxi2VzmZG0jbCZFkeOf9M4FKRfi29omF75KMbtlUk8yHvVsb/z33/R60vs1/WT5ANN0R5Ts9sCz7frMM9DDQ3EDoEylXhLgS" +
            "+aofqnxTdrqDBoSNgJrGVrAZ1STcqkiimoxBiiYNoqQ8POwpiazqIsXp75R+YQO7oYBIN2AsfyOw1ZFY07kmQSIF296FqYIviTDIQifJQlnRqjip8ghoJFrreBTHu90lw5sZPGkxTUzhIabvd" +
            "+QinjRcX2QyswkvIy7VgZSvfjeRxdx4xpdWvTx2vp7Zng9oXPX4E3zbPyyTzV2RAllZyNyIJelwdvgRcYp7FwIukg" +
            "/7BZz2ZMnejk9VbcJvhsVMRpVI29HjPaiAiw7kQa36OWodgQr3tvmTyOVoJp2OBDFnPl51KbjbzWDhVthCivjKVQLinPlHmVYzVCU362pxg1xt3B/CgRGSLNyWe7UbB9Cm2EiBrjaWurx0V4QjMYkLA17G1iJ" +
            "/wNWR3ED9JAQDoQuJuSORfIse5HCFDKk+fNv/Egjrq8M1nGY1ZsyyPNDBl3UtIGJoKAj5KZpN9g91n9rtRTywlEyvjNb2Zjv";
    private final String LICENSE_VEXEMPHIM_LIVE = "S9GtX5gvh79vzBzLoOsql9yRDcu+vqlpxb6+rDMuOR06vzxlZ552O3ddRiFY" +
            "/wXQz88BMdLbuw5pBAirToFttqEs6SP5gYQWDJQB657LvcANgZFFgsRrgNDK3ylhIDuph2ZwLDc4swT4yUziCiEhp67j6Gy1N4rOTO8NRHKaR37avkvm0eDNiXOiy6SQcIZTH4gPwSE/fC0NiLPK51FN" +
            "/EBYpfmgWRCZvOkm16nNvEgKhAf5WEhS7De26AQdSziOl921Z0L5Gy0Rh3RvEkyncEx9VN9zCrpPwgm0m3Vd+hee" +
            "+63sWkBiaoQvnVNMRl4sAXbYEarrwMfMcD76uBIpAaJDg7ZjBm3Ty77mazUugU3SMfBTH7lnwwIF47eqXBZaIE0T7ZvPwbvcQZ7Y1U5hjO/zKi4EUU2UzKDVErFjjTCIRMetqeE+VauiPwu8L4S0aZ" +
            "+8f8WjmLX236uzGSWT0cIQN12zMCPlEBF9+UqYBIsRtu8uGrDIhIV+FCE5mi0dWC2qLgNHdOzS9IkyN5t9dnpMt7j4/Yr58YtP8c3f" +
            "+9CAVQSp1wJvjKDN9GJ3apQgscxGtX2FsE89Bd0B0YFs6Y1XHKUm0kKTqJZLMhktoBdZykSG6x1OUrrnQf/5V3M/L8VauwX1TT/0qwPpNZx3y0tDG/5Qb+cJgttcnbGgqBlIb6dNuwit8Zgh+T9BZA6Q/AWwebt+On03KgnE66w=";

    private UIModel() {

    }

    public static UIModel getInstance() {
        if (instance == null)
            instance = new UIModel();
        return instance;
    }

    public int getDPtoPX(Context context, float dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
        return px;
    }

    public boolean checkNetwork(Context context) {
        boolean b = false;
        if (context != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getNetworkInfo(1);
                if (networkInfo != null && networkInfo.isConnected())
                    b = true;
                else {
                    final NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
                    if (networkInfo2 != null && networkInfo2.isConnected())
                        b = true;
                    else {
                        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                        b = false;
                        if (activeNetworkInfo != null)
                            b = activeNetworkInfo.isConnected();
                    }
                }
            }
        }
        return b;
    }

    public String getLang() {
        return language;
    }

    public Gson provideGson() {
        if (gson == null)
            gson = new GsonBuilder().create();
        return gson;
    }

    public String getDotMoneyHasCcy(String str, String ccy) {
        if (!TextUtils.isEmpty(ccy))
            return getDotMoney(str, ",") + " " + ccy;
        return getDotMoney(str, ",");
    }

    public String getDotMoney(String str) {
        return getDotMoney(str, ",");// sẽ dùng . phân cách hàng thập phân
    }

    public String getDotMoney(String str, String dot) {
        try {
            String splitCcy;
            String temp[];
            if (".".equals(dot)) {
                splitCcy = ",";
                temp = str.split(",");

            } else {
                splitCcy = ".";
                temp = str.split("\\.");
            }

            if (temp.length < 2) {
                return str.replaceAll("[^\\d-]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", dot);
            } else {
                if( temp[1].equals("00") || temp[1].equals("0")){
                    return getDotMoney(temp[0], dot);
                }
                String rs = temp[0].replaceAll("[^\\d-]", "").replaceAll("\\B(?=(\\d{3})+(?!\\d))", dot);
                if (!"0".equals(temp[1]) && !"00".equals(temp[1]))
                    rs += splitCcy + temp[1];
                return rs;
            }
        } catch (Exception e) {
        }
        return str;
    }

    public String Captain(String str) {
        try {
            return (str.substring(0, 1).toUpperCase() + str.substring(1, str.length()));
        } catch (Exception e) {
        }
        return str;
    }

    public long getTimeIntDate(String dateTime, String formatDate) {
        try {
            SimpleDateFormat s = new SimpleDateFormat(formatDate, Locale.US);
            Date c = s.parse(dateTime);
            return c.getTime();
        } catch (Exception e) {
        }
        return 0;
    }

    public void Vibrate() {
        try {
            Vibrator v = (Vibrator) Common.baseActivity.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(50);
        } catch (Exception e) {
        }
    }

    public void logout(@Nullable String value) {
//        try {
//            baseActivity.startActivity(
//                    new Intent(baseActivity, ActiveActivity.class).addFlags(
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    .putExtra(Tags.DES, value)
//            );
//            ActivityCompat.finishAffinity(Common.baseActivity);
//            baseActivity.finish();
//        } catch (Exception e) {
//            System.exit(0);
//        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Common.baseActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    Picasso picasso;

    public Picasso providePicasso(Context context) {
        if (picasso == null)
            picasso = new Picasso.Builder(context)
                    .downloader(new OkHttp3Downloader(context.getCacheDir(), 250000000))
                    .memoryCache(new LruCache(419430400))
                    .listener((picasso, uri, exception) -> {

                    })
                    .build();
        return picasso;
    }

    public boolean isMaxRangeTime(String fromDate, String toDate, int i, String pattern) {
        return getDateTime(toDate, pattern) - getDateTime(fromDate, pattern) > i * 86400;
    }

    public long getDateTime(String time, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.US);
        long i = -1;
        try {
            Date d = df.parse(time);
            i = d.getTime() / 1000;
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return i;
    }

    public String getToDay() {
        Calendar calendar = Calendar.getInstance();
        return getTypeDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), "/");
    }

    public String getTypeDate(int d, int m, int y, String pattern) {
        return (d < 10 ? "0" + d : d) + "" + pattern + "" + (m < 10 ? "0" + m : m) + "" + pattern + "" + getBeautyNumber(y);
    }

    public String getTypeDate(Calendar c, String pattern) {
        return getBeautyNumber(c.get(c.DATE)) + "" + pattern + "" + getBeautyNumber(c.get(c.MONTH) + 1) + "" + pattern + "" + getBeautyNumber(c.get(c.YEAR));
    }

    public String getBeautyNumber(int size) {
        return size == 0 || size > 9 ? "" + size : "0" + size;
    }

    public String getMonthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        return getTypeDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), "/");
    }

    public String getWeekAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return getTypeDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), "/");
    }

    public int[] parseDate(String date) {
        try {
            Pattern r = Pattern.compile("([0]*[1-9]|[12][0-9]|3[01])[- /.]([0]*[1-9]|1[012])[- /.]([\\d]{4})");
            Matcher m = r.matcher(date);
            if (m.find()) {
                return new int[]{Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3))};
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String convertDate(String fromFormat, String toFormat, String strDate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromFormat);
            SimpleDateFormat simpleTo = new SimpleDateFormat(toFormat);
            return simpleTo.format(simpleDateFormat.parse(strDate));
        } catch (ParseException ex) {
        }
        return strDate;
    }

    public boolean isVietNamese() {
        return true;
    }

    public String removeAccentNormalize(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        str = str.toLowerCase();
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[éèẽẹêẻềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replace("đ", "d");

        return str;
    }

    private HashMap<String, String> keys;
    private Pattern hiphopPattern;

    public String removeAccent(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        str = str.replace("Đ", "D");
        str = str.replaceAll("[ỲÝỴỶỸ]", "Y");
        str = str.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        str = str.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        str = str.replaceAll("[ÌÍỊỈĨ]", "I");
        str = str.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        str = str.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");

        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[éèẽẹêẻềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replace("đ", "d");

        return str;
    }

    public String removeAccentHipHop(String str) {
        genKeys();
        if (hiphopPattern == null) {
            String DATE_PATTERN = "([^(a-zA-Z|\\s)|@#$%^&*!\\/\\\\])";
            hiphopPattern = Pattern.compile(DATE_PATTERN);
        }
        Matcher m = hiphopPattern.matcher(str);
        String lastKey = "";
        while (m.find()) {
            lastKey = m.group();
        }
        if (!lastKey.isEmpty() && keys.containsKey(lastKey)) {
            return keys.get(lastKey);
        }
        return " " + str;
    }

    private void genKeys() {
        if (keys == null) {
            keys = new HashMap<String, String>();
            keys.put("Đ", "D");
            keys.put("Ê", "E");
            keys.put("É", "S");
            keys.put("È", "F");
            keys.put("Ẹ", "J");
            keys.put("Ẻ", "R");
            keys.put("Ẽ", "X");
            keys.put("Ý", "S");
            keys.put("Ỳ", "F");
            keys.put("Ỵ", "J");
            keys.put("Ỷ", "R");
            keys.put("Ỹ", "X");
            keys.put("Ư", "W");
            keys.put("Ú", "S");
            keys.put("Ù", "F");
            keys.put("Ụ", "J");
            keys.put("Ủ", "R");
            keys.put("Ũ", "X");
            keys.put("Ô", "O");
            keys.put("Ơ", "W");
            keys.put("Ó", "S");
            keys.put("Ò", "F");
            keys.put("Ọ", "J");
            keys.put("Ỏ", "R");
            keys.put("Õ", "X");
            keys.put("ƯƠ", "W");
            keys.put("Â", "A");
            keys.put("Á", "S");
            keys.put("À", "F");
            keys.put("Ạ", "J");
            keys.put("Ả", "R");
            keys.put("Ã", "X");
            keys.put("Í", "S");
            keys.put("Ì", "F");
            keys.put("Ị", "J");
            keys.put("Ỉ", "R");
            keys.put("Ĩ", "X");
            keys.put("ê", "e");
            keys.put("é", "s");
            keys.put("è", "f");
            keys.put("ẹ", "j");
            keys.put("ẻ", "r");
            keys.put("ẽ", "x");
            keys.put("ý", "s");
            keys.put("ỳ", "f");
            keys.put("ỵ", "j");
            keys.put("ỷ", "r");
            keys.put("ỹ", "x");
            keys.put("ư", "w");
            keys.put("ú", "s");
            keys.put("ù", "f");
            keys.put("ụ", "j");
            keys.put("ủ", "r");
            keys.put("ũ", "x");
            keys.put("ô", "o");
            keys.put("ơ", "w");
            keys.put("ó", "s");
            keys.put("ò", "f");
            keys.put("ọ", "j");
            keys.put("ỏ", "r");
            keys.put("õ", "x");
            keys.put("ươ", "w");
            keys.put("â", "a");
            keys.put("ă", "w");
            keys.put("á", "s");
            keys.put("à", "f");
            keys.put("ạ", "j");
            keys.put("ả", "r");
            keys.put("ã", "x");
            keys.put("í", "s");
            keys.put("ì", "f");
            keys.put("ị", "j");
            keys.put("ỉ", "r");
            keys.put("ĩ", "x");
            keys.put("đ", "d");
        }
    }

    public String getToday() {
        Calendar c = Calendar.getInstance();
        return
                getBeautyNumber(c.get(Calendar.DAY_OF_MONTH)) + "/" + getBeautyNumber(c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
    }

    public void writeFile(String fileName, Bitmap bm) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            Log.wtf("EXC", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {

            }
        }
    }

    public String getCurrentDateTime2() {
        Calendar c = Calendar.getInstance();
        return getBeautyNumber(c.get(Calendar.HOUR_OF_DAY)) + ":" + getBeautyNumber(c.get(Calendar.MINUTE)) + " " +
                getBeautyNumber(c.get(Calendar.DAY_OF_MONTH)) + "/" + getBeautyNumber(c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
    }

    public void flateIcon(PopupMenu popup) {
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(popup) instanceof MenuPopupHelper) {
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLicenseVexemphim() {
        if (BuildConfig.SERVER)
            return LICENSE_VEXEMPHIM_LIVE;
        else
            return LICENSE_VEXEMPHIM_TEST;
    }

    public double convertDouble(String value) {
        try {
            DecimalFormat f = new DecimalFormat("###,##0.###");
            NumberFormat format = NumberFormat.getInstance(Locale.CHINESE);
            return format.parse(value).doubleValue();
        } catch (ParseException e) {
            return 0;
        }

    }

    public void requestPermission(final FragmentActivity context, String Permission, int requestCode) {
        Log.wtf(UIModel.getInstance().TAG, "Permission has NOT been granted. Requesting permission.");
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Permission)) {
            Log.wtf(UIModel.getInstance().TAG, "Permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(context, new String[]{Permission}, requestCode);
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Permission}, requestCode);
        }
    }

    public String formatDoubleGia(String value, int num) {
        if (TextUtils.isEmpty(value)) return "";
        value = value.replace('+', ' ').trim();
        String s = "";
        if (value.contains(".")) {
            String[] arrMoney = value.split("\\.");
            s = formatDotMoney(arrMoney[0], num);
            s = s + "." + arrMoney[1];
        } else {
            s = formatDotMoney(value, num);
        }
        return s;
    }

    public String formatDotMoney(String value, int num) {
        try {
            DecimalFormat f = null;
            switch (num) {
                case 0:
                    // case 2:
                    f = new DecimalFormat("###,###");
                    break;
                case 1:
                    f = new DecimalFormat("###,##0.0");
                    break;
                case 2:
                    f = new DecimalFormat("###,##0.00");
                    break;
                case 3:
                    f = new DecimalFormat("###,##0.000");
                    break;
                default:
                    f = new DecimalFormat("###,###");
                    break;
            }

            NumberFormat format = NumberFormat.getInstance(Locale.CHINESE);
            double d = format.parse(value).doubleValue();
            String s = f.format(d);

            int index1 = s.indexOf(".");
            int index2 = s.indexOf(",");
            if (index1 > index2) {
                s = s.replace(',', 'a');
                s = s.replace('.', ',');
                s = s.replace('a', '.');
            }
            return s;
        } catch (Exception e) {
            return "";
        }
    }

    public int convertInt(String input) {
        input = input.replaceAll("[^\\d-]", "");
        if (TextUtils.isEmpty(input)) {
            return 0;
        }
        return Integer.parseInt(input);
    }

    public String getAWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        return getTypeDate(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), "/");
    }

    public String hiddenNumber(String phone, int startLength, int endLength) {
        try {
            int lengh = phone.length();
            StringBuilder start = new StringBuilder(phone.substring(0, startLength));
            for (int i = 0; i < lengh - startLength - endLength; i++) {
                start.append("*");
            }
            start.append(phone.substring(phone.length() - endLength, phone.length()));
            return start.toString();
        } catch (Exception e) {
            Log.wtf("EX", e);
        }
        return phone;
    }

    public Bitmap getBitmapFromDrawable(Context context, Drawable drawable) {
        try {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                return bitmap;
            }
        } catch (Exception e) {
            android.util.Log.wtf("EXX", e);
        }
        return null;
    }
}
