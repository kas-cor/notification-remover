package com.example.notificationremover.util

/**
 * Utility class for validation operations
 */
object ValidationUtils {
    
    private const val MIN_REMOVAL_TIME = 1
    private const val MAX_REMOVAL_TIME = 1440 // 24 hours
    
    /**
     * Validates removal time in minutes
     */
    fun isValidRemovalTime(minutes: Int): Boolean {
        return minutes in MIN_REMOVAL_TIME..MAX_REMOVAL_TIME
    }
    
    /**
     * Validates package name
     */
    fun isValidPackageName(packageName: String): Boolean {
        return packageName.isNotBlank() && packageName.trim() == packageName
    }
    
    /**
     * Sanitizes app name by removing extra whitespace
     */
    fun sanitizeAppName(appName: String): String {
        return appName.trim().replace(Regex("\\s+"), " ")
    }
    
    /**
     * Validates if a string is a valid app name
     */
    fun isValidAppName(appName: String): Boolean {
        return sanitizeAppName(appName).isNotEmpty()
    }
}