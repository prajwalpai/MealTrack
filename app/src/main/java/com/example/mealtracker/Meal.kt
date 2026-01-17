package com.example.mealtracker

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a meal entry
 *
 * @property id Unique identifier for the meal (auto-generated)
 * @property name The name of the meal (e.g., "Oatmeal", "Pizza")
 * @property type The type of meal (Breakfast, Lunch, Evening Snacks, Dinner)
 * @property category The health category (Healthy, Neutral, Junk)
 * @property date The date of the meal in format "yyyy-MM-dd"
 * @property timestamp When the meal was logged (milliseconds since epoch)
 */
@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String,
    val category: String,
    val date: String,
    val timestamp: Long = System.currentTimeMillis()
)

