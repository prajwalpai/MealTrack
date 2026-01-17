package com.prajwalpai.mealtracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.prajwalpai.mealtracker.DailyDashboardActivity
import com.prajwalpai.mealtracker.R

/**
 * Helper class to manage notifications
 * Handles notification channels, creation, and display
 */
class NotificationHelper(private val context: Context) {

    companion object {
        // Notification Channels
        const val CHANNEL_MEAL_REMINDERS = "meal_reminders"
        const val CHANNEL_ACHIEVEMENTS = "achievements"
        const val CHANNEL_SUMMARIES = "summaries"

        // Notification IDs
        const val NOTIFICATION_ID_BREAKFAST = 1001
        const val NOTIFICATION_ID_LUNCH = 1002
        const val NOTIFICATION_ID_EVENING_SNACKS = 1003
        const val NOTIFICATION_ID_DINNER = 1004
        const val NOTIFICATION_ID_ACHIEVEMENT = 2001
        const val NOTIFICATION_ID_WEEKLY_SUMMARY = 3001
    }

    init {
        createNotificationChannels()
    }

    /**
     * Create notification channels for Android O and above
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Meal Reminders Channel
            val mealRemindersChannel = NotificationChannel(
                CHANNEL_MEAL_REMINDERS,
                "Meal Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders to log your meals"
                enableVibration(true)
            }

            // Achievements Channel
            val achievementsChannel = NotificationChannel(
                CHANNEL_ACHIEVEMENTS,
                "Achievements",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for achievements and milestones"
                enableVibration(true)
            }

            // Summaries Channel
            val summariesChannel = NotificationChannel(
                CHANNEL_SUMMARIES,
                "Weekly Summaries",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Weekly meal tracking summaries"
            }

            notificationManager.createNotificationChannel(mealRemindersChannel)
            notificationManager.createNotificationChannel(achievementsChannel)
            notificationManager.createNotificationChannel(summariesChannel)
        }
    }

    /**
     * Show a meal reminder notification
     */
    fun showMealReminder(mealType: String, notificationId: Int) {
        val intent = Intent(context, DailyDashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val (icon, title, message) = getMealReminderContent(mealType)

        val notification = NotificationCompat.Builder(context, CHANNEL_MEAL_REMINDERS)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    /**
     * Show an achievement notification
     */
    fun showAchievementNotification(title: String, message: String) {
        val intent = Intent(context, DailyDashboardActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_ACHIEVEMENT,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ACHIEVEMENTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_ACHIEVEMENT, notification)
    }

    /**
     * Show weekly summary notification
     */
    fun showWeeklySummary(healthyCount: Int, totalMeals: Int) {
        val intent = Intent(context, DailyDashboardActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WEEKLY_SUMMARY,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val healthyPercent = if (totalMeals > 0) (healthyCount * 100) / totalMeals else 0
        val message = "You logged $totalMeals meals this week! $healthyPercent% were healthy 🎉"

        val notification = NotificationCompat.Builder(context, CHANNEL_SUMMARIES)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("📊 Weekly Summary")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WEEKLY_SUMMARY, notification)
    }

    /**
     * Get meal reminder content based on meal type
     */
    private fun getMealReminderContent(mealType: String): Triple<Int, String, String> {
        return when (mealType) {
            "Breakfast" -> Triple(
                R.drawable.ic_launcher_foreground,
                "🌅 Time for Breakfast!",
                "Don't forget to log your breakfast meal"
            )
            "Lunch" -> Triple(
                R.drawable.ic_launcher_foreground,
                "☀️ Time for Lunch!",
                "Remember to track your lunch"
            )
            "Evening Snacks" -> Triple(
                R.drawable.ic_launcher_foreground,
                "☕ Time for Evening Snacks!",
                "Log your evening snack to stay on track"
            )
            "Dinner" -> Triple(
                R.drawable.ic_launcher_foreground,
                "🌙 Time for Dinner!",
                "Don't forget to log your dinner"
            )
            else -> Triple(
                R.drawable.ic_launcher_foreground,
                "🍽️ Meal Reminder",
                "Time to log your meal!"
            )
        }
    }

    /**
     * Cancel a specific notification
     */
    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }

    /**
     * Cancel all notifications
     */
    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }
}

