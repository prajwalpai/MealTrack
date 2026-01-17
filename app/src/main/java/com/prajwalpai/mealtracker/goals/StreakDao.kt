package com.prajwalpai.mealtracker.goals

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for Streak operations
 */
@Dao
interface StreakDao {

    /**
     * Insert a new streak
     */
    @Insert
    suspend fun insert(streak: Streak): Long

    /**
     * Update an existing streak
     */
    @Update
    suspend fun update(streak: Streak)

    /**
     * Delete a streak
     */
    @Delete
    suspend fun delete(streak: Streak)

    /**
     * Get all active streaks
     */
    @Query("SELECT * FROM streaks WHERE isActive = 1")
    fun getAllActiveStreaks(): LiveData<List<Streak>>

    /**
     * Get a specific streak by type
     */
    @Query("SELECT * FROM streaks WHERE type = :type AND isActive = 1 LIMIT 1")
    suspend fun getStreakByType(type: String): Streak?

    /**
     * Get a specific streak by type (LiveData)
     */
    @Query("SELECT * FROM streaks WHERE type = :type AND isActive = 1 LIMIT 1")
    fun getStreakByTypeLiveData(type: String): LiveData<Streak?>

    /**
     * Update streak values
     */
    @Query("UPDATE streaks SET currentStreak = :currentStreak, longestStreak = :longestStreak, lastUpdatedDate = :lastUpdatedDate WHERE id = :streakId")
    suspend fun updateStreakValues(streakId: Long, currentStreak: Int, longestStreak: Int, lastUpdatedDate: String)

    /**
     * Reset current streak to 0
     */
    @Query("UPDATE streaks SET currentStreak = 0, lastUpdatedDate = :date WHERE id = :streakId")
    suspend fun resetCurrentStreak(streakId: Long, date: String)

    /**
     * Delete all streaks (for testing)
     */
    @Query("DELETE FROM streaks")
    suspend fun deleteAll()
}

