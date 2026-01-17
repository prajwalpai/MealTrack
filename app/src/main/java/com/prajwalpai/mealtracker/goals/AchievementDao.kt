package com.prajwalpai.mealtracker.goals

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Data Access Object for Achievement operations
 */
@Dao
interface AchievementDao {

    /**
     * Insert a new achievement
     */
    @Insert
    suspend fun insert(achievement: Achievement): Long

    /**
     * Insert multiple achievements
     */
    @Insert
    suspend fun insertAll(achievements: List<Achievement>)

    /**
     * Update an existing achievement
     */
    @Update
    suspend fun update(achievement: Achievement)

    /**
     * Delete an achievement
     */
    @Delete
    suspend fun delete(achievement: Achievement)

    /**
     * Get all achievements
     */
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, id ASC")
    fun getAllAchievements(): LiveData<List<Achievement>>

    /**
     * Get unlocked achievements
     */
    @Query("SELECT * FROM achievements WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedAchievements(): LiveData<List<Achievement>>

    /**
     * Get locked achievements
     */
    @Query("SELECT * FROM achievements WHERE isUnlocked = 0")
    fun getLockedAchievements(): LiveData<List<Achievement>>

    /**
     * Get achievement by type
     */
    @Query("SELECT * FROM achievements WHERE type = :type LIMIT 1")
    suspend fun getAchievementByType(type: String): Achievement?

    /**
     * Unlock an achievement
     */
    @Query("UPDATE achievements SET isUnlocked = 1, unlockedAt = :unlockedAt WHERE id = :achievementId")
    suspend fun unlockAchievement(achievementId: Long, unlockedAt: Long)

    /**
     * Check if achievement is unlocked
     */
    @Query("SELECT isUnlocked FROM achievements WHERE type = :type LIMIT 1")
    suspend fun isAchievementUnlocked(type: String): Boolean?

    /**
     * Delete all achievements (for testing)
     */
    @Query("DELETE FROM achievements")
    suspend fun deleteAll()
}

