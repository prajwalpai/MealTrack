package com.prajwalpai.mealtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prajwalpai.mealtracker.goals.*
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

/**
 * Activity to display and manage goals, streaks, and achievements
 */
class GoalsActivity : AppCompatActivity() {

    private lateinit var backButton: MaterialButton
    private lateinit var streaksContainer: LinearLayout
    private lateinit var achievementsRecyclerView: RecyclerView
    private lateinit var goalsContainer: LinearLayout
    private lateinit var addGoalButton: MaterialButton

    private lateinit var database: MealDatabase
    private lateinit var streakTracker: StreakTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)

        database = MealDatabase.getDatabase(this)
        streakTracker = StreakTracker(this)

        initializeViews()
        setupListeners()
        loadData()
    }

    private fun initializeViews() {
        backButton = findViewById(R.id.backButton)
        streaksContainer = findViewById(R.id.streaksContainer)
        achievementsRecyclerView = findViewById(R.id.achievementsRecyclerView)
        goalsContainer = findViewById(R.id.goalsContainer)
        addGoalButton = findViewById(R.id.addGoalButton)

        // Setup achievements RecyclerView
        achievementsRecyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            finish()
        }

        addGoalButton.setOnClickListener {
            Toast.makeText(this, "Add Goal feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            // Initialize streaks if needed
            streakTracker.initializeStreaks()

            // Initialize achievements if needed
            initializeAchievements()

            // Load streaks
            loadStreaks()

            // Load achievements
            loadAchievements()

            // Load goals
            loadGoals()
        }
    }

    private suspend fun initializeAchievements() {
        val existingAchievements = database.achievementDao().getAllAchievements().value
        if (existingAchievements.isNullOrEmpty()) {
            database.achievementDao().insertAll(AchievementType.getAllAchievements())
        }
    }

    private fun loadStreaks() {
        database.streakDao().getAllActiveStreaks().observe(this) { streaks ->
            displayStreaks(streaks)
        }
    }

    private fun displayStreaks(streaks: List<Streak>) {
        streaksContainer.removeAllViews()

        for (streak in streaks) {
            val streakView = LayoutInflater.from(this)
                .inflate(R.layout.item_streak_card, streaksContainer, false)

            val streakIcon = streakView.findViewById<TextView>(R.id.streakIcon)
            val streakName = streakView.findViewById<TextView>(R.id.streakName)
            val streakDescription = streakView.findViewById<TextView>(R.id.streakDescription)
            val currentStreakText = streakView.findViewById<TextView>(R.id.currentStreakText)
            val longestStreakText = streakView.findViewById<TextView>(R.id.longestStreakText)

            // Set streak data
            val (icon, name, description) = getStreakInfo(streak.type)
            streakIcon.text = icon
            streakName.text = name
            streakDescription.text = description
            currentStreakText.text = streak.currentStreak.toString()
            longestStreakText.text = "Best: ${streak.longestStreak}"

            streaksContainer.addView(streakView)
        }
    }

    private fun getStreakInfo(type: String): Triple<String, String, String> {
        return when (type) {
            StreakType.HEALTHY_MEALS -> Triple("🥗", "Healthy Meals", "At least 3 healthy meals per day")
            StreakType.ALL_MEALS_LOGGED -> Triple("📝", "All Meals Logged", "Log all 4 meals every day")
            StreakType.NO_JUNK -> Triple("🚫", "No Junk Food", "No junk food for the day")
            StreakType.PERFECT_DAY -> Triple("⭐", "Perfect Days", "All 4 meals logged and healthy")
            else -> Triple("🔥", "Streak", "Keep it going!")
        }
    }

    private fun loadAchievements() {
        database.achievementDao().getAllAchievements().observe(this) { achievements ->
            displayAchievements(achievements)
        }
    }

    private fun displayAchievements(achievements: List<Achievement>) {
        val adapter = AchievementAdapter(achievements)
        achievementsRecyclerView.adapter = adapter
    }

    private fun loadGoals() {
        database.goalDao().getAllActiveGoals().observe(this) { goals ->
            displayGoals(goals)
        }
    }

    private fun displayGoals(goals: List<Goal>) {
        goalsContainer.removeAllViews()

        if (goals.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No active goals. Tap 'Add New Goal' to get started!"
                textSize = 14f
                setTextColor(getColor(R.color.text_secondary))
                setPadding(16, 16, 16, 16)
            }
            goalsContainer.addView(emptyText)
        }
    }
}

