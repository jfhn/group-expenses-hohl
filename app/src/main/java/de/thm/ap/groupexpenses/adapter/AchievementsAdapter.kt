package de.thm.ap.groupexpenses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.thm.ap.groupexpenses.databinding.ItemAchievementBinding
import de.thm.ap.groupexpenses.model.Achievement

/**
 * The achievements adapter, containing the achievements data to be displayed.
 *
 * @param achievements the achievements to be displayed.
 */
class AchievementsAdapter(private val achievements: List<Achievement>)
    : RecyclerView.Adapter<AchievementsAdapter.AchievementsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementsViewHolder {
        val binding = ItemAchievementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return AchievementsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementsViewHolder, position: Int) {
        val achievement = this.achievements[position]

        holder.bind(achievement)
    }

    override fun getItemCount(): Int = achievements.size

    inner class AchievementsViewHolder(private val binding: ItemAchievementBinding)
        : RecyclerView.ViewHolder(binding.root) {

        private val resources = binding.root.resources
        private val type      = binding.textAchievementType
        private val name      = binding.textAchievementName
        private val desc      = binding.textAchievementDescription

        fun bind(achievement: Achievement) {
            type.text = achievement.getType(resources)
            name.text = achievement.getName(resources)
            desc.text = achievement.getDescription(resources)

            binding.achievement = achievement
        }
    }
}