package com.example.notificationremover.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing app-specific notification removal settings
 */
@Entity(tableName = "app_settings")
data class AppSettingEntity(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val removalTimeMinutes: Int,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Validates the entity data
     */
    fun isValid(): Boolean {
        return packageName.isNotEmpty() && 
               appName.isNotEmpty() && 
               removalTimeMinutes > 0 && 
               removalTimeMinutes <= 1440 // Max 24 hours
    }
    
    /**
     * Returns a copy with updated timestamp
     */
    fun withUpdatedTimestamp(): AppSettingEntity {
        return copy(updatedAt = System.currentTimeMillis())
    }
}
