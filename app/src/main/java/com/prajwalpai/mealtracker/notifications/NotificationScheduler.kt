package com.prajwalpai.mealtracker.notifications

import android.content.Context
import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Scheduler for meal reminder notifications
 * Uses WorkManager to schedule daily reminders
 */
class NotificationScheduler(private val context: Context) {

    companion object {
        // Default meal times (24-hour format)
        const val DEFAULT_BREAKFAST_HOUR = 8
        const val DEFAULT_BREAKFAST_MINUTE = 0

        const val DEFAULT_LUNCH_HOUR = 13
        const val DEFAULT_LUNCH_MINUTE = 0

        const val DEFAULT_EVENING_SNACKS_HOUR = 17
        const val DEFAULT_EVENING_SNACKS_MINUTE = 0

        const val DEFAULT_DINNER_HOUR = 20
        const val DEFAULT_DINNER_MINUTE = 0

        // Work tags
        private const val TAG_BREAKFAST = "breakfast_reminder"
        private const val TAG_LUNCH = "lunch_reminder"
        private const val TAG_EVENING_SNACKS = "evening_snacks_reminder"
        private const val TAG_DINNER = "dinner_reminder"
    }

    /**
     * Schedule all meal reminders with default times
     */
    fun scheduleAllReminders() {
        scheduleBreakfastReminder(DEFAULT_BREAKFAST_HOUR, DEFAULT_BREAKFAST_MINUTE)
        scheduleLunchReminder(DEFAULT_LUNCH_HOUR, DEFAULT_LUNCH_MINUTE)
        scheduleEveningSnacksReminder(DEFAULT_EVENING_SNACKS_HOUR, DEFAULT_EVENING_SNACKS_MINUTE)
        scheduleDinnerReminder(DEFAULT_DINNER_HOUR, DEFAULT_DINNER_MINUTE)
    }

    /**
     * Schedule breakfast reminder
     */
    fun scheduleBreakfastReminder(hour: Int, minute: Int) {
        scheduleMealReminder(
            "Breakfast",
            NotificationHelper.NOTIFICATION_ID_BREAKFAST,
            hour,
            minute,
            TAG_BREAKFAST
        )
    }

    /**
     * Schedule lunch reminder
     */
    fun scheduleLunchReminder(hour: Int, minute: Int) {
        scheduleMealReminder(
            "Lunch",
            NotificationHelper.NOTIFICATION_ID_LUNCH,
            hour,
            minute,
            TAG_LUNCH
        )
    }

    /**
     * Schedule evening snacks reminder
     */
    fun scheduleEveningSnacksReminder(hour: Int, minute: Int) {
        scheduleMealReminder(
            "Evening Snacks",
            NotificationHelper.NOTIFICATION_ID_EVENING_SNACKS,
            hour,
            minute,
            TAG_EVENING_SNACKS
        )
    }

    /**
     * Schedule dinner reminder
     */
    fun scheduleDinnerReminder(hour: Int, minute: Int) {
        scheduleMealReminder(
            "Dinner",
            NotificationHelper.NOTIFICATION_ID_DINNER,
            hour,
            minute,
            TAG_DINNER
        )
    }

    /**
     * Generic method to schedule a meal reminder
     */
    private fun scheduleMealReminder(
        mealType: String,
        notificationId: Int,
        hour: Int,
        minute: Int,
        tag: String
    ) {
        // Calculate initial delay
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // If target time has passed today, schedule for tomorrow
        if (targetTime.before(currentTime)) {
            targetTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

        // Create input data
        val inputData = workDataOf(
            "MEAL_TYPE" to mealType,
            "NOTIFICATION_ID" to notificationId
        )

        // Create periodic work request (repeats daily)
        val workRequest = PeriodicWorkRequestBuilder<MealReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(tag)
            .build()

        // Enqueue the work
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    /**
     * Cancel all meal reminders
     */
    fun cancelAllReminders() {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_BREAKFAST)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_LUNCH)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_EVENING_SNACKS)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_DINNER)
    }
}

