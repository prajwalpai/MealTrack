package com.prajwalpai.mealtracker.goals

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a streak
 *
 * @property id Unique identifier for the streak
 * @property type Type of streak (healthy_meals, all_meals_logged, etc.)
 * @property currentStreak Current consecutive days
 * @property longestStreak Longest streak ever achieved
 * @property lastUpdatedDate Last date the streak was updated
 * @property isActive Whether the streak is currently active
 */
@Entity(tableName = "streaks")
data class Streak(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,  // "healthy_meals", "all_meals_logged", "no_junk"
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastUpdatedDate: String,  // yyyy-MM-dd format
    val isActive: Boolean = true
)

/**
 * Streak types
 */
object StreakType {
    const val HEALTHY_MEALS = "healthy_meals"  // At least 3 healthy meals per day
    const val ALL_MEALS_LOGGED = "all_meals_logged"  // All 4 meals logged
    const val NO_JUNK = "no_junk"  // No junk food for the day
    const val PERFECT_DAY = "perfect_day"  // All 4 meals logged and all healthy
}

