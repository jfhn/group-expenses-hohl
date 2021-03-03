package de.thm.ap.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

/**
 * This class contains the UI related data for the expense form activity.
 *
 * @see ExpenseFormActivity
 */
class ExpenseFormViewModel : ViewModel() {
    /**
     * Contains the document id for the group in the firebase firestore database.
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
    var expenseId: String? = null

    /**
     * Contains the current date.
     */
    val date: MutableLiveData<Date> = MutableLiveData()

    init {
        date.value = Date()
    }
}