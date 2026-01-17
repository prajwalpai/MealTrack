package com.example.mealtracker.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mealtracker.MealDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * WorkManager worker to send meal reminders
 * Checks if meal has been logged before sending notification
 */
class MealReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val notificationHelper = NotificationHelper(context)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Get meal type from input data
            val mealType = inputData.getString("MEAL_TYPE") ?: return@withContext Result.failure()
            val notificationId = inputData.getInt("NOTIFICATION_ID", 0)

            // Check if meal has already been logged today
            val today = dateFormat.format(Date())
            val database = MealDatabase.getDatabase(applicationContext)
            val mealDao = database.mealDao()

            // Get meals for today of this type
            val mealsToday = mealDao.getMealsByDateAndTypeSuspend(today, mealType)

            // Only send notification if meal hasn't been logged
            if (mealsToday.isEmpty()) {
                notificationHelper.showMealReminder(mealType, notificationId)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}

