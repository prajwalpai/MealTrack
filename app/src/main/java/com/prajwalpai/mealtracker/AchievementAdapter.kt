package com.prajwalpai.mealtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prajwalpai.mealtracker.goals.Achievement

/**
 * Adapter for displaying achievements in a grid
 */
class AchievementAdapter(
    private val achievements: List<Achievement>
) : RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    class AchievementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.achievementIcon)
        val name: TextView = view.findViewById(R.id.achievementName)
        val description: TextView = view.findViewById(R.id.achievementDescription)
        val lockedOverlay: View = view.findViewById(R.id.lockedOverlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievements[position]

        holder.icon.text = achievement.icon
        holder.name.text = achievement.name
        holder.description.text = achievement.description

        // Show locked overlay if not unlocked
        if (!achievement.isUnlocked) {
            holder.lockedOverlay.visibility = View.VISIBLE
            holder.icon.alpha = 0.3f
            holder.name.alpha = 0.5f
            holder.description.alpha = 0.5f
        } else {
            holder.lockedOverlay.visibility = View.GONE
            holder.icon.alpha = 1.0f
            holder.name.alpha = 1.0f
            holder.description.alpha = 1.0f
        }
    }

    override fun getItemCount() = achievements.size
}

