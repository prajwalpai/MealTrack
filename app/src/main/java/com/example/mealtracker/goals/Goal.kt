package com.example.mealtracker.goals

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a user goal
 *
 * @property id Unique identifier for the goal
 * @property type Type of goal (daily_healthy_meals, weekly_healthy_meals, etc.)
 * @property targetValue Target value to achieve
 * @property currentValue Current progress towards goal
 * @property startDate When the goal started
 * @property endDate When the goal ends (null for ongoing goals)
 * @property isActive Whether the goal is currently active
 * @property createdAt When the goal was created
 */
@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String,  // "daily_healthy_meals", "weekly_healthy_meals", "monthly_healthy_meals"
    val targetValue: Int,
    val currentValue: Int = 0,
    val startDate: String,
    val endDate: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Goal types
 */
object GoalType {
    const val DAILY_HEALTHY_MEALS = "daily_healthy_meals"
    const val WEEKLY_HEALTHY_MEALS = "weekly_healthy_meals"
    const val MONTHLY_HEALTHY_MEALS = "monthly_healthy_meals"
    const val DAILY_ALL_MEALS = "daily_all_meals"  // Log all 4 meals
}

