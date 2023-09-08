package com.codelytical.lyticalfcm

object NotificationDataHolder {
    private var notificationData: Map<String, String>? = null

    fun setNotificationData(data: Map<String, String>?) {
        notificationData = data
    }

    fun getNotificationData(): Map<String, String>? {
        return notificationData
    }

    fun clearNotificationData() {
        notificationData = null
    }
}
