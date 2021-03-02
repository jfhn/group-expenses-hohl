package de.thm.ap.groupexpenses

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import de.thm.ap.groupexpenses.model.GroupExpense

/**
 * This class contains the UI related data for the expense detail activity.
 *
 * @param app the current application state.
 *
 * @see ExpensesDetailActivity
 */
class ExpensesDetailViewModel(app: Application) : AndroidViewModel(app) {
    /**
     * Contains the group id in the firebase firestore database.
     * This value is provided by the intent extras.
     *
     * firestore path: groups/$groupId
     */
    lateinit var groupId: String

    /**
     * Contains the expense id in the firebase firestore database.
     * This value is provided by the intent extras.
     *
     * firestore path: groups/$groupId/expenses/$expenseId
     */
    lateinit var expenseId: String

    /**
     * Contains a bitmap that represents a receipt to an expense.
     * Data changes in the backend will be reflected in real time.
     *
     * firebase path: storage/images/
     */
    val image: MutableLiveData<Bitmap> = MutableLiveData()

    /**
     * Contains the data for the expense.
     * Data changes in the backend will be reflected in real time.
     *
     * firestore path: groups/$groupId/expenses/$expenseId/
     */
    val expense: MutableLiveData<GroupExpense> = MutableLiveData()

    init {
        expense.value = null
    }
}