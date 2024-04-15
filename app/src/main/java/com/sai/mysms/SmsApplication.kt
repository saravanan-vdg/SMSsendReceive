package com.sai.mysms

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmsApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}