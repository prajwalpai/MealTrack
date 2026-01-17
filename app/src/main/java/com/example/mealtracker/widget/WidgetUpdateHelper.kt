package com.example.mealtracker.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * Helper class to update widgets when meal data changes
 */
object WidgetUpdateHelper {
    
    /**
     * Request update for all Meal Tracker widgets
     * Call this whenever meal data changes (add, edit, delete)
     */
    fun updateWidgets(context: Context) {
        val intent = Intent(context, MealWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val ids = appWidgetManager.getAppWidgetIds(
            ComponentName(context, MealWidgetProvider::class.java)
        )
        
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}

