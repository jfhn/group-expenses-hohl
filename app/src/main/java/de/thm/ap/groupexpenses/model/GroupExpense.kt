package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class GroupExpense {
    /**
     * Id of the expense document
     */
    @DocumentId
    var id: String? = null

    /**
     * name/purpose of the expense
     */
    var name: String? = null

    /**
     * cost of the expense
     */
    var cost: Double = 0.0

    /**
     * date of the expense
     */
    @ServerTimestamp
    var date: Date? = null

    /**
     * id of the user, who made the expense
     */
    var userId: String? = null

    /**
     * name of the user, who made the expense
     */
    var userName: String? = null
}
