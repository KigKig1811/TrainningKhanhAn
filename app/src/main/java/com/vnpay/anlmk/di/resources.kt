package com.vnpay.anlmk.di

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.provider.SyncStateContract
import androidx.annotation.*
import androidx.core.content.ContextCompat

interface ResourceProvider {
    fun getString(resourceIdentifier: Int, vararg arguments: Any = arrayOf()): String

    fun getStringArray(resourceIdentifier: Int): Array<String>

    fun getInteger(resourceIdentifier: Int): Int

    fun getIntegerArray(resourceIdentifier: Int): Array<Int>

    fun getBoolean(resourceIdentifier: Int): Boolean

    fun getColor(resourceIdentifier: Int): Int
    fun startActvity(des: Class<out Activity>): Unit

}

class AndroidResourceProvider
constructor(private val context: Context) : ResourceProvider {
    override fun startActvity(des: Class<out Activity>) {
        context.startActivity(Intent(context, des))
    }

    override fun getString(@StringRes resourceIdentifier: Int, vararg arguments: Any): String {
        return if (arguments.isNotEmpty())
            context.resources.getString(resourceIdentifier, *arguments)
        else
            context.resources.getString(resourceIdentifier)
    }

    override fun getStringArray(@ArrayRes resourceIdentifier: Int): Array<String> =
        context.resources.getStringArray(resourceIdentifier)

    override fun getInteger(@IntegerRes resourceIdentifier: Int): Int =
        context.resources.getInteger(resourceIdentifier)

    override fun getIntegerArray(@ArrayRes resourceIdentifier: Int): Array<Int> =
        context.resources.getIntArray(resourceIdentifier).toTypedArray()


    override fun getBoolean(@BoolRes resourceIdentifier: Int): Boolean =
        context.resources.getBoolean(resourceIdentifier)

    override fun getColor(@ColorRes resourceIdentifier: Int): Int =
        ContextCompat.getColor(context, resourceIdentifier)

}

//object Commont {
//    //    public static Vector<MyEmoticon> mAllEmoSet;
//    var tfRoboto_Regular: Typeface? = null
//    var tfRoboto_Bold: Typeface? = null
//    var tfRoboto_Medium: Typeface? = null
//}


@SuppressLint("StaticFieldLeak")
public object Common {
    @JvmStatic
    lateinit var baseActivity: Activity

    var tfRoboto_Regular: Typeface? = null

    var tfRoboto_Italics: Typeface? = null

    var tfRoboto_Medium: Typeface? = null
}

