package com.prajwalpai.mealtracker

import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class to manage a meal slot view
 * Handles showing/hiding meal content and empty state
 */
class MealSlotView(
    private val rootView: View,
    private val mealType: String,
    private val iconResId: Int
) {

    // UI Elements
    private val mealTypeIcon: ImageView = rootView.findViewById(R.id.mealTypeIcon)
    private val mealTypeLabel: TextView = rootView.findViewById(R.id.mealTypeLabel)
    private val categoryIndicator: View = rootView.findViewById(R.id.categoryIndicatorSlot)

    // Meal Content Layout
    private val mealContentLayout: LinearLayout = rootView.findViewById(R.id.mealContentLayout)
    private val mealNameSlot: TextView = rootView.findViewById(R.id.mealNameSlot)
    private val mealTimeSlot: TextView = rootView.findViewById(R.id.mealTimeSlot)
    private val categoryBadgeSlot: TextView = rootView.findViewById(R.id.categoryBadgeSlot)

    // Empty State Layout
    private val emptyStateLayout: LinearLayout = rootView.findViewById(R.id.emptyStateLayout)
    private val addMealSlotButton: Button = rootView.findViewById(R.id.addMealSlotButton)

    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    init {
        // Set meal type label and icon
        mealTypeLabel.text = mealType.uppercase()
        mealTypeIcon.setImageResource(iconResId)
    }
    
    /**
     * Show a meal in this slot
     */
    fun showMeal(meal: Meal, onClickListener: ((Meal) -> Unit)? = null) {
        // Hide empty state, show content
        emptyStateLayout.visibility = View.GONE
        mealContentLayout.visibility = View.VISIBLE
        categoryIndicator.visibility = View.VISIBLE

        // Set meal details
        mealNameSlot.text = meal.name
        mealTimeSlot.text = timeFormat.format(Date(meal.timestamp))
        categoryBadgeSlot.text = meal.category

        // Set category color
        val color = when (meal.category) {
            "Healthy" -> Color.parseColor("#4CAF50") // Green
            "Neutral" -> Color.parseColor("#9E9E9E") // Gray
            "Junk" -> Color.parseColor("#F44336")    // Red
            else -> Color.parseColor("#9E9E9E")
        }

        categoryIndicator.setBackgroundColor(color)
        categoryBadgeSlot.setBackgroundColor(color)

        // Animate the meal card sliding in
        val slideIn = AnimationUtils.loadAnimation(rootView.context, R.anim.slide_up)
        mealContentLayout.startAnimation(slideIn)

        // Set click listener for the entire meal content
        if (onClickListener != null) {
            mealContentLayout.setOnClickListener {
                onClickListener(meal)
            }
            // Make it look clickable
            mealContentLayout.isClickable = true
            mealContentLayout.isFocusable = true
        }
    }
    
    /**
     * Show empty state (no meal logged)
     */
    fun showEmpty() {
        // Hide content, show empty state
        mealContentLayout.visibility = View.GONE
        categoryIndicator.visibility = View.GONE
        emptyStateLayout.visibility = View.VISIBLE
    }
    
    /**
     * Set click listener for the add button
     */
    fun setOnAddClickListener(listener: View.OnClickListener) {
        addMealSlotButton.setOnClickListener(listener)
    }

    /**
     * Get the root view for animations
     */
    fun getRootView(): View {
        return rootView
    }
}

