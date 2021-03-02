package de.thm.ap.groupexpenses.ui.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This class contains the UI related data for the user statistics fragment.
 *
 * @see UserStatisticsFragment
 */
class UserStatisticsViewModel: ViewModel() {
    /**
     * Contains the user expenses.
     * Data changes in the backend will be reflected in real time.
     */
    var expenses: MutableLiveData<Double> = MutableLiveData()

    /**
     * Contains the user payments.
     * Data changes in the backend will be reflected in real time.
     */
    var payments: MutableLiveData<Double> = MutableLiveData()
}
