package com.vnpay.anlmk.bind

/**
 * Created by LeHieu on 8/18/2017.
 */

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Tap(vararg val value: Int)
