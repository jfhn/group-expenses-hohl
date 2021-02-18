package de.thm.ap.groupexpenses.ui.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.thm.ap.groupexpenses.model.Group

class GroupStatisticsViewModel : ViewModel() {
    val group: MutableLiveData<Group?> = MutableLiveData()

    init {
        group.value = null
    }
}