package de.thm.ap.groupexpenses

import androidx.lifecycle.MutableLiveData
import java.util.*

class ExpenseFormViewModel {
    val date: MutableLiveData<Date> = MutableLiveData()

    init {
        date.value = Date()
    }
}