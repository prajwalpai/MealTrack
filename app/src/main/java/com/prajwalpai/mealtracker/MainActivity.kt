package com.prajwalpai.mealtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.app.DatePickerDialog
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.ViewModelProvider
import com.prajwalpai.mealtracker.widget.WidgetUpdateHelper

class MainActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var mealNameInput: EditText
    private lateinit var dateSelector: TextView
    private lateinit var mealTypeSpinner: Spinner
    private lateinit var healthyButton: Button
    private lateinit var neutralButton: Button
    private lateinit var junkButton: Button
    private lateinit var saveMealButton: Button
    private lateinit var deleteMealButton: Button

    // Data
    private var selectedCategory: String = ""
    private var selectedDate: Calendar = Calendar.getInstance()
    private lateinit var mealViewModel: MealViewModel
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // Edit mode
    private var isEditMode = false
    private var editingMealId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        mealViewModel = ViewModelProvider(this).get(MealViewModel::class.java)

        // Check if we're in edit mode
        checkEditMode()

        // Initialize UI elements
        initializeViews()

        // Set up date selector
        setupDateSelector()

        // Set up the meal type spinner
        setupMealTypeSpinner()

        // Set up category buttons
        setupCategoryButtons()

        // Set up save button
        setupSaveButton()

        // If edit mode, load the meal data
        if (isEditMode) {
            loadMealForEditing()
        }
    }

    private fun checkEditMode() {
        // Check if meal ID was passed via intent
        editingMealId = intent.getLongExtra("MEAL_ID", -1L)
        isEditMode = editingMealId != -1L

        // Update title based on mode
        title = if (isEditMode) "Edit Meal" else "Add Meal"

        // Check for preset date and meal type (from slot add buttons)
        if (!isEditMode) {
            val presetDate = intent.getStringExtra("PRESET_DATE")
            val presetMealType = intent.getStringExtra("PRESET_MEAL_TYPE")

            if (presetDate != null) {
                try {
                    val date = dateFormat.parse(presetDate)
                    if (date != null) {
                        selectedDate.time = date
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (presetMealType != null) {
                // Store it to set after spinner is initialized
                intent.putExtra("PRESET_MEAL_TYPE_TO_SET", presetMealType)
            }
        }
    }

    private fun initializeViews() {
        mealNameInput = findViewById(R.id.mealNameInput)
        dateSelector = findViewById(R.id.dateSelector)
        mealTypeSpinner = findViewById(R.id.mealTypeSpinner)
        healthyButton = findViewById(R.id.healthyButton)
        neutralButton = findViewById(R.id.neutralButton)
        junkButton = findViewById(R.id.junkButton)
        saveMealButton = findViewById(R.id.saveMealButton)
        deleteMealButton = findViewById(R.id.deleteMealButton)

        // Set initial date to today
        updateDateDisplay()

        // Show delete button only in edit mode
        if (isEditMode) {
            deleteMealButton.visibility = android.view.View.VISIBLE
            setupDeleteButton()
        }
    }

    private fun setupDateSelector() {
        dateSelector.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val year = selectedDate.get(Calendar.YEAR)
        val month = selectedDate.get(Calendar.MONTH)
        val day = selectedDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the selected date
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                updateDateDisplay()
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun updateDateDisplay() {
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }

        val displayText = when {
            isSameDay(selectedDate, today) -> "Today (${displayDateFormat.format(selectedDate.time)})"
            isSameDay(selectedDate, yesterday) -> "Yesterday (${displayDateFormat.format(selectedDate.time)})"
            isSameDay(selectedDate, tomorrow) -> "Tomorrow (${displayDateFormat.format(selectedDate.time)})"
            else -> displayDateFormat.format(selectedDate.time)
        }

        dateSelector.text = displayText
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    private fun setupMealTypeSpinner() {
        // Create adapter for spinner using the meal_types array from strings.xml
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.meal_types,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mealTypeSpinner.adapter = adapter

        // Set preset meal type if provided (from slot add button)
        val presetMealType = intent.getStringExtra("PRESET_MEAL_TYPE_TO_SET")
        if (presetMealType != null && !isEditMode) {
            val mealTypes = resources.getStringArray(R.array.meal_types)
            val position = mealTypes.indexOf(presetMealType)
            if (position >= 0) {
                mealTypeSpinner.setSelection(position)
            }
        }
    }

    private fun setupCategoryButtons() {
        // Healthy button click
        healthyButton.setOnClickListener {
            selectCategory("Healthy")
            updateCategoryButtonsUI()
        }

        // Neutral button click
        neutralButton.setOnClickListener {
            selectCategory("Neutral")
            updateCategoryButtonsUI()
        }

        // Junk button click
        junkButton.setOnClickListener {
            selectCategory("Junk")
            updateCategoryButtonsUI()
        }
    }

    private fun selectCategory(category: String) {
        selectedCategory = category
    }

    private fun updateCategoryButtonsUI() {
        // Reset all buttons to default opacity
        healthyButton.alpha = 0.5f
        neutralButton.alpha = 0.5f
        junkButton.alpha = 0.5f

        // Highlight selected button
        when (selectedCategory) {
            "Healthy" -> healthyButton.alpha = 1.0f
            "Neutral" -> neutralButton.alpha = 1.0f
            "Junk" -> junkButton.alpha = 1.0f
        }
    }

    private fun setupSaveButton() {
        saveMealButton.setOnClickListener {
            saveMeal()
        }
    }

    private fun setupDeleteButton() {
        deleteMealButton.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Meal")
            .setMessage("Are you sure you want to delete this meal?")
            .setPositiveButton("Delete") { _, _ ->
                deleteMeal()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteMeal() {
        // Find the meal and delete it
        mealViewModel.allMeals.observe(this) { meals ->
            val mealToDelete = meals.find { it.id == editingMealId }

            if (mealToDelete != null) {
                mealViewModel.delete(mealToDelete)
                Toast.makeText(this, "Meal deleted! 🗑️", Toast.LENGTH_SHORT).show()

                // Update widgets
                WidgetUpdateHelper.updateWidgets(this)

                finish()
            }
        }
    }

    private fun saveMeal() {
        // Get meal name
        val mealName = mealNameInput.text.toString().trim()

        // Get selected meal type
        val mealType = mealTypeSpinner.selectedItem.toString()

        // Validate input
        if (mealName.isEmpty()) {
            Toast.makeText(this, "Please enter a meal name", Toast.LENGTH_SHORT).show()
            return
        }

        if (mealType == "Select Meal Type") {
            Toast.makeText(this, "Please select a meal type", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedCategory.isEmpty()) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
            return
        }

        if (isEditMode) {
            // Update existing meal
            val meal = Meal(
                id = editingMealId,
                name = mealName,
                type = mealType,
                category = selectedCategory,
                date = dateFormat.format(selectedDate.time)
            )

            mealViewModel.update(meal)
            Toast.makeText(this, "Meal updated! ✅", Toast.LENGTH_SHORT).show()

            // Update widgets
            WidgetUpdateHelper.updateWidgets(this)

            // Go back to previous screen
            finish()
        } else {
            // Create new meal
            val meal = Meal(
                name = mealName,
                type = mealType,
                category = selectedCategory,
                date = dateFormat.format(selectedDate.time)
            )

            // Save to database using ViewModel
            mealViewModel.insert(meal)

            // Update widgets
            WidgetUpdateHelper.updateWidgets(this)

            // Clear form
            clearForm()

            // Show success message
            Toast.makeText(this, "Meal saved! 🎉", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        mealNameInput.text.clear()
        selectedDate = Calendar.getInstance() // Reset to today
        updateDateDisplay()
        mealTypeSpinner.setSelection(0)
        selectedCategory = ""
        updateCategoryButtonsUI()
    }

    private fun loadMealForEditing() {
        // Observe all meals and find the one we're editing
        mealViewModel.allMeals.observe(this) { meals ->
            val mealToEdit = meals.find { it.id == editingMealId }

            if (mealToEdit != null) {
                // Populate form with existing data
                mealNameInput.setText(mealToEdit.name)

                // Set date
                try {
                    val date = dateFormat.parse(mealToEdit.date)
                    if (date != null) {
                        selectedDate.time = date
                        updateDateDisplay()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                // Set meal type
                val mealTypes = resources.getStringArray(R.array.meal_types)
                val typePosition = mealTypes.indexOf(mealToEdit.type)
                if (typePosition >= 0) {
                    mealTypeSpinner.setSelection(typePosition)
                }

                // Set category
                selectedCategory = mealToEdit.category
                updateCategoryButtonsUI()
            }
        }
    }

    override fun finish() {
        super.finish()
        // Add slide back animation
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}
