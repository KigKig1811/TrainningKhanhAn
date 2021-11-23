package com.vnpay.anlmk.main

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.vnpay.supersecure.Storage
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : MultiDexApplication() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Storage.newInstance(this@MainApplication)
        startKoin {
            androidContext(this@MainApplication)
            modules(com.vnpay.anlmk.di.modules)

        }
        FirebaseApp.initializeApp(this)

    }

    companion object {
        private var instance: MainApplication? = null

        fun getInstanceApp(): MainApplication {
            return instance as MainApplication
        }
    }
}
