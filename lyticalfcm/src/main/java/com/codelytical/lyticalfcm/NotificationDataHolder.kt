package com.codelytical.lyticalfcm

import android.app.Activity
import android.util.Log

object NotificationDataHolder {
    private var notificationData: Map<String, String>? = null
    private var targetActivity: Class<out Activity>? = null

    fun setNotificationData(data: Map<String, String>?) {
        Log.d("TAG", "Setting NotificationData: $data")
        notificationData = data
    }

    fun getNotificationData(): Map<String, String>? {
        return notificationData
    }

    fun clearNotificationData() {
        notificationData = null
    }

    fun setTargetActivity(activity: Class<out Activity>) {
        targetActivity = activity
    }

    fun getTargetActivity(): Class<out Activity>? {
        return targetActivity
    }
}

