package com.vnpay.anlmk.ui.entites

class FunctionEntity {

    private var id: Int = 0
    private var title: String? = null
    private var image: Int=0
    private var backgroundImage:Int=0

    fun getTitle(): String? {
        return title
    }
    fun getId(): Int{
        return id
    }

    fun getImage():Int{
        return image
    }
    fun getBackgoundImage():Int{
        return backgroundImage
    }

    constructor(id: Int, title: String?) {
        this.title = title
        this.id = id
    }

    constructor(id: Int, title: String?,image:Int,backgroundImage: Int)
    {
        this.title = title
        this.id = id
        this.image=image
        this.backgroundImage=backgroundImage
    }

    constructor() {}

}