package com.example.notificationremover.util

import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun `isValidRemovalTime returns true for valid times`() {
        assertTrue(ValidationUtils.isValidRemovalTime(1))
        assertTrue(ValidationUtils.isValidRemovalTime(15))
        assertTrue(ValidationUtils.isValidRemovalTime(60))
        assertTrue(ValidationUtils.isValidRemovalTime(1440))
    }

    @Test
    fun `isValidRemovalTime returns false for invalid times`() {
        assertFalse(ValidationUtils.isValidRemovalTime(0))
        assertFalse(ValidationUtils.isValidRemovalTime(-1))
        assertFalse(ValidationUtils.isValidRemovalTime(1441))
        assertFalse(ValidationUtils.isValidRemovalTime(Int.MAX_VALUE))
    }

    @Test
    fun `isValidPackageName returns true for valid package names`() {
        assertTrue(ValidationUtils.isValidPackageName("com.example.app"))
        assertTrue(ValidationUtils.isValidPackageName("com.google.android.gms"))
        assertTrue(ValidationUtils.isValidPackageName("app"))
    }

    @Test
    fun `isValidPackageName returns false for invalid package names`() {
        assertFalse(ValidationUtils.isValidPackageName(""))
        assertFalse(ValidationUtils.isValidPackageName(" "))
        assertFalse(ValidationUtils.isValidPackageName("  com.example.app  "))
    }

    @Test
    fun `sanitizeAppName removes extra spaces and returns clean name`() {
        assertEquals("Test App", ValidationUtils.sanitizeAppName("  Test App  "))
        assertEquals("Test App", ValidationUtils.sanitizeAppName("Test   App"))
        assertEquals("Test App", ValidationUtils.sanitizeAppName("\nTest\tApp\r"))
    }

    @Test
    fun `sanitizeAppName returns empty string for invalid input`() {
        assertEquals("", ValidationUtils.sanitizeAppName(""))
        assertEquals("", ValidationUtils.sanitizeAppName("   "))
        assertEquals("", ValidationUtils.sanitizeAppName("\n\t\r"))
    }
}