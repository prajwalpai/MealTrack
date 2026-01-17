package com.example.mealtracker.backup

import android.content.Context
import android.net.Uri
import com.example.mealtracker.Meal
import com.example.mealtracker.MealDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Manages backup and restore operations for the app
 */
class BackupManager(
    private val context: Context,
    private val mealDao: MealDao
) {
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    /**
     * Create a backup of all data
     * @return BackupData object containing all app data
     */
    suspend fun createBackup(): BackupData = withContext(Dispatchers.IO) {
        val meals = mealDao.getAllMealsSuspend()
        
        BackupData(
            version = 1,
            timestamp = System.currentTimeMillis(),
            meals = meals,
            appVersion = "1.0"
        )
    }

    /**
     * Export backup to a URI (file)
     * @param uri The URI to write the backup to
     * @param backupData The backup data to export
     */
    suspend fun exportBackup(uri: Uri, backupData: BackupData): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    val json = gson.toJson(backupData)
                    writer.write(json)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Import backup from a URI (file)
     * @param uri The URI to read the backup from
     * @return BackupData object if successful
     */
    suspend fun importBackup(uri: Uri): Result<BackupData> = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    val json = reader.readText()
                    val backupData = gson.fromJson(json, BackupData::class.java)
                    Result.success(backupData)
                }
            } ?: Result.failure(Exception("Failed to open input stream"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Restore data from backup
     * @param backupData The backup data to restore
     * @param clearExisting Whether to clear existing data before restoring
     */
    suspend fun restoreBackup(backupData: BackupData, clearExisting: Boolean = false): Result<Int> = withContext(Dispatchers.IO) {
        try {
            if (clearExisting) {
                mealDao.deleteAll()
            }

            // Insert all meals
            var count = 0
            backupData.meals.forEach { meal ->
                // Create new meal without ID to avoid conflicts
                val newMeal = meal.copy(id = 0)
                mealDao.insert(newMeal)
                count++
            }

            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generate a default backup filename
     */
    fun generateBackupFileName(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())
        return "MealTracker_Backup_$timestamp.json"
    }

    /**
     * Validate a backup file
     * @param backupData The backup data to validate
     * @return true if valid, false otherwise
     */
    fun validateBackup(backupData: BackupData): Boolean {
        return try {
            backupData.version > 0 &&
            backupData.timestamp > 0 &&
            backupData.meals.all { meal ->
                meal.name.isNotBlank() &&
                meal.type.isNotBlank() &&
                meal.category.isNotBlank() &&
                meal.date.isNotBlank()
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get backup statistics
     */
    fun getBackupStats(backupData: BackupData): BackupStats {
        val healthyCount = backupData.meals.count { it.category == "Healthy" }
        val neutralCount = backupData.meals.count { it.category == "Neutral" }
        val junkCount = backupData.meals.count { it.category == "Junk" }
        
        val uniqueDates = backupData.meals.map { it.date }.distinct().size

        return BackupStats(
            totalMeals = backupData.meals.size,
            healthyMeals = healthyCount,
            neutralMeals = neutralCount,
            junkMeals = junkCount,
            daysTracked = uniqueDates,
            backupDate = Date(backupData.timestamp)
        )
    }
}

/**
 * Statistics about a backup
 */
data class BackupStats(
    val totalMeals: Int,
    val healthyMeals: Int,
    val neutralMeals: Int,
    val junkMeals: Int,
    val daysTracked: Int,
    val backupDate: Date
)

