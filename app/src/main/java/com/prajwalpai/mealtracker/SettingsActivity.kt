package com.prajwalpai.mealtracker

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.prajwalpai.mealtracker.notifications.NotificationScheduler
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import java.text.SimpleDateFormat
import java.util.*

/**
 * Settings Activity for notification preferences
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var notificationsSwitch: SwitchMaterial
    private lateinit var breakfastTimeText: TextView
    private lateinit var lunchTimeText: TextView
    private lateinit var eveningSnacksTimeText: TextView
    private lateinit var dinnerTimeText: TextView
    private lateinit var backupRestoreButton: LinearLayout
    private lateinit var backButton: MaterialButton

    private lateinit var notificationScheduler: NotificationScheduler
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    // Notification permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications enabled! ✅", Toast.LENGTH_SHORT).show()
            scheduleNotifications()
        } else {
            Toast.makeText(this, "Notifications permission denied", Toast.LENGTH_SHORT).show()
            notificationsSwitch.isChecked = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        notificationScheduler = NotificationScheduler(this)

        initializeViews()
        setupListeners()
        loadSavedTimes()
    }

    private fun initializeViews() {
        notificationsSwitch = findViewById(R.id.notificationsSwitch)
        breakfastTimeText = findViewById(R.id.breakfastTimeText)
        lunchTimeText = findViewById(R.id.lunchTimeText)
        eveningSnacksTimeText = findViewById(R.id.eveningSnacksTimeText)
        dinnerTimeText = findViewById(R.id.dinnerTimeText)
        backupRestoreButton = findViewById(R.id.backupRestoreButton)
        backButton = findViewById(R.id.backButton)
    }

    private fun setupListeners() {
        // Notifications switch
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkNotificationPermission()
            } else {
                notificationScheduler.cancelAllReminders()
                Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Time pickers
        breakfastTimeText.setOnClickListener {
            showTimePicker("Breakfast", 8, 0) { hour, minute ->
                updateTime(breakfastTimeText, hour, minute)
                notificationScheduler.scheduleBreakfastReminder(hour, minute)
            }
        }

        lunchTimeText.setOnClickListener {
            showTimePicker("Lunch", 13, 0) { hour, minute ->
                updateTime(lunchTimeText, hour, minute)
                notificationScheduler.scheduleLunchReminder(hour, minute)
            }
        }

        eveningSnacksTimeText.setOnClickListener {
            showTimePicker("Evening Snacks", 17, 0) { hour, minute ->
                updateTime(eveningSnacksTimeText, hour, minute)
                notificationScheduler.scheduleEveningSnacksReminder(hour, minute)
            }
        }

        dinnerTimeText.setOnClickListener {
            showTimePicker("Dinner", 20, 0) { hour, minute ->
                updateTime(dinnerTimeText, hour, minute)
                notificationScheduler.scheduleDinnerReminder(hour, minute)
            }
        }

        // Backup & Restore button
        backupRestoreButton.setOnClickListener {
            val intent = Intent(this, BackupRestoreActivity::class.java)
            startActivity(intent)
        }

        // Back button
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    scheduleNotifications()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            scheduleNotifications()
        }
    }

    private fun scheduleNotifications() {
        notificationScheduler.scheduleAllReminders()
        Toast.makeText(this, "Meal reminders scheduled! 🔔", Toast.LENGTH_SHORT).show()
    }

    private fun showTimePicker(
        mealType: String,
        defaultHour: Int,
        defaultMinute: Int,
        onTimeSet: (Int, Int) -> Unit
    ) {
        TimePickerDialog(
            this,
            { _, hour, minute ->
                onTimeSet(hour, minute)
                Toast.makeText(this, "$mealType reminder updated!", Toast.LENGTH_SHORT).show()
            },
            defaultHour,
            defaultMinute,
            false
        ).show()
    }

    private fun updateTime(textView: TextView, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        textView.text = timeFormat.format(calendar.time)
    }

    private fun loadSavedTimes() {
        // Load default times for now
        // In a real app, you'd load these from SharedPreferences
    }
}

