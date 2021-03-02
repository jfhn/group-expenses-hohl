package de.thm.ap.groupexpenses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.thm.ap.groupexpenses.databinding.ActivityAchievementsBinding

/**
 * This activity is the parent to the achievements fragment.
 *
 * @see de.thm.ap.groupexpenses.ui.achievements.AchievementsFragment
 */
class AchievementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.achievements_title)
    }
}