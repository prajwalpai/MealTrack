package com.example.mealtracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mealtracker.backup.BackupManager
import com.example.mealtracker.widget.WidgetUpdateHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BackupRestoreActivity : AppCompatActivity() {

    private lateinit var backupManager: BackupManager
    private lateinit var createBackupButton: MaterialButton
    private lateinit var restoreBackupButton: MaterialButton
    private lateinit var backButton: MaterialButton

    // Activity result launchers
    private val createBackupLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { performBackup(it) }
    }

    private val restoreBackupLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { performRestore(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup_restore)

        // Initialize backup manager
        val database = MealDatabase.getDatabase(this)
        backupManager = BackupManager(this, database.mealDao())

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        createBackupButton = findViewById(R.id.createBackupButton)
        restoreBackupButton = findViewById(R.id.restoreBackupButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupListeners() {
        createBackupButton.setOnClickListener {
            val fileName = backupManager.generateBackupFileName()
            createBackupLauncher.launch(fileName)
        }

        restoreBackupButton.setOnClickListener {
            showRestoreWarningDialog()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun performBackup(uri: android.net.Uri) {
        lifecycleScope.launch {
            try {
                // Show loading
                createBackupButton.isEnabled = false
                createBackupButton.text = "Creating backup..."

                // Create backup
                val backupData = backupManager.createBackup()
                
                // Export to file
                val result = backupManager.exportBackup(uri, backupData)
                
                if (result.isSuccess) {
                    val stats = backupManager.getBackupStats(backupData)
                    showBackupSuccessDialog(stats)
                } else {
                    Toast.makeText(
                        this@BackupRestoreActivity,
                        "Backup failed: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@BackupRestoreActivity,
                    "Error creating backup: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                createBackupButton.isEnabled = true
                createBackupButton.text = "📤 Create Backup"
            }
        }
    }

    private fun performRestore(uri: android.net.Uri) {
        lifecycleScope.launch {
            try {
                // Show loading
                restoreBackupButton.isEnabled = false
                restoreBackupButton.text = "Restoring..."

                // Import backup
                val importResult = backupManager.importBackup(uri)
                
                if (importResult.isFailure) {
                    Toast.makeText(
                        this@BackupRestoreActivity,
                        "Failed to read backup file: ${importResult.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                val backupData = importResult.getOrNull()!!
                
                // Validate backup
                if (!backupManager.validateBackup(backupData)) {
                    Toast.makeText(
                        this@BackupRestoreActivity,
                        "Invalid backup file",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                // Show preview and confirm
                showRestoreConfirmDialog(backupData)
                
            } catch (e: Exception) {
                Toast.makeText(
                    this@BackupRestoreActivity,
                    "Error reading backup: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                restoreBackupButton.isEnabled = true
                restoreBackupButton.text = "📥 Restore Backup"
            }
        }
    }

    private fun showRestoreWarningDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("⚠️ Restore Backup")
            .setMessage("Select a backup file to restore your data.\n\nYou can choose to:\n• Merge with existing data\n• Replace all existing data")
            .setPositiveButton("Select File") { _, _ ->
                restoreBackupLauncher.launch(arrayOf("application/json"))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRestoreConfirmDialog(backupData: com.example.mealtracker.backup.BackupData) {
        val stats = backupManager.getBackupStats(backupData)
        val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())

        val message = """
            📊 Backup Information:

            • Total Meals: ${stats.totalMeals}
            • Healthy: ${stats.healthyMeals} 🟢
            • Neutral: ${stats.neutralMeals} ⚪
            • Junk: ${stats.junkMeals} 🔴
            • Days Tracked: ${stats.daysTracked}
            • Backup Date: ${dateFormat.format(stats.backupDate)}

            How would you like to restore?
        """.trimIndent()

        MaterialAlertDialogBuilder(this)
            .setTitle("Restore Backup")
            .setMessage(message)
            .setPositiveButton("Merge") { _, _ ->
                confirmRestore(backupData, clearExisting = false)
            }
            .setNeutralButton("Replace All") { _, _ ->
                confirmRestore(backupData, clearExisting = true)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmRestore(backupData: com.example.mealtracker.backup.BackupData, clearExisting: Boolean) {
        val action = if (clearExisting) "replace all existing data" else "merge with existing data"

        MaterialAlertDialogBuilder(this)
            .setTitle("⚠️ Confirm Restore")
            .setMessage("Are you sure you want to $action?\n\n${if (clearExisting) "This will DELETE all your current meals!" else "This will add ${backupData.meals.size} meals to your existing data."}")
            .setPositiveButton("Yes, Restore") { _, _ ->
                executeRestore(backupData, clearExisting)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun executeRestore(backupData: com.example.mealtracker.backup.BackupData, clearExisting: Boolean) {
        lifecycleScope.launch {
            try {
                restoreBackupButton.isEnabled = false
                restoreBackupButton.text = "Restoring..."

                val result = backupManager.restoreBackup(backupData, clearExisting)

                if (result.isSuccess) {
                    val count = result.getOrNull() ?: 0
                    showRestoreSuccessDialog(count, clearExisting)
                } else {
                    Toast.makeText(
                        this@BackupRestoreActivity,
                        "Restore failed: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@BackupRestoreActivity,
                    "Error restoring backup: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                restoreBackupButton.isEnabled = true
                restoreBackupButton.text = "📥 Restore Backup"
            }
        }
    }

    private fun showBackupSuccessDialog(stats: com.example.mealtracker.backup.BackupStats) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())

        val message = """
            ✅ Backup created successfully!

            📊 Backup Contents:
            • Total Meals: ${stats.totalMeals}
            • Healthy: ${stats.healthyMeals} 🟢
            • Neutral: ${stats.neutralMeals} ⚪
            • Junk: ${stats.junkMeals} 🔴
            • Days Tracked: ${stats.daysTracked}

            Your data is now safely backed up!
        """.trimIndent()

        MaterialAlertDialogBuilder(this)
            .setTitle("Backup Complete")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showRestoreSuccessDialog(count: Int, wasReplaced: Boolean) {
        val action = if (wasReplaced) "replaced with" else "added"

        MaterialAlertDialogBuilder(this)
            .setTitle("✅ Restore Complete")
            .setMessage("Successfully $action $count meals!\n\nYour data has been restored.")
            .setPositiveButton("OK") { _, _ ->
                // Refresh widget
                WidgetUpdateHelper.updateWidgets(this)
                finish()
            }
            .show()
    }
}

