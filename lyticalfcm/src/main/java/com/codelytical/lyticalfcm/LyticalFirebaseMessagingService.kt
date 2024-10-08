package com.codelytical.lyticalfcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicInteger

class LyticalFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            val icon = remoteMessage.data["icon"]
            val title = remoteMessage.data["title"]
            val shortDesc = remoteMessage.data["short_desc"]
            val longDesc = remoteMessage.data["long_desc"]
            val image = remoteMessage.data["feature"]
            val packageName = remoteMessage.data["package"]

            // Log the received data
            CustomLogger.log(this, "Received Notification - Icon: $icon, Title: $title, Short Desc: $shortDesc")

            if (icon == null || title == null || shortDesc == null) {
                return
            } else {
                val notificationData = mapOf(
                    "icon" to icon,
                    "title" to title,
                    "short_desc" to shortDesc,
                    "long_desc" to (longDesc ?: ""),
                    "feature" to (image ?: ""),
                    "package" to (packageName ?: ""),
                )

                CustomLogger.log(this, "Notification Data: $notificationData")

                NotificationDataHolder.setNotificationData(notificationData)

                Handler(this.mainLooper).post {
                    sendNotification(icon, title, shortDesc, image, longDesc, packageName)
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CustomLogger.log(this, "Refreshed token: $token")
    }

    private fun sendNotification(
        icon: String,
        title: String,
        shortDesc: String,
        image: String?,
        longDesc: String?,
        storePackage: String?,
    ) {
        val targetActivity = NotificationDataHolder.getTargetActivity() ?: return

        CustomLogger.log(this, "Sending Notification - Icon: $icon, Title: $title, Short Desc: $shortDesc, Activity: $targetActivity")

        val mIntent = Intent(this, targetActivity).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("title", title)
            putExtra("short_desc", shortDesc)
            putExtra("long_desc", longDesc)
            putExtra("feature", image)
            putExtra("icon", icon)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                mIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT,
            )
        } else {
            PendingIntent.getActivity(
                this,
                0,
                mIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        // Make Remote Views For text
        val remoteViews = RemoteViews(packageName, R.layout.firebase_notification_view)
        remoteViews.setTextViewText(R.id.tv_title, title)
        remoteViews.setTextViewText(R.id.tv_short_desc, shortDesc)

        // Notification Parameters
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationID = getNextInt()
        notificationManager.notify(notificationID, notificationBuilder.build())

        try {
            Picasso.get().load(icon)
                .into(remoteViews, R.id.iv_icon, notificationID, notificationBuilder.build())
            if (image != null) {
                remoteViews.setViewVisibility(R.id.iv_feature, View.VISIBLE)
                Picasso.get().load(image)
                    .into(remoteViews, R.id.iv_feature, notificationID, notificationBuilder.build())
            }
        } catch (e: Exception) {
            CustomLogger.log(this, "Exception while loading images: ${e.message}")
        } catch (_: java.lang.Exception) {
        } catch (_: IllegalStateException) {
        } catch (_: IllegalArgumentException) {
        }
    }

    private fun openApp(storePackage: String): Intent {
        return try {
            packageManager.getLaunchIntentForPackage(storePackage) ?: setStoreIntent(storePackage)
        } catch (e: Exception) {
            setStoreIntent(storePackage)
        }
    }

    private fun setStoreIntent(storePackage: String): Intent {
        return try {
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$storePackage"))
        } catch (e: ActivityNotFoundException) {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$storePackage"),
            )
        }
    }

    private fun isCrossPromotionPackage(mPackage: String): Boolean {
        return packageName != mPackage
    }

    companion object {
        private const val TAG = "LyticalFirebase"
        private val number = AtomicInteger()
        fun getNextInt(): Int {
            return number.incrementAndGet()
        }
    }
}

