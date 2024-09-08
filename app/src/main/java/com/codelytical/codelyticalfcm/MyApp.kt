package com.codelytical.codelyticalfcm

import android.app.Application
import android.util.Log
import com.codelytical.lyticalfcm.LyticalFCM

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("TAG", "NotificationData: Setup successful")
        LyticalFCM.setupFCM(this, packageName, MainActivity::class.java)
    }
}
