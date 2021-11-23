package com.vnpay.anlmk.ui.entites

/**
 * Created by Lvhieu2016 on 9/20/2016.
 */

class CommonEntity {
    private var icon: Int = 0
    private var title: String? = null
    private var descript: String? = null
    var destiny: String? = null
    private var isHightLight: Boolean = false
    private var typeLayout: Int = 0
    private var counter: Int = 0
    private var dataToolTip: String? = null

    fun isHighLight(): Boolean {
        return isHightLight
    }

    fun setHightLight(hightLight: Boolean): CommonEntity {
        isHightLight = hightLight
        return this
    }

    fun getTypeLayout(): Int {
        return typeLayout
    }

    fun setTypeLayout(typeLayout: Int): CommonEntity {
        this.typeLayout = typeLayout
        return this
    }


    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?): CommonEntity {
        this.title = title
        return this
    }

    fun getIcon(): Int {
        return icon
    }

    fun setIcon(icon: Int): CommonEntity {
        this.icon = icon
        return this
    }

    fun getDescript(): String? {
        return descript
    }

    fun setDescript(descript: String?): CommonEntity {
        this.descript = descript
        return this
    }


    fun getCounter(): Int {
        return counter
    }


    fun setCounter(counter: Int): CommonEntity {
        this.counter = counter
        return this
    }

    fun setDestiny(destiny: String): CommonEntity {
        this.destiny = destiny
        return this
    }


    constructor(title: String, descript: String?) {
        this.title = title
        this.descript = descript
    }

    constructor() {}

    fun setDataToolTip(dataToolTip: String): CommonEntity {
        this.dataToolTip = dataToolTip
        return this
    }
    fun getDataToolTip():String?{
        return dataToolTip
    }

}
