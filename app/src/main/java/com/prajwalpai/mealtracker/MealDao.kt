package com.prajwalpai.mealtracker

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for Meal operations
 * Defines all database queries for meals
 */
@Dao
interface MealDao {
    
    /**
     * Insert a new meal into the database
     * @param meal The meal to insert
     * @return The ID of the inserted meal
     */
    @Insert
    suspend fun insert(meal: Meal): Long
    
    /**
     * Update an existing meal
     * @param meal The meal to update
     */
    @Update
    suspend fun update(meal: Meal)
    
    /**
     * Delete a meal from the database
     * @param meal The meal to delete
     */
    @Delete
    suspend fun delete(meal: Meal)
    
    /**
     * Get all meals ordered by date (newest first) and timestamp
     * @return LiveData list of all meals
     */
    @Query("SELECT * FROM meals ORDER BY date DESC, timestamp DESC")
    fun getAllMeals(): LiveData<List<Meal>>

    /**
     * Get all meals (suspend version for backup/restore)
     * @return List of all meals
     */
    @Query("SELECT * FROM meals ORDER BY date DESC, timestamp DESC")
    suspend fun getAllMealsSuspend(): List<Meal>
    
    /**
     * Get meals for a specific date
     * @param date The date in format "yyyy-MM-dd"
     * @return LiveData list of meals for that date
     */
    @Query("SELECT * FROM meals WHERE date = :date ORDER BY timestamp ASC")
    fun getMealsByDate(date: String): LiveData<List<Meal>>

    /**
     * Get meals for a specific date (suspend version for coroutines)
     * @param date The date in format "yyyy-MM-dd"
     * @return List of meals for that date
     */
    @Query("SELECT * FROM meals WHERE date = :date ORDER BY timestamp ASC")
    suspend fun getMealsByDateSuspend(date: String): List<Meal>

    /**
     * Get meals for a specific date and type
     * @param date The date in format "yyyy-MM-dd"
     * @param type The meal type (Breakfast, Lunch, Evening Snacks, Dinner)
     * @return LiveData list of meals matching date and type
     */
    @Query("SELECT * FROM meals WHERE date = :date AND type = :type ORDER BY timestamp DESC")
    fun getMealsByDateAndType(date: String, type: String): LiveData<List<Meal>>

    /**
     * Get meals for a specific date and type (suspend version for Workers)
     * @param date The date in format "yyyy-MM-dd"
     * @param type The meal type (Breakfast, Lunch, Evening Snacks, Dinner)
     * @return List of meals matching date and type
     */
    @Query("SELECT * FROM meals WHERE date = :date AND type = :type ORDER BY timestamp DESC")
    suspend fun getMealsByDateAndTypeSuspend(date: String, type: String): List<Meal>
    
    /**
     * Get meals within a date range
     * @param startDate Start date in format "yyyy-MM-dd"
     * @param endDate End date in format "yyyy-MM-dd"
     * @return LiveData list of meals in the date range
     */
    @Query("SELECT * FROM meals WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, timestamp DESC")
    fun getMealsByDateRange(startDate: String, endDate: String): LiveData<List<Meal>>

    /**
     * Get meals within a date range (suspend version for widgets)
     * @param startDate Start date in format "yyyy-MM-dd"
     * @param endDate End date in format "yyyy-MM-dd"
     * @return List of meals in the date range
     */
    @Query("SELECT * FROM meals WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC, timestamp DESC")
    suspend fun getMealsByDateRangeSuspend(startDate: String, endDate: String): List<Meal>

    /**
     * Get count of meals by category for a date range
     * Used for statistics
     */
    @Query("SELECT category, COUNT(*) as count FROM meals WHERE date BETWEEN :startDate AND :endDate GROUP BY category")
    suspend fun getCategoryCountsByDateRange(startDate: String, endDate: String): List<CategoryCount>

    /**
     * Get unique dates in a date range
     * Used to count days tracked
     */
    @Query("SELECT DISTINCT date FROM meals WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    suspend fun getUniqueDatesInRange(startDate: String, endDate: String): List<String>

    /**
     * Get count of meals per date in a date range
     * Used to find perfect days (4 meals/day)
     */
    @Query("SELECT date, COUNT(*) as count FROM meals WHERE date BETWEEN :startDate AND :endDate GROUP BY date")
    suspend fun getMealsCountByDate(startDate: String, endDate: String): List<DateCount>

    /**
     * Delete all meals (for testing/reset)
     */
    @Query("DELETE FROM meals")
    suspend fun deleteAll()
}

/**
 * Data class for category count results
 */
data class CategoryCount(
    val category: String,
    val count: Int
)

/**
 * Data class for date count results
 */
data class DateCount(
    val date: String,
    val count: Int
)

