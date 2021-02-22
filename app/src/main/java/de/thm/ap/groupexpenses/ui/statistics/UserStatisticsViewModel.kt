package de.thm.ap.groupexpenses.ui.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserStatisticsViewModel: ViewModel() {

    var expenses: MutableLiveData<Double> = MutableLiveData()
    var payments: MutableLiveData<Double> = MutableLiveData()
}