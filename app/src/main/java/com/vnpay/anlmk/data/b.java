package com.vnpay.anlmk.data;

import android.content.Context;

import com.vnpay.anlmk.BuildConfig;

public class b {

    public static boolean hook = !BuildConfig.DEBUG;
    public static int rooted = BuildConfig.DEBUG ? 0 : 10;
    public static boolean emulator = !BuildConfig.DEBUG;

    public static void a(Object data) {
        System.exit(0);
    }

    public static void b(Object data) {
    }

    public static void c(Context base) {
        //probe
    }

    public static void d(Object data) {
        rooted = 11;
    }

    public static void d2(Object data) {
        rooted = 0;
    }//negative root

    public static void x(Object data) {
        hook = true;
    }

    public static void x2(Object data) {
        hook = false;
    }

    public static void f(Object data) {
        emulator = true;
    }

    public static void f2(Object data) {
        emulator = false;
    }

    public static void e(Object data) {

    }
}
