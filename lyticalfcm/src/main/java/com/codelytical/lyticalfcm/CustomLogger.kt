package com.codelytical.lyticalfcm

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object CustomLogger {
    private const val LOG_FILE_NAME = "app_log.txt"

    fun log(context: Context, message: String) {
        val logFile = File(context.filesDir, LOG_FILE_NAME)
        try {
            FileWriter(logFile, true).use { writer ->
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                writer.append("$timestamp: $message\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getLogs(context: Context): String {
        val logFile = File(context.filesDir, LOG_FILE_NAME)
        return if (logFile.exists()) {
            logFile.readText()
        } else {
            "No logs available."
        }
    }
}
