package com.android.main

import android.app.Application
import com.llj.baselib.IOTLib
import com.llj.baselib.bean.UserConfigBean

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val bean = UserConfigBean(
            userId = "19362",
            appKey = "62a26e576c",
            deviceId = "25985",
            clientId = "1202",
            clientSecret = "4b2d4989fb"
        )
        IOTLib.init(applicationContext,bean)
    }
}