package com.example.mealtracker.backup

import com.example.mealtracker.Meal

/**
 * Data class representing a complete backup of all app data
 */
data class BackupData(
    val version: Int = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val meals: List<Meal> = emptyList(),
    val appVersion: String = "1.0"
)

/**
 * Metadata about a backup file
 */
data class BackupMetadata(
    val fileName: String,
    val timestamp: Long,
    val mealCount: Int,
    val fileSize: Long
)

