package de.thm.ap.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

/**
 * This class contains the UI related data for the payment form activity.
 *
 * @see PaymentFormActivity
 */
class PaymentFormViewModel : ViewModel() {
    /**
     * Contains the group id in the firebase firestore database.
     * This value is provided by the intent extras.
     *
     * firestore path: groups/$groupId
     */
    lateinit var groupId: String

    /**
     * Contains the current date.
     */
    val date: MutableLiveData<Date> = MutableLiveData()

    init {
        date.value = Date()
    }
}
