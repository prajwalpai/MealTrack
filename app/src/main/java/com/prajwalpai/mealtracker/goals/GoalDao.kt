package com.prajwalpai.mealtracker.goals

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for Goal operations
 */
@Dao
interface GoalDao {

    /**
     * Insert a new goal
     */
    @Insert
    suspend fun insert(goal: Goal): Long

    /**
     * Update an existing goal
     */
    @Update
    suspend fun update(goal: Goal)

    /**
     * Delete a goal
     */
    @Delete
    suspend fun delete(goal: Goal)

    /**
     * Get all active goals
     */
    @Query("SELECT * FROM goals WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveGoals(): LiveData<List<Goal>>

    /**
     * Get a specific goal by ID
     */
    @Query("SELECT * FROM goals WHERE id = :goalId")
    suspend fun getGoalById(goalId: Long): Goal?

    /**
     * Get active goals by type
     */
    @Query("SELECT * FROM goals WHERE type = :type AND isActive = 1")
    fun getActiveGoalsByType(type: String): LiveData<List<Goal>>

    /**
     * Deactivate a goal
     */
    @Query("UPDATE goals SET isActive = 0 WHERE id = :goalId")
    suspend fun deactivateGoal(goalId: Long)

    /**
     * Update goal progress
     */
    @Query("UPDATE goals SET currentValue = :currentValue WHERE id = :goalId")
    suspend fun updateGoalProgress(goalId: Long, currentValue: Int)

    /**
     * Delete all goals (for testing)
     */
    @Query("DELETE FROM goals")
    suspend fun deleteAll()
}

