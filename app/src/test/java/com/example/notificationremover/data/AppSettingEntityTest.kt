package com.example.notificationremover.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppSettingEntityTest {

    @Test
    fun `isValid returns true for valid entity`() {
        val entity = AppSettingEntity(
            packageName = "com.example.app",
            appName = "Test App",
            removalTimeMinutes = 15
        )
        
        assertTrue(entity.isValid())
    }

    @Test
    fun `isValid returns false for empty package name`() {
        val entity = AppSettingEntity(
            packageName = "",
            appName = "Test App",
            removalTimeMinutes = 15
        )
        
        assertFalse(entity.isValid())
    }

    @Test
    fun `isValid returns false for empty app name`() {
        val entity = AppSettingEntity(
            packageName = "com.example.app",
            appName = "",
            removalTimeMinutes = 15
        )
        
        assertFalse(entity.isValid())
    }

    @Test
    fun `isValid returns false for zero removal time`() {
        val entity = AppSettingEntity(
            packageName = "com.example.app",
            appName = "Test App",
            removalTimeMinutes = 0
        )
        
        assertFalse(entity.isValid())
    }

    @Test
    fun `isValid returns false for negative removal time`() {
        val entity = AppSettingEntity(
            packageName = "com.example.app",
            appName = "Test App",
            removalTimeMinutes = -5
        )
        
        assertFalse(entity.isValid())
    }

    @Test
    fun `isValid returns false for removal time exceeding 24 hours`() {
        val entity = AppSettingEntity(
            packageName = "com.example.app",
            appName = "Test App",
            removalTimeMinutes = 1500 // More than 1440 minutes (24 hours)
        )
        
        assertFalse(entity.isValid())
    }

    @Test
    fun `withUpdatedTimestamp updates timestamp`() {
        val entity = AppSettingEntity(
            packageName = "com.example.app",
            appName = "Test App",
            removalTimeMinutes = 15,
            updatedAt = 1000L
        )
        
        val updated = entity.withUpdatedTimestamp()
        
        assertTrue(updated.updatedAt > entity.updatedAt)
        assertEquals(entity.packageName, updated.packageName)
        assertEquals(entity.appName, updated.appName)
        assertEquals(entity.removalTimeMinutes, updated.removalTimeMinutes)
    }
}