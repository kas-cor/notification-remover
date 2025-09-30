package com.example.notificationremover.service

import android.app.Notification
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notificationremover.BuildConfig
import com.example.notificationremover.data.AppDatabase
import com.example.notificationremover.data.AppSettingEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class NotificationListenerService : NotificationListenerService() {
    
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val handler = Handler(Looper.getMainLooper())
    private val pendingRemovals = ConcurrentHashMap<String, Runnable>()
    private lateinit var prefs: SharedPreferences
    private lateinit var database: AppDatabase
    
    companion object {
        private const val TAG = "NotificationListener"
        private const val DEFAULT_REMOVAL_TIME_KEY = "default_removal_time"
        private const val DEFAULT_REMOVAL_TIME = 15 // 15 minutes default
        private const val MAX_PENDING_OPERATIONS = 1000
        private val DEBUG = BuildConfig.DEBUG
    }
    
    private fun logDebug(message: String) {
        if (DEBUG) Log.d(TAG, message)
    }
    
    private fun logInfo(message: String) {
        Log.i(TAG, message)
    }
    
    private fun logError(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
    
    override fun onCreate() {
        super.onCreate()
        try {
            prefs = getSharedPreferences("notification_settings", MODE_PRIVATE)
            database = AppDatabase.getDatabase(this)
            logInfo("Notification listener service created successfully")
        } catch (e: Exception) {
            logError("Failed to initialize notification listener service", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        try {
            // Cancel all pending notification removals
            cleanupAllPendingRemovals()
            job.cancel()
            logInfo("Notification listener service destroyed")
        } catch (e: Exception) {
            logError("Error during service destruction", e)
        }
    }
    
    private fun cleanupAllPendingRemovals() {
        pendingRemovals.values.forEach { handler.removeCallbacks(it) }
        pendingRemovals.clear()
        logDebug("Cleaned up ${pendingRemovals.size} pending operations")
    }
    
    private fun cleanupOldOperations() {
        if (pendingRemovals.size >= MAX_PENDING_OPERATIONS) {
            logDebug("Max pending operations reached, cleaning up...")
            // Remove the oldest 25% of operations
            val toRemove = pendingRemovals.size / 4
            val iterator = pendingRemovals.iterator()
            var removed = 0
            while (iterator.hasNext() && removed < toRemove) {
                val entry = iterator.next()
                handler.removeCallbacks(entry.value)
                iterator.remove()
                removed++
            }
            logDebug("Cleaned up $removed old pending operations")
        }
    }
    
    private fun addPendingRemoval(key: String, runnable: Runnable) {
        cleanupOldOperations()
        pendingRemovals[key] = runnable
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            val packageName = sbn.packageName
            
            // Don't process our own notifications
            if (packageName == applicationContext.packageName) {
                logDebug("Ignoring own notification")
                return
            }
            
            // Don't process ongoing notifications (like music player, navigation, etc.)
            if (sbn.notification.flags and Notification.FLAG_ONGOING_EVENT != 0) {
                logDebug("Ignoring ongoing notification from $packageName")
                return
            }
            
            // Don't process system notifications
            if (isSystemNotification(sbn)) {
                logDebug("Ignoring system notification from $packageName")
                return
            }
            
            val key = sbn.key
            
            // If there's already a pending removal for this notification, cancel it
            pendingRemovals[key]?.let { 
                handler.removeCallbacks(it)
                logDebug("Cancelled previous pending removal for $key")
            }
            
            // Schedule new removal
            scope.launch {
                try {
                    val removalTime = getRemovalTimeForApp(packageName)
                    if (removalTime <= 0) {
                        logDebug("Removal disabled for $packageName")
                        return@launch
                    }
                    
                    val removalRunnable = Runnable {
                        try {
                            logInfo("Removing notification from: $packageName")
                            cancelNotification(key)
                            pendingRemovals.remove(key)
                        } catch (e: SecurityException) {
                            logError("Security exception when removing notification: ${e.message}")
                        } catch (e: Exception) {
                            logError("Error removing notification: ${e.message}", e)
                        }
                    }
                    
                    withContext(Dispatchers.Main) {
                        val delayMs = removalTime * 60 * 1000L
                        handler.postDelayed(removalRunnable, delayMs)
                        addPendingRemoval(key, removalRunnable)
                    }
                    
                    logDebug("Scheduled removal for $packageName notification in $removalTime minutes")
                } catch (e: Exception) {
                    logError("Error scheduling notification removal", e)
                }
            }
        } catch (e: Exception) {
            logError("Error in onNotificationPosted", e)
        }
    }
    
    private fun isSystemNotification(sbn: StatusBarNotification): Boolean {
        // Add logic to identify system notifications that shouldn't be removed
        val systemPackages = setOf(
            "android",
            "com.android.systemui",
            "com.android.providers.downloads"
        )
        return systemPackages.contains(sbn.packageName)
    }
    
    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        try {
            // Clean up any pending removal when notifications are manually dismissed
            val key = sbn.key
            pendingRemovals[key]?.let {
                handler.removeCallbacks(it)
                pendingRemovals.remove(key)
                logDebug("Cleaned up pending removal for manually dismissed notification: ${sbn.packageName}")
            }
        } catch (e: Exception) {
            logError("Error in onNotificationRemoved", e)
        }
    }
    
    private suspend fun getRemovalTimeForApp(packageName: String): Int {
        return try {
            // Try to get enabled app-specific setting from database
            val database = AppDatabase.getDatabase(this)
            val appSetting = database.appSettingsDao().getEnabledSettingByPackage(packageName)
            
            // Return app-specific setting if available and enabled
            if (appSetting != null && appSetting.isEnabled) {
                appSetting.removalTimeMinutes
            } else {
                // Otherwise use default setting from preferences
                prefs.getInt(DEFAULT_REMOVAL_TIME_KEY, DEFAULT_REMOVAL_TIME)
            }
        } catch (e: Exception) {
            logError("Error getting removal time for $packageName", e)
            DEFAULT_REMOVAL_TIME
        }
    }
}
