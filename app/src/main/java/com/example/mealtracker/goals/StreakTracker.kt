package com.example.mealtracker.goals

import android.content.Context
import com.example.mealtracker.MealDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tracker for calculating and updating streaks
 */
class StreakTracker(context: Context) {

    private val database = MealDatabase.getDatabase(context)
    private val streakDao = database.streakDao()
    private val mealDao = database.mealDao()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * Initialize default streaks if they don't exist
     */
    suspend fun initializeStreaks() = withContext(Dispatchers.IO) {
        val streakTypes = listOf(
            StreakType.HEALTHY_MEALS,
            StreakType.ALL_MEALS_LOGGED,
            StreakType.NO_JUNK,
            StreakType.PERFECT_DAY
        )

        val today = dateFormat.format(Date())

        for (type in streakTypes) {
            val existing = streakDao.getStreakByType(type)
            if (existing == null) {
                streakDao.insert(
                    Streak(
                        type = type,
                        currentStreak = 0,
                        longestStreak = 0,
                        lastUpdatedDate = today,
                        isActive = true
                    )
                )
            }
        }
    }

    /**
     * Update all streaks for a given date
     */
    suspend fun updateStreaksForDate(date: String) = withContext(Dispatchers.IO) {
        // Get all meals for the date
        val meals = mealDao.getMealsByDateSuspend(date)

        // Update each streak type
        updateHealthyMealsStreak(date, meals)
        updateAllMealsLoggedStreak(date, meals)
        updateNoJunkStreak(date, meals)
        updatePerfectDayStreak(date, meals)
    }

    /**
     * Update healthy meals streak (at least 3 healthy meals)
     */
    private suspend fun updateHealthyMealsStreak(date: String, meals: List<com.example.mealtracker.Meal>) {
        val healthyCount = meals.count { it.category == "Healthy" }
        val streak = streakDao.getStreakByType(StreakType.HEALTHY_MEALS) ?: return

        if (healthyCount >= 3) {
            incrementStreak(streak, date)
        } else {
            resetStreak(streak, date)
        }
    }

    /**
     * Update all meals logged streak (all 4 meals logged)
     */
    private suspend fun updateAllMealsLoggedStreak(date: String, meals: List<com.example.mealtracker.Meal>) {
        val mealTypes = meals.map { it.type }.toSet()
        val allTypesLogged = mealTypes.containsAll(
            listOf("Breakfast", "Lunch", "Evening Snacks", "Dinner")
        )

        val streak = streakDao.getStreakByType(StreakType.ALL_MEALS_LOGGED) ?: return

        if (allTypesLogged) {
            incrementStreak(streak, date)
        } else {
            resetStreak(streak, date)
        }
    }

    /**
     * Update no junk streak (no junk food for the day)
     */
    private suspend fun updateNoJunkStreak(date: String, meals: List<com.example.mealtracker.Meal>) {
        val hasJunk = meals.any { it.category == "Junk" }
        val streak = streakDao.getStreakByType(StreakType.NO_JUNK) ?: return

        if (!hasJunk && meals.isNotEmpty()) {
            incrementStreak(streak, date)
        } else if (hasJunk) {
            resetStreak(streak, date)
        }
    }

    /**
     * Update perfect day streak (all 4 meals logged and all healthy)
     */
    private suspend fun updatePerfectDayStreak(date: String, meals: List<com.example.mealtracker.Meal>) {
        val mealTypes = meals.map { it.type }.toSet()
        val allTypesLogged = mealTypes.containsAll(
            listOf("Breakfast", "Lunch", "Evening Snacks", "Dinner")
        )
        val allHealthy = meals.all { it.category == "Healthy" }

        val streak = streakDao.getStreakByType(StreakType.PERFECT_DAY) ?: return

        if (allTypesLogged && allHealthy && meals.size == 4) {
            incrementStreak(streak, date)
        } else {
            resetStreak(streak, date)
        }
    }

    /**
     * Increment a streak
     */
    private suspend fun incrementStreak(streak: Streak, date: String) {
        val lastDate = parseDate(streak.lastUpdatedDate)
        val currentDate = parseDate(date)

        val daysDiff = daysBetween(lastDate, currentDate)

        val newCurrentStreak = if (daysDiff == 1) {
            // Consecutive day
            streak.currentStreak + 1
        } else if (daysDiff == 0) {
            // Same day, keep current
            streak.currentStreak
        } else {
            // Gap in streak, reset to 1
            1
        }

        val newLongestStreak = maxOf(streak.longestStreak, newCurrentStreak)

        streakDao.updateStreakValues(
            streak.id,
            newCurrentStreak,
            newLongestStreak,
            date
        )
    }

    /**
     * Reset a streak to 0
     */
    private suspend fun resetStreak(streak: Streak, date: String) {
        streakDao.resetCurrentStreak(streak.id, date)
    }

    /**
     * Parse date string to Calendar
     */
    private fun parseDate(dateString: String): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(dateString) ?: Date()
        return calendar
    }

    /**
     * Calculate days between two dates
     */
    private fun daysBetween(date1: Calendar, date2: Calendar): Int {
        val diff = date2.timeInMillis - date1.timeInMillis
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }
}

