package com.example.mealtracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mealtracker.goals.*

/**
 * Room Database for Meal Tracker
 * Singleton pattern ensures only one instance exists
 */
@Database(
    entities = [
        Meal::class,
        Goal::class,
        Streak::class,
        Achievement::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MealDatabase : RoomDatabase() {

    /**
     * Get the MealDao for database operations
     */
    abstract fun mealDao(): MealDao

    /**
     * Get the GoalDao for goal operations
     */
    abstract fun goalDao(): GoalDao

    /**
     * Get the StreakDao for streak operations
     */
    abstract fun streakDao(): StreakDao

    /**
     * Get the AchievementDao for achievement operations
     */
    abstract fun achievementDao(): AchievementDao
    
    companion object {
        // Singleton instance
        @Volatile
        private var INSTANCE: MealDatabase? = null
        
        /**
         * Get database instance (creates if doesn't exist)
         * @param context Application context
         * @return MealDatabase instance
         */
        fun getDatabase(context: Context): MealDatabase {
            // Return existing instance or create new one
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database"
                )
                    .fallbackToDestructiveMigration() // For development - recreates DB on schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

