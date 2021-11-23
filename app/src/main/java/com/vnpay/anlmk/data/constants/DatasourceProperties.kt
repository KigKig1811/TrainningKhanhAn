package com.vnpay.anlmk.data.constants


object DatasourceProperties {
    fun getUrl(): String {
        return SERVER_URL
    }

    fun getDomain(): String {
        return DOMAIN
    }

    fun getCerts(): List<String> {
        val list = arrayListOf<String>()

        list.add(DatasourceProperties.CERT)


        return list
    }

    const val KEY_DEFAULT = "A37E3544B4E46DBC"
    const val KEY_MAC = "*G-KaPdSgVkYp3s6"
    const val APP_ID = 1111

    const val SERVER_URL = "https://training.vnpay.cf/api/v1/"
    const val DOMAIN = "training.vnpay.cf"

    const val TIMEOUT_CONNECT: Long = 30
    const val TIMEOUT_READ: Long = 90
    const val CERT = "uUcKkGbV95tkrXg+ri0TcWKwZcE8IXXtqr9ena76/Qw="
}