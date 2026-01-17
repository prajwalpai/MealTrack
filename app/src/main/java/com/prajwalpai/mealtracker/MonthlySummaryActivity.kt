package com.prajwalpai.mealtracker

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

class MonthlySummaryActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var currentMonthText: TextView
    private lateinit var previousMonthButton: ImageButton
    private lateinit var nextMonthButton: ImageButton
    private lateinit var totalMealsText: TextView
    private lateinit var daysTrackedText: TextView
    private lateinit var healthyStatsText: TextView
    private lateinit var neutralStatsText: TextView
    private lateinit var junkStatsText: TextView
    private lateinit var healthyProgressBar: ProgressBar
    private lateinit var neutralProgressBar: ProgressBar
    private lateinit var junkProgressBar: ProgressBar
    private lateinit var weeklyBreakdownContainer: LinearLayout
    private lateinit var bestWeekText: TextView
    private lateinit var perfectDaysText: TextView
    private lateinit var healthyPercentText: TextView

    // Data
    private lateinit var mealViewModel: MealViewModel
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private var currentCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthly_summary)

        // Initialize ViewModel
        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        // Initialize UI
        initializeViews()

        // Set up navigation
        setupNavigation()

        // Calculate and display monthly stats
        calculateMonthlyStats()
    }

    private fun initializeViews() {
        currentMonthText = findViewById(R.id.currentMonthText)
        previousMonthButton = findViewById(R.id.previousMonthButton)
        nextMonthButton = findViewById(R.id.nextMonthButton)
        totalMealsText = findViewById(R.id.totalMealsText)
        daysTrackedText = findViewById(R.id.daysTrackedText)
        healthyStatsText = findViewById(R.id.healthyStatsText)
        neutralStatsText = findViewById(R.id.neutralStatsText)
        junkStatsText = findViewById(R.id.junkStatsText)
        healthyProgressBar = findViewById(R.id.healthyProgressBar)
        neutralProgressBar = findViewById(R.id.neutralProgressBar)
        junkProgressBar = findViewById(R.id.junkProgressBar)
        weeklyBreakdownContainer = findViewById(R.id.weeklyBreakdownContainer)
        bestWeekText = findViewById(R.id.bestWeekText)
        perfectDaysText = findViewById(R.id.perfectDaysText)
        healthyPercentText = findViewById(R.id.healthyPercentText)
    }

    private fun setupNavigation() {
        // Add touch animations to navigation buttons
        addButtonAnimation(previousMonthButton)
        addButtonAnimation(nextMonthButton)

        previousMonthButton.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            calculateMonthlyStats()
        }

        nextMonthButton.setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            calculateMonthlyStats()
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

    private fun calculateMonthlyStats() {
        // Update month display
        currentMonthText.text = monthYearFormat.format(currentCalendar.time)

        // Get first and last day of the month
        val firstDay = Calendar.getInstance().apply {
            timeInMillis = currentCalendar.timeInMillis
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val startDate = dateFormat.format(firstDay.time)

        val lastDay = Calendar.getInstance().apply {
            timeInMillis = currentCalendar.timeInMillis
            set(Calendar.DAY_OF_MONTH, currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        }
        val endDate = dateFormat.format(lastDay.time)
        val daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Debug log
        android.util.Log.d("MonthlySummary", "Start: $startDate, End: $endDate")

        // Get category counts from database
        mealViewModel.getCategoryCountsByDateRange(startDate, endDate) { categoryCounts ->
            runOnUiThread {
                displayStats(categoryCounts, startDate, endDate, daysInMonth)
            }
        }
    }

    private fun displayStats(categoryCounts: List<CategoryCount>, startDate: String, endDate: String, daysInMonth: Int) {
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

        // Get days tracked
        mealViewModel.getUniqueDatesInRange(startDate, endDate) { uniqueDates ->
            runOnUiThread {
                val daysTracked = uniqueDates.size
                daysTrackedText.text = "Days Tracked: $daysTracked/$daysInMonth"

                // Calculate perfect days (4 meals in a day)
                calculatePerfectDays(startDate, endDate)
            }
        }

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

            // Update achievements
            healthyPercentText.text = "💪 Healthy Meals: $healthyPercent%"
        } else {
            // No meals logged this month
            healthyStatsText.text = "🟢 Healthy: 0 (0%)"
            neutralStatsText.text = "🟡 Neutral: 0 (0%)"
            junkStatsText.text = "🔴 Junk: 0 (0%)"

            healthyProgressBar.progress = 0
            neutralProgressBar.progress = 0
            junkProgressBar.progress = 0

            healthyPercentText.text = "💪 Healthy Meals: 0%"
        }

        // Populate weekly breakdown
        populateWeeklyBreakdown(startDate, endDate)
    }

    private fun calculatePerfectDays(startDate: String, endDate: String) {
        mealViewModel.getMealsCountByDate(startDate, endDate) { dateCounts ->
            runOnUiThread {
                val perfectDays = dateCounts.count { it.count >= 4 }
                perfectDaysText.text = "📅 Perfect Days: $perfectDays days"
            }
        }
    }

    private fun populateWeeklyBreakdown(startDate: String, endDate: String) {
        // Clear existing views
        weeklyBreakdownContainer.removeAllViews()

        // Observe all meals to build weekly breakdown
        mealViewModel.allMeals.observe(this) { allMeals ->
            // Clear again in case of updates
            weeklyBreakdownContainer.removeAllViews()

            // Filter meals for this month
            val monthMeals = allMeals.filter { meal ->
                meal.date >= startDate && meal.date <= endDate
            }

            // Group by week
            val weeklyData = mutableMapOf<Int, MutableList<Meal>>()
            for (meal in monthMeals) {
                val mealDate = dateFormat.parse(meal.date)
                val cal = Calendar.getInstance()
                cal.time = mealDate!!
                val weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH)

                if (!weeklyData.containsKey(weekOfMonth)) {
                    weeklyData[weekOfMonth] = mutableListOf()
                }
                weeklyData[weekOfMonth]?.add(meal)
            }

            // Find best week
            var bestWeek = 1
            var maxMeals = 0

            // Display each week
            for (week in 1..5) {
                val weekMeals = weeklyData[week] ?: emptyList()
                val mealCount = weekMeals.size

                if (mealCount > maxMeals) {
                    maxMeals = mealCount
                    bestWeek = week
                }

                // Create row for this week
                val weekRow = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    setPadding(0, 8, 0, 8)
                }

                // Week label
                val weekLabel = TextView(this).apply {
                    text = "Week $week"
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.black, null))
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                }
                weekRow.addView(weekLabel)

                // Dots (7 days per week)
                val dotsText = TextView(this).apply {
                    val avgMealsPerDay = if (mealCount > 0) mealCount / 7.0 else 0.0
                    val dots = (1..7).map {
                        if (avgMealsPerDay >= 4) "●" else "○"
                    }.joinToString("")
                    text = dots
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.black, null))
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f
                    )
                }
                weekRow.addView(dotsText)

                // Meal count
                val countText = TextView(this).apply {
                    text = "($mealCount meals)"
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                weekRow.addView(countText)

                // Add row to container
                weeklyBreakdownContainer.addView(weekRow)
            }

            // Update best week text
            if (maxMeals > 0) {
                bestWeekText.text = "🌟 Best Week: Week $bestWeek ($maxMeals meals)"
            } else {
                bestWeekText.text = "🌟 Best Week: No data yet"
            }
        }
    }

    private fun animateProgressBar(progressBar: ProgressBar, targetProgress: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", 0, targetProgress)
        animator.duration = 1000 // 1 second animation
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }
}

