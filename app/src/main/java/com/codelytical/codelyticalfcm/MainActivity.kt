package com.codelytical.codelyticalfcm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.codelytical.lyticalfcm.CustomLogger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askNotificationPermission()

        // Display logs
        val logs = CustomLogger.getLogs(this)
        Log.d("TAG", "Logs: $logs")

        val intent = intent
        val title = intent.getStringExtra("title")
        val shortDesc = intent.getStringExtra("short_desc")

        Log.d("TAG", "NotificationData Title: $title")
        Log.d("TAG", "NotificationData Short Desc: $shortDesc")

        if (title == "Daily Hymn") {
            // Open SecondActivity
            Toast.makeText(this, "Title is $title", Toast.LENGTH_LONG).show()
        } else {
            Log.d("TAG", "NotificationData is null")
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Can post notifications.
        } else {
            // Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
