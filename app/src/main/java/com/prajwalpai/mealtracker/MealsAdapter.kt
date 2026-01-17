package com.prajwalpai.mealtracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying meals in a RecyclerView
 */
class MealsAdapter(private var meals: List<Meal>) : RecyclerView.Adapter<MealsAdapter.MealViewHolder>() {
    
    /**
     * ViewHolder holds references to the views for each meal item
     */
    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealNameText: TextView = itemView.findViewById(R.id.mealNameText)
        val mealTypeText: TextView = itemView.findViewById(R.id.mealTypeText)
        val mealDateText: TextView = itemView.findViewById(R.id.mealDateText)
        val mealTimeText: TextView = itemView.findViewById(R.id.mealTimeText)
        val categoryIndicator: View = itemView.findViewById(R.id.categoryIndicator)
        val categoryBadge: TextView = itemView.findViewById(R.id.categoryBadge)
    }
    
    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.meal_item, parent, false)
        return MealViewHolder(view)
    }
    
    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]

        // Set meal name
        holder.mealNameText.text = meal.name

        // Set meal type
        holder.mealTypeText.text = meal.type

        // Format and set date
        holder.mealDateText.text = formatDate(meal.date)

        // Format and set time
        holder.mealTimeText.text = formatTime(meal.timestamp)

        // Set category badge and color
        holder.categoryBadge.text = meal.category

        val color = when (meal.category) {
            "Healthy" -> Color.parseColor("#4CAF50") // Green
            "Neutral" -> Color.parseColor("#9E9E9E") // Gray
            "Junk" -> Color.parseColor("#F44336")    // Red
            else -> Color.parseColor("#9E9E9E")
        }

        holder.categoryIndicator.setBackgroundColor(color)
        holder.categoryBadge.setBackgroundColor(color)
    }
    
    /**
     * Return the size of the dataset (invoked by the layout manager)
     */
    override fun getItemCount() = meals.size
    
    /**
     * Format date string to readable format
     */
    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)

            // Check if it's today, yesterday, or tomorrow
            val mealCalendar = java.util.Calendar.getInstance()
            mealCalendar.time = date ?: return dateString

            val today = java.util.Calendar.getInstance()
            val yesterday = java.util.Calendar.getInstance().apply { add(java.util.Calendar.DAY_OF_MONTH, -1) }
            val tomorrow = java.util.Calendar.getInstance().apply { add(java.util.Calendar.DAY_OF_MONTH, 1) }

            when {
                isSameDay(mealCalendar, today) -> "Today"
                isSameDay(mealCalendar, yesterday) -> "Yesterday"
                isSameDay(mealCalendar, tomorrow) -> "Tomorrow"
                else -> outputFormat.format(date)
            }
        } catch (e: Exception) {
            dateString
        }
    }

    /**
     * Format timestamp to time only
     */
    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Check if two calendars represent the same day
     */
    private fun isSameDay(cal1: java.util.Calendar, cal2: java.util.Calendar): Boolean {
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }

    /**
     * Update the meals list and refresh the RecyclerView
     */
    fun updateMeals(newMeals: List<Meal>) {
        meals = newMeals
        notifyDataSetChanged()
    }
}

