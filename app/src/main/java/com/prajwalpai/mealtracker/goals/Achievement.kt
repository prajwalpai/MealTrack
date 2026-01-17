package com.prajwalpai.mealtracker.goals

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing an achievement/badge
 *
 * @property id Unique identifier for the achievement
 * @property name Name of the achievement
 * @property description Description of what was achieved
 * @property icon Icon/emoji for the achievement
 * @property type Type of achievement
 * @property unlockedAt When the achievement was unlocked
 * @property isUnlocked Whether the achievement has been unlocked
 */
@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val icon: String,  // Emoji or icon name
    val type: String,
    val unlockedAt: Long? = null,
    val isUnlocked: Boolean = false
)

/**
 * Achievement types and definitions
 */
object AchievementType {
    const val FIRST_MEAL = "first_meal"
    const val WEEK_STREAK = "week_streak"
    const val MONTH_STREAK = "month_streak"
    const val HUNDRED_MEALS = "hundred_meals"
    const val HEALTHY_WEEK = "healthy_week"
    const val PERFECT_WEEK = "perfect_week"
    const val NO_JUNK_WEEK = "no_junk_week"

    /**
     * Get all available achievements
     */
    fun getAllAchievements(): List<Achievement> {
        return listOf(
            Achievement(
                name = "First Steps",
                description = "Log your first meal",
                icon = "🎯",
                type = FIRST_MEAL
            ),
            Achievement(
                name = "Week Warrior",
                description = "Maintain a 7-day streak",
                icon = "🔥",
                type = WEEK_STREAK
            ),
            Achievement(
                name = "Month Master",
                description = "Maintain a 30-day streak",
                icon = "👑",
                type = MONTH_STREAK
            ),
            Achievement(
                name = "Century Club",
                description = "Log 100 meals",
                icon = "💯",
                type = HUNDRED_MEALS
            ),
            Achievement(
                name = "Healthy Hero",
                description = "Eat healthy for a full week",
                icon = "🥗",
                type = HEALTHY_WEEK
            ),
            Achievement(
                name = "Perfect Week",
                description = "Log all 4 meals every day for a week",
                icon = "⭐",
                type = PERFECT_WEEK
            ),
            Achievement(
                name = "Junk-Free Journey",
                description = "No junk food for a full week",
                icon = "🚫",
                type = NO_JUNK_WEEK
            )
        )
    }
}

