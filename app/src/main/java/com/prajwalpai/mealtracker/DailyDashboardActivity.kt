package com.prajwalpai.mealtracker

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.prajwalpai.mealtracker.goals.StreakTracker
import com.prajwalpai.mealtracker.goals.StreakType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DailyDashboardActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var currentDateText: TextView
    private lateinit var previousDayButton: ImageButton
    private lateinit var nextDayButton: ImageButton
    private lateinit var addMealButton: Button
    private lateinit var viewSummaryButton: Button
    private lateinit var goalsButton: Button
    private lateinit var settingsButton: ImageButton

    // Streak Widget
    private lateinit var streakCount: TextView
    private lateinit var streakTitle: TextView
    private lateinit var streakSubtitle: TextView

    // Meal Slots
    private lateinit var breakfastSlot: MealSlotView
    private lateinit var lunchSlot: MealSlotView
    private lateinit var eveningSnacksSlot: MealSlotView
    private lateinit var dinnerSlot: MealSlotView

    // Data
    private lateinit var mealViewModel: MealViewModel
    private lateinit var streakTracker: StreakTracker
    private var currentDate: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_dashboard)

        // Initialize ViewModel
        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        // Initialize Streak Tracker
        streakTracker = StreakTracker(this)

        // Initialize UI
        initializeViews()

        // Set up navigation
        setupNavigation()

        // Load today's meals
        updateDateDisplay()
        loadMealsForCurrentDate()

        // Load streak data
        loadStreakData()
    }

    private fun initializeViews() {
        currentDateText = findViewById(R.id.currentDateText)
        previousDayButton = findViewById(R.id.previousDayButton)
        nextDayButton = findViewById(R.id.nextDayButton)
        addMealButton = findViewById(R.id.addMealButton)
        viewSummaryButton = findViewById(R.id.viewSummaryButton)
        goalsButton = findViewById(R.id.goalsButton)
        settingsButton = findViewById(R.id.settingsButton)

        // Streak widget
        streakCount = findViewById(R.id.streakCount)
        streakTitle = findViewById(R.id.streakTitle)
        streakSubtitle = findViewById(R.id.streakSubtitle)

        // Initialize meal slots with custom icons
        breakfastSlot = MealSlotView(findViewById(R.id.breakfastSlot), "Breakfast", R.drawable.ic_breakfast)
        lunchSlot = MealSlotView(findViewById(R.id.lunchSlot), "Lunch", R.drawable.ic_lunch)
        eveningSnacksSlot = MealSlotView(findViewById(R.id.eveningSnacksSlot), "Evening Snacks", R.drawable.ic_snacks)
        dinnerSlot = MealSlotView(findViewById(R.id.dinnerSlot), "Dinner", R.drawable.ic_dinner)

        // Set up add button click listeners for each slot
        setupSlotAddButtons()
    }

    private fun setupSlotAddButtons() {
        // When clicking add button in any slot, open the meal logger with pre-filled date and type
        breakfastSlot.setOnAddClickListener {
            openAddMealScreen("Breakfast")
        }

        lunchSlot.setOnAddClickListener {
            openAddMealScreen("Lunch")
        }

        eveningSnacksSlot.setOnAddClickListener {
            openAddMealScreen("Evening Snacks")
        }

        dinnerSlot.setOnAddClickListener {
            openAddMealScreen("Dinner")
        }
    }

    private fun openAddMealScreen(mealType: String? = null) {
        val intent = Intent(this, MainActivity::class.java)

        // Pass the current date being viewed
        if (mealType != null) {
            val dateString = dateFormat.format(currentDate.time)
            intent.putExtra("PRESET_DATE", dateString)
            intent.putExtra("PRESET_MEAL_TYPE", mealType)
        }

        startActivity(intent)
        // Add slide transition animation
        applyTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun setupNavigation() {
        // Add touch animations to all buttons
        addButtonAnimation(previousDayButton)
        addButtonAnimation(nextDayButton)
        addButtonAnimation(addMealButton)
        addButtonAnimation(viewSummaryButton)
        addButtonAnimation(goalsButton)
        addButtonAnimation(settingsButton)

        // Previous day
        previousDayButton.setOnClickListener {
            currentDate.add(Calendar.DAY_OF_MONTH, -1)
            updateDateDisplay()
            loadMealsForCurrentDate()
        }

        // Next day
        nextDayButton.setOnClickListener {
            currentDate.add(Calendar.DAY_OF_MONTH, 1)
            updateDateDisplay()
            loadMealsForCurrentDate()
        }

        // Add meal button
        addMealButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            applyTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Summary button - Navigate to Weekly Summary
        viewSummaryButton.setOnClickListener {
            val intent = Intent(this, WeeklySummaryActivity::class.java)
            startActivity(intent)
            applyTransition(R.anim.slide_up, R.anim.fade_out)
        }

        // Goals button - Navigate to Goals
        goalsButton.setOnClickListener {
            val intent = Intent(this, GoalsActivity::class.java)
            startActivity(intent)
            applyTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Settings button - Navigate to Settings
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            applyTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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

    private fun updateDateDisplay() {
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }

        val displayText = when {
            isSameDay(currentDate, today) -> "Today, ${displayDateFormat.format(currentDate.time)}"
            isSameDay(currentDate, yesterday) -> "Yesterday, ${displayDateFormat.format(currentDate.time)}"
            isSameDay(currentDate, tomorrow) -> "Tomorrow, ${displayDateFormat.format(currentDate.time)}"
            else -> displayDateFormat.format(currentDate.time)
        }

        currentDateText.text = displayText
    }

    private fun loadMealsForCurrentDate() {
        val dateString = dateFormat.format(currentDate.time)

        // Load meals for each type with stagger animation
        loadMealForType(dateString, "Breakfast", breakfastSlot, 0)
        loadMealForType(dateString, "Lunch", lunchSlot, 100)
        loadMealForType(dateString, "Evening Snacks", eveningSnacksSlot, 200)
        loadMealForType(dateString, "Dinner", dinnerSlot, 300)
    }

    private fun loadMealForType(date: String, type: String, slotView: MealSlotView, delayMs: Long = 0) {
        mealViewModel.getMealsByDateAndType(date, type).observe(this) { meals ->
            // Add stagger delay for smooth animation
            slotView.getRootView().postDelayed({
                if (meals.isNullOrEmpty()) {
                    slotView.showEmpty()
                } else {
                    // Show the most recent meal for this type
                    slotView.showMeal(meals[0]) { meal ->
                        // Open edit screen when meal is clicked
                        openEditScreen(meal)
                    }
                }
            }, delayMs)
        }
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    override fun onResume() {
        super.onResume()
        // Reload meals when returning from add meal screen
        loadMealsForCurrentDate()
        // Reload streak data
        loadStreakData()
    }

    private fun openEditScreen(meal: Meal) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("MEAL_ID", meal.id)
        startActivity(intent)
    }

    /**
     * Load and display streak data
     */
    private fun loadStreakData() {
        lifecycleScope.launch {
            // Initialize streaks if needed
            streakTracker.initializeStreaks()

            // Observe the healthy meals streak
            val database = MealDatabase.getDatabase(this@DailyDashboardActivity)
            database.streakDao().getStreakByTypeLiveData(StreakType.HEALTHY_MEALS)
                .observe(this@DailyDashboardActivity) { streak ->
                    if (streak != null) {
                        streakCount.text = streak.currentStreak.toString()
                        streakTitle.text = "Healthy Meals Streak"
                        streakSubtitle.text = if (streak.currentStreak > 0) {
                            "Keep it going! 🎉"
                        } else {
                            "Start your streak today!"
                        }
                    }
                }
        }
    }

    /**
     * Apply activity transition animation
     * Uses modern API for Android 14+ and legacy API for older versions
     */
    private fun applyTransition(enterAnim: Int, exitAnim: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 14+ (API 34+)
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                enterAnim,
                exitAnim
            )
        } else {
            // Legacy API for older versions
            @Suppress("DEPRECATION")
            overridePendingTransition(enterAnim, exitAnim)
        }
    }
}

