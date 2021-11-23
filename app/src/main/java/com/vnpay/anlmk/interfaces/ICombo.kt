package com.vnpay.anlmk.interfaces


/**
 * Created by Lvhieu2016 on 10/17/2016.
 */

interface ICombo {
    @Throws(Exception::class)
    fun actionItem(item: Any, type: Int)
}