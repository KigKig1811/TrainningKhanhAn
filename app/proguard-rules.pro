
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-dontwarn **CompatHoneycomb
-keepattributes EnclosingMethod
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-assumenosideeffects class android.util.Log {
    *;
}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-assumenosideeffects class java.io.PrintStream {
     public void println(%);
     public void println(**);
 }
-assumenosideeffects class com.vnpay.namabank.common.LogVnp { *; }


-assumenosideeffects class java.lang.Throwable {
     public void printStackTrace();
 }

-dontwarn com.squareup.**
-keep class com.squareup.** { *; }
-dontwarn org.sqlite.**
-keep class org.sqlite.** { *; }
-dontwarn net.sqlcipher.**
-keep class net.sqlcipher.** { *; }
-keep class com.vnpay.anlmk.data.b {*;}
-dontwarn com.samsung.**
-keep class com.samsung.** { *; }
-keep class com.vnpay.service.message.** { *; }
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep class com.vnpay.oD.a { *; }


-dontwarn okio.**
-keep class okio.** { *; }
-dontwarn io.netty.**
-keep class io.netty.** { *; }
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn io.realm.**
-keep class io.realm.** { *; }
-keep class android.support.design.widget.** { *; }
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames class * implements android.os.Parcelable
-keepclassmembers class * implements android.os.Parcelable {
  public static final *** CREATOR;
}
-keep class io.fabric.sdk.android.** { *; }
-keep @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *

-keep @interface com.google.android.gms.common.annotation.KeepName
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}

-keep @interface com.google.android.gms.common.util.DynamiteApi
-keep public @com.google.android.gms.common.util.DynamiteApi class * {
  public <fields>;
  public <methods>;
}
-dontwarn android.security.NetworkSecurityPolicy
-verbose
-dontoptimize
-dontshrink
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn com.google.**
-dontwarn org.apache.log4j.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Notification
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.widget.ImageView
-keep class com.vcb.vcbpay.oD.a { *; }
-keep public class * extends java.lang.annotation.Annotation {*;}
-keepattributes *Annotation*
-keep class org.acra.** { *; }
-dontwarn org.apache.**
-keep class org.apache.** { *; }
-keep class com.google.** { *; }


-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes Annotation

-keep class sun.misc.Unsafe { *; }



-dontwarn io.netty.**
-keep class io.netty.** { *; }
-keep class com.vnpay.vexemphim.entity.** { *; }
-keep class com.vnpay.ticketlib.** { *; }
-keep class com.bookinghotel.entity.** { *; }
-keep class com.vn.vntalk.entity.** { *; }
-keepclassmembers enum * { *; }
-keep class com.vcb.vcbpay.entity.notify.** { *; }
-dontwarn com.google.maps.android.**
-keep public class com.google.maps.android.** {*;}


# Remove Android logging calls (in this case, including errors).
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
    public static java.lang.String getStackTraceString(java.lang.Throwable);
}

# Remove Java logging calls.
-assumenosideeffects class java.util.logging.Logger {
    public static java.util.logging.Logger getLogger(...);
    public boolean isLoggable(java.util.logging.Level);
    public void entering(...);
    public void exiting(...);
    public void finest(...);
    public void finer(...);
    public void fine(...);
    public void config(...);
    public void info(...);
    public void warning(...);
    public void severe(...);
}
# Remove Apache Commons logging calls.
-assumenosideeffects class org.apache.commons.logging.Log {
    public boolean is*Enabled();
    public void trace(...);
    public void debug(...);
    public void info(...);
    public void warn(...);
    public void error(...);
    public void fatal(...);
}
