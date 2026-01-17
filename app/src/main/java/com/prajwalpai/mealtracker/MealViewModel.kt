package com.prajwalpai.mealtracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for Meal operations
 * Manages UI-related data and handles business logic
 */
class MealViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: MealRepository
    val allMeals: LiveData<List<Meal>>
    
    init {
        val mealDao = MealDatabase.getDatabase(application).mealDao()
        repository = MealRepository(mealDao)
        allMeals = repository.allMeals
    }
    
    /**
     * Insert a new meal
     * Runs in background thread using coroutines
     */
    fun insert(meal: Meal) = viewModelScope.launch {
        repository.insert(meal)
    }
    
    /**
     * Update an existing meal
     */
    fun update(meal: Meal) = viewModelScope.launch {
        repository.update(meal)
    }
    
    /**
     * Delete a meal
     */
    fun delete(meal: Meal) = viewModelScope.launch {
        repository.delete(meal)
    }
    
    /**
     * Get meals for a specific date
     */
    fun getMealsByDate(date: String): LiveData<List<Meal>> {
        return repository.getMealsByDate(date)
    }
    
    /**
     * Get meals for a specific date and type
     */
    fun getMealsByDateAndType(date: String, type: String): LiveData<List<Meal>> {
        return repository.getMealsByDateAndType(date, type)
    }
    
    /**
     * Get meals within a date range
     */
    fun getMealsByDateRange(startDate: String, endDate: String): LiveData<List<Meal>> {
        return repository.getMealsByDateRange(startDate, endDate)
    }
    
    /**
     * Get category counts for statistics
     */
    fun getCategoryCountsByDateRange(
        startDate: String,
        endDate: String,
        callback: (List<CategoryCount>) -> Unit
    ) = viewModelScope.launch {
        val counts = repository.getCategoryCountsByDateRange(startDate, endDate)
        callback(counts)
    }

    /**
     * Get unique dates in a date range
     */
    fun getUniqueDatesInRange(
        startDate: String,
        endDate: String,
        callback: (List<String>) -> Unit
    ) = viewModelScope.launch {
        val dates = repository.getUniqueDatesInRange(startDate, endDate)
        callback(dates)
    }

    /**
     * Get meals count by date
     */
    fun getMealsCountByDate(
        startDate: String,
        endDate: String,
        callback: (List<DateCount>) -> Unit
    ) = viewModelScope.launch {
        val counts = repository.getMealsCountByDate(startDate, endDate)
        callback(counts)
    }

    /**
     * Delete all meals (for testing)
     */
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

