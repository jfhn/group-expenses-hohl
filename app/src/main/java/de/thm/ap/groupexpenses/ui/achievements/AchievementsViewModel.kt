package de.thm.ap.groupexpenses.ui.achievements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.thm.ap.groupexpenses.model.Achievement

class AchievementsViewModel(app: Application): AndroidViewModel(app) {

    val achievements: LiveData<List<Achievement>> = MutableLiveData(mutableListOf(Achievement().also {
        it.name = "Dummy achievementA"
    }, Achievement().also {
        it.name = "Dummy achievementB"
    })) // TODO init from db and remove dummies
}
