package com.example.notificationremover.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData

class AppSettingsRepository(private val context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val appSettingsDao = database.appSettingsDao()
    private val prefs: SharedPreferences = context.getSharedPreferences("notification_settings", Context.MODE_PRIVATE)
    
    companion object {
        private const val TAG = "AppSettingsRepository"
        private const val DEFAULT_REMOVAL_TIME_KEY = "default_removal_time"
        private const val DEFAULT_REMOVAL_TIME = 15 // 15 minutes default
        private const val MIN_REMOVAL_TIME = 1
        private const val MAX_REMOVAL_TIME = 1440 // 24 hours
    }
    
    // App-specific settings
    fun getAllAppSettings(): LiveData<List<AppSettingEntity>> {
        return appSettingsDao.getAllSettings()
    }
    
    fun getAllEnabledAppSettings(): LiveData<List<AppSettingEntity>> {
        return appSettingsDao.getAllEnabledSettings()
    }
    
    suspend fun getAppSettingByPackage(packageName: String): AppSettingEntity? {
        return try {
            appSettingsDao.getSettingByPackage(packageName)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting app setting for $packageName", e)
            null
        }
    }
    
    suspend fun getEnabledAppSettingByPackage(packageName: String): AppSettingEntity? {
        return try {
            appSettingsDao.getEnabledSettingByPackage(packageName)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting enabled app setting for $packageName", e)
            null
        }
    }
    
    suspend fun saveAppSetting(appSetting: AppSettingEntity): Boolean {
        return try {
            if (!appSetting.isValid()) {
                Log.w(TAG, "Invalid app setting: $appSetting")
                return false
            }
            
            val updatedSetting = appSetting.withUpdatedTimestamp()
            appSettingsDao.insertOrUpdateSetting(updatedSetting)
            Log.d(TAG, "Saved app setting for ${appSetting.packageName}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving app setting", e)
            false
        }
    }
    
    suspend fun toggleAppSetting(packageName: String, enabled: Boolean): Boolean {
        return try {
            appSettingsDao.toggleSetting(packageName, enabled)
            Log.d(TAG, "Toggled app setting for $packageName: enabled=$enabled")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling app setting for $packageName", e)
            false
        }
    }
    
    suspend fun deleteAppSetting(appSetting: AppSettingEntity): Boolean {
        return try {
            appSettingsDao.deleteSetting(appSetting)
            Log.d(TAG, "Deleted app setting for ${appSetting.packageName}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting app setting", e)
            false
        }
    }
    
    suspend fun getSettingsCount(): Int {
        return try {
            appSettingsDao.getSettingsCount()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting settings count", e)
            0
        }
    }
    
    // Default time settings
    fun getDefaultRemovalTime(): Int {
        return prefs.getInt(DEFAULT_REMOVAL_TIME_KEY, DEFAULT_REMOVAL_TIME)
            .coerceIn(MIN_REMOVAL_TIME, MAX_REMOVAL_TIME)
    }
    
    fun saveDefaultRemovalTime(minutes: Int): Boolean {
        return try {
            val validatedMinutes = minutes.coerceIn(MIN_REMOVAL_TIME, MAX_REMOVAL_TIME)
            prefs.edit().putInt(DEFAULT_REMOVAL_TIME_KEY, validatedMinutes).apply()
            Log.d(TAG, "Saved default removal time: $validatedMinutes minutes")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving default removal time", e)
            false
        }
    }
    
    fun isValidRemovalTime(minutes: Int): Boolean {
        return minutes in MIN_REMOVAL_TIME..MAX_REMOVAL_TIME
    }
}
