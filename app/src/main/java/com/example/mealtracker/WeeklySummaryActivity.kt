package com.example.mealtracker

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class WeeklySummaryActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var weekRangeText: TextView
    private lateinit var totalMealsText: TextView
    private lateinit var healthyStatsText: TextView
    private lateinit var neutralStatsText: TextView
    private lateinit var junkStatsText: TextView
    private lateinit var healthyProgressBar: ProgressBar
    private lateinit var neutralProgressBar: ProgressBar
    private lateinit var junkProgressBar: ProgressBar
    private lateinit var motivationalMessage: TextView
    private lateinit var dailyBreakdownContainer: LinearLayout

    // Data
    private lateinit var mealViewModel: MealViewModel
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Mon, Tue, etc.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_summary)

        // Initialize ViewModel
        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        // Initialize UI
        initializeViews()

        // Calculate and display weekly stats
        calculateWeeklyStats()
    }

    private fun initializeViews() {
        weekRangeText = findViewById(R.id.weekRangeText)
        totalMealsText = findViewById(R.id.totalMealsText)
        healthyStatsText = findViewById(R.id.healthyStatsText)
        neutralStatsText = findViewById(R.id.neutralStatsText)
        junkStatsText = findViewById(R.id.junkStatsText)
        healthyProgressBar = findViewById(R.id.healthyProgressBar)
        neutralProgressBar = findViewById(R.id.neutralProgressBar)
        junkProgressBar = findViewById(R.id.junkProgressBar)
        motivationalMessage = findViewById(R.id.motivationalMessage)
        dailyBreakdownContainer = findViewById(R.id.dailyBreakdownContainer)

        // Set up monthly summary button
        val viewMonthlyButton = findViewById<Button>(R.id.viewMonthlyButton)
        addButtonAnimation(viewMonthlyButton)
        viewMonthlyButton.setOnClickListener {
            val intent = Intent(this, MonthlySummaryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.fade_out)
        }
    }

    private fun addButtonAnimation(button: View) {
        button.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val scaleDown = AnimationUtils.loadAnimation(this, R.anim.button_scale_down)
                    view.startAnimation(scaleDown)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val scaleUp = AnimationUtils.loadAnimation(this, R.anim.button_scale_up)
                    view.startAnimation(scaleUp)
                }
            }
            false // Return false to allow click events to proceed
        }
    }

    private fun calculateWeeklyStats() {
        // Get current calendar week (Monday - Sunday)
        val today = Calendar.getInstance()

        // Find the Monday of current week
        val startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        // If today is Sunday, we need to go back to previous Monday
        if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            startCalendar.add(Calendar.WEEK_OF_YEAR, -1)
        }

        // Find the Sunday of current week
        val endCalendar = Calendar.getInstance()
        endCalendar.time = startCalendar.time
        endCalendar.add(Calendar.DAY_OF_MONTH, 6) // Monday + 6 days = Sunday

        val startDate = dateFormat.format(startCalendar.time)
        val endDate = dateFormat.format(endCalendar.time)

        // Display week range
        val weekRangeDisplay = "${displayDateFormat.format(startCalendar.time)} - ${displayDateFormat.format(endCalendar.time)}, ${endCalendar.get(Calendar.YEAR)}"
        weekRangeText.text = weekRangeDisplay

        // Debug: Log the dates
        android.util.Log.d("WeeklySummary", "Start Date: $startDate, End Date: $endDate")

        // Debug: Check all meals first
        mealViewModel.allMeals.observe(this) { allMeals ->
            android.util.Log.d("WeeklySummary", "Total meals in DB: ${allMeals.size}")
            allMeals.forEach { meal ->
                android.util.Log.d("WeeklySummary", "Meal: ${meal.name}, Date: ${meal.date}, Category: ${meal.category}")
            }
        }

        // Get category counts from database
        mealViewModel.getCategoryCountsByDateRange(startDate, endDate) { categoryCounts ->
            // Debug: Log the results
            android.util.Log.d("WeeklySummary", "Category Counts: $categoryCounts")
            runOnUiThread {
                displayStats(categoryCounts)
            }
        }
    }

    private fun displayStats(categoryCounts: List<CategoryCount>) {
        // Calculate totals
        var healthyCount = 0
        var neutralCount = 0
        var junkCount = 0

        for (categoryCount in categoryCounts) {
            when (categoryCount.category) {
                "Healthy" -> healthyCount = categoryCount.count
                "Neutral" -> neutralCount = categoryCount.count
                "Junk" -> junkCount = categoryCount.count
            }
        }

        val totalMeals = healthyCount + neutralCount + junkCount

        // Display total
        totalMealsText.text = "Total Meals: $totalMeals"

        if (totalMeals > 0) {
            // Calculate percentages
            val healthyPercent = (healthyCount * 100) / totalMeals
            val neutralPercent = (neutralCount * 100) / totalMeals
            val junkPercent = (junkCount * 100) / totalMeals

            // Display stats
            healthyStatsText.text = "🟢 Healthy: $healthyCount ($healthyPercent%)"
            neutralStatsText.text = "🟡 Neutral: $neutralCount ($neutralPercent%)"
            junkStatsText.text = "🔴 Junk: $junkCount ($junkPercent%)"

            // Update progress bars with animation
            animateProgressBar(healthyProgressBar, healthyPercent)
            animateProgressBar(neutralProgressBar, neutralPercent)
            animateProgressBar(junkProgressBar, junkPercent)

            // Set motivational message
            motivationalMessage.text = getMotivationalMessage(healthyPercent, junkPercent, totalMeals)
        } else {
            // No meals logged this week
            healthyStatsText.text = "🟢 Healthy: 0 (0%)"
            neutralStatsText.text = "🟡 Neutral: 0 (0%)"
            junkStatsText.text = "🔴 Junk: 0 (0%)"
            
            healthyProgressBar.progress = 0
            neutralProgressBar.progress = 0
            junkProgressBar.progress = 0
            
            motivationalMessage.text = "Start tracking your meals to see your weekly stats! 🎯"
        }

        // Populate daily breakdown
        populateDailyBreakdown()
    }

    private fun populateDailyBreakdown() {
        // Clear existing views
        dailyBreakdownContainer.removeAllViews()

        // Get current week (Monday - Sunday)
        val startCalendar = Calendar.getInstance()
        startCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        // If today is Sunday, we need to go back to previous Monday
        val today = Calendar.getInstance()
        if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            startCalendar.add(Calendar.WEEK_OF_YEAR, -1)
        }

        // Observe all meals to build daily breakdown
        mealViewModel.allMeals.observe(this) { allMeals ->
            // Clear again in case of updates
            dailyBreakdownContainer.removeAllViews()

            // For each day of the week (Monday to Sunday)
            for (i in 0..6) {
                val dayCalendar = Calendar.getInstance()
                dayCalendar.time = startCalendar.time
                dayCalendar.add(Calendar.DAY_OF_MONTH, i)
                val dayDate = dateFormat.format(dayCalendar.time)
                val dayName = dayFormat.format(dayCalendar.time)

                // Get meals for this day
                val mealsForDay = allMeals.filter { it.date == dayDate }

                // Count meals by type
                val mealTypes = listOf("Breakfast", "Lunch", "Evening Snacks", "Dinner")
                val loggedTypes = mealsForDay.map { it.type }.toSet()

                // Create row for this day
                val dayRow = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    setPadding(0, 8, 0, 8)
                }

                // Day name
                val dayNameText = TextView(this).apply {
                    text = dayName
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.black, null))
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                }
                dayRow.addView(dayNameText)

                // Dots for each meal type
                val dotsText = TextView(this).apply {
                    val dots = mealTypes.map { type ->
                        if (loggedTypes.contains(type)) "●" else "○"
                    }.joinToString(" ")
                    text = dots
                    textSize = 18f
                    setTextColor(resources.getColor(android.R.color.black, null))
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f
                    )
                }
                dayRow.addView(dotsText)

                // Meal count
                val countText = TextView(this).apply {
                    text = "(${mealsForDay.size}/4)"
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                dayRow.addView(countText)

                // Add row to container
                dailyBreakdownContainer.addView(dayRow)
            }
        }
    }

    private fun getMotivationalMessage(healthyPercent: Int, junkPercent: Int, totalMeals: Int): String {
        return when {
            healthyPercent >= 70 -> "🌟 Excellent! You're eating very healthy this week! Keep it up!"
            healthyPercent >= 50 -> "👍 Good job! More than half your meals are healthy!"
            healthyPercent >= 30 -> "💪 You're making progress! Try to add more healthy meals!"
            junkPercent >= 50 -> "⚠️ Too much junk food this week. Let's aim for healthier choices!"
            totalMeals < 14 -> "📝 Try to log all your meals for better insights!"
            else -> "Keep tracking! Every healthy choice counts! 🎯"
        }
    }

    private fun animateProgressBar(progressBar: ProgressBar, targetProgress: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, targetProgress)
        animator.duration = 1000 // 1 second animation
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }
}

