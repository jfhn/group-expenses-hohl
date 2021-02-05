package de.thm.ap.groupexpenses.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ExpensesDetailViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var groupId: String    // from intent extras
    lateinit var expenseId: String  // from intent extras
    val expense: MutableLiveData<Expense> = MutableLiveData()

    init {
        expense.value = null
    }
}