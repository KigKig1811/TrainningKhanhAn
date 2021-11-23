package com.vnpay.anlmk.ui.entites

import java.io.Serializable

class InfoConfirmEmtity:Serializable {

    private var title: String? = null
    private var value: String? = null


    fun getTitle(): String? {
        return title
    }
    fun getValue(): String? {
        return value
    }

    fun setTitle(title: String?){
        this.title=title
    }
    fun setValue(value: String?) {
        this.value
    }
    constructor(title: String, value: String?) {
        this.title = title
        this.value = value
    }


    constructor() {}

}