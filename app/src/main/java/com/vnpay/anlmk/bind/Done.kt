package com.vnpay.anlmk.bind


import androidx.annotation.IdRes
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by Lvhieu2016 on 12/6/2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Done(@IdRes val value: Int)