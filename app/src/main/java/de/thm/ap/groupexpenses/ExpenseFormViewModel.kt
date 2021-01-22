package de.thm.ap.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class ExpenseFormViewModel : ViewModel() {
    lateinit var groupId: String

    var expenseId: String? = null

    val date: MutableLiveData<Date> = MutableLiveData()

    init {
        date.value = Date()
    }
}