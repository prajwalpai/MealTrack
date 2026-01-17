package com.prajwalpai.mealtracker

import androidx.lifecycle.LiveData

/**
 * Repository for Meal data
 * Abstracts data sources and provides clean API for ViewModels
 */
class MealRepository(private val mealDao: MealDao) {
    
    /**
     * Get all meals as LiveData
     */
    val allMeals: LiveData<List<Meal>> = mealDao.getAllMeals()
    
    /**
     * Insert a new meal
     * @param meal The meal to insert
     */
    suspend fun insert(meal: Meal): Long {
        return mealDao.insert(meal)
    }
    
    /**
     * Update an existing meal
     * @param meal The meal to update
     */
    suspend fun update(meal: Meal) {
        mealDao.update(meal)
    }
    
    /**
     * Delete a meal
     * @param meal The meal to delete
     */
    suspend fun delete(meal: Meal) {
        mealDao.delete(meal)
    }
    
    /**
     * Get meals for a specific date
     * @param date The date in format "yyyy-MM-dd"
     */
    fun getMealsByDate(date: String): LiveData<List<Meal>> {
        return mealDao.getMealsByDate(date)
    }
    
    /**
     * Get meals for a specific date and type
     * @param date The date in format "yyyy-MM-dd"
     * @param type The meal type
     */
    fun getMealsByDateAndType(date: String, type: String): LiveData<List<Meal>> {
        return mealDao.getMealsByDateAndType(date, type)
    }
    
    /**
     * Get meals within a date range
     * @param startDate Start date
     * @param endDate End date
     */
    fun getMealsByDateRange(startDate: String, endDate: String): LiveData<List<Meal>> {
        return mealDao.getMealsByDateRange(startDate, endDate)
    }
    
    /**
     * Get category counts for a date range
     * @param startDate Start date
     * @param endDate End date
     */
    suspend fun getCategoryCountsByDateRange(startDate: String, endDate: String): List<CategoryCount> {
        return mealDao.getCategoryCountsByDateRange(startDate, endDate)
    }

    /**
     * Get unique dates in a date range
     * @param startDate Start date
     * @param endDate End date
     */
    suspend fun getUniqueDatesInRange(startDate: String, endDate: String): List<String> {
        return mealDao.getUniqueDatesInRange(startDate, endDate)
    }

    /**
     * Get meals count by date
     * @param startDate Start date
     * @param endDate End date
     */
    suspend fun getMealsCountByDate(startDate: String, endDate: String): List<DateCount> {
        return mealDao.getMealsCountByDate(startDate, endDate)
    }

    /**
     * Delete all meals
     */
    suspend fun deleteAll() {
        mealDao.deleteAll()
    }
}

