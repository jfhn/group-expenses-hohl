package de.thm.ap.groupexpenses.ui.achievements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import de.thm.ap.groupexpenses.model.Achievement
import de.thm.ap.groupexpenses.model.UserData

class AchievementsViewModel(app: Application): AndroidViewModel(app) {

    val userData: MutableLiveData<UserData> = MutableLiveData()
}
