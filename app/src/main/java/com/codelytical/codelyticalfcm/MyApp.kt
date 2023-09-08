package com.codelytical.codelyticalfcm

import android.app.Application
import com.codelytical.lyticalfcm.LyticalFCM

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        LyticalFCM.setupFCM(this, packageName)
    }
}
