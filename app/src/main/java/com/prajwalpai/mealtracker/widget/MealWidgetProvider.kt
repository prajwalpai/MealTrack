package com.prajwalpai.mealtracker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.prajwalpai.mealtracker.DailyDashboardActivity
import com.prajwalpai.mealtracker.MainActivity
import com.prajwalpai.mealtracker.MealDatabase
import com.prajwalpai.mealtracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Widget Provider for Meal Tracker
 * Displays meal summary and provides quick add functionality
 */
class MealWidgetProvider : AppWidgetProvider() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update each widget instance
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        // Called when widget is resized
        updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Get widget size
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)

        // Choose layout based on size
        val layoutId = when {
            minWidth >= 250 && minHeight >= 200 -> R.layout.widget_meal_large
            minWidth >= 180 -> R.layout.widget_meal_medium
            else -> R.layout.widget_meal_small
        }

        val views = RemoteViews(context.packageName, layoutId)

        // Set up click handlers
        setupClickHandlers(context, views)

        // Load and display data
        loadWidgetData(context, views, appWidgetManager, appWidgetId, layoutId)
    }

    private fun setupClickHandlers(context: Context, views: RemoteViews) {
        // Click on widget opens Daily Dashboard
        val dashboardIntent = Intent(context, DailyDashboardActivity::class.java)
        val dashboardPendingIntent = PendingIntent.getActivity(
            context, 0, dashboardIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widgetContainer, dashboardPendingIntent)

        // Quick add button opens MainActivity
        val addMealIntent = Intent(context, MainActivity::class.java)
        val addMealPendingIntent = PendingIntent.getActivity(
            context, 1, addMealIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.quickAddButton, addMealPendingIntent)
    }

    private fun loadWidgetData(
        context: Context,
        views: RemoteViews,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        layoutId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val database = MealDatabase.getDatabase(context)
                val mealDao = database.mealDao()
                val today = dateFormat.format(Date())

                // Get today's meals
                val todayMeals = mealDao.getMealsByDateSuspend(today)

                // Calculate category counts
                val healthyCount = todayMeals.count { it.category == "Healthy" }
                val neutralCount = todayMeals.count { it.category == "Neutral" }
                val junkCount = todayMeals.count { it.category == "Junk" }
                val totalCount = todayMeals.size

                // Update UI on main thread
                CoroutineScope(Dispatchers.Main).launch {
                    when (layoutId) {
                        R.layout.widget_meal_small -> {
                            // Small widget - just show quick add button
                            // Button text is already set in XML
                        }
                        R.layout.widget_meal_medium -> {
                            // Medium widget - show today's count and categories
                            views.setTextViewText(R.id.todayMealCount, "$totalCount meals today")
                            views.setTextViewText(R.id.categoryBreakdown, 
                                "🟢 $healthyCount  ⚪ $neutralCount  🔴 $junkCount")
                        }
                        R.layout.widget_meal_large -> {
                            // Large widget - show detailed stats
                            views.setTextViewText(R.id.todayMealCount, "$totalCount meals today")
                            views.setTextViewText(R.id.categoryBreakdown,
                                "🟢 $healthyCount  ⚪ $neutralCount  🔴 $junkCount")
                            
                            // Calculate weekly stats
                            val calendar = Calendar.getInstance()
                            calendar.firstDayOfWeek = Calendar.MONDAY
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                            val weekStart = dateFormat.format(calendar.time)
                            calendar.add(Calendar.DAY_OF_WEEK, 6)
                            val weekEnd = dateFormat.format(calendar.time)
                            
                            val weekMeals = mealDao.getMealsByDateRangeSuspend(weekStart, weekEnd)
                            views.setTextViewText(R.id.weeklyStats, "${weekMeals.size} meals this week")
                        }
                    }

                    // Update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

