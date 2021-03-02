package de.thm.ap.groupexpenses.ui.achievements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import de.thm.ap.groupexpenses.model.UserData

/**
 * This class contains the UI related data for the achievements fragment.
 *
 * @param app the current application state.
 *
 * @see AchievementsFragment
 */
class AchievementsViewModel(app: Application): AndroidViewModel(app) {
    /**
     * Contains the relevant data to calculate the achievements.
     * It is mutable live data to automatically use the newest data.
     *
     * @see de.thm.ap.groupexpenses.model.UserData
     * @see androidx.lifecycle.MutableLiveData
     */
    val userData: MutableLiveData<UserData> = MutableLiveData()
}
