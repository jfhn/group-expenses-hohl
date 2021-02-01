package de.thm.ap.groupexpenses.ui.achievements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.thm.ap.groupexpenses.model.Achievement

class AchievementsViewModel(app: Application): AndroidViewModel(app) {

    val achievements: LiveData<List<Achievement>> = MutableLiveData() // TODO init from db
}