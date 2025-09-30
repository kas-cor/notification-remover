package com.example.notificationremover.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings WHERE isEnabled = 1 ORDER BY appName ASC")
    fun getAllEnabledSettings(): LiveData<List<AppSettingEntity>>
    
    @Query("SELECT * FROM app_settings ORDER BY appName ASC")
    fun getAllSettings(): LiveData<List<AppSettingEntity>>
    
    @Query("SELECT * FROM app_settings WHERE packageName = :packageName LIMIT 1")
    suspend fun getSettingByPackage(packageName: String): AppSettingEntity?
    
    @Query("SELECT * FROM app_settings WHERE packageName = :packageName AND isEnabled = 1 LIMIT 1")
    suspend fun getEnabledSettingByPackage(packageName: String): AppSettingEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSetting(setting: AppSettingEntity)
    
    @Update
    suspend fun updateSetting(setting: AppSettingEntity)
    
    @Query("UPDATE app_settings SET isEnabled = :enabled, updatedAt = :timestamp WHERE packageName = :packageName")
    suspend fun toggleSetting(packageName: String, enabled: Boolean, timestamp: Long = System.currentTimeMillis())
    
    @Delete
    suspend fun deleteSetting(setting: AppSettingEntity)
    
    @Query("DELETE FROM app_settings WHERE packageName = :packageName")
    suspend fun deleteSettingByPackage(packageName: String)
    
    @Query("SELECT COUNT(*) FROM app_settings")
    suspend fun getSettingsCount(): Int
}
