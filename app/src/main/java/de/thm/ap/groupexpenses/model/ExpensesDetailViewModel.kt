package de.thm.ap.groupexpenses.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ExpensesDetailViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var groupId: String    // from intent extras
    lateinit var expenseId: String  // from intent extras

    val image: MutableLiveData<Bitmap> = MutableLiveData()

    val expense: MutableLiveData<GroupExpense> = MutableLiveData()

    init {
        expense.value = null
    }
}