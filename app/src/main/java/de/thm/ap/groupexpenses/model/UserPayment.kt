package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * This class represents the document of a user payment in the firestore database.
 */
class UserPayment {
    /**
     * The document id for the group where the payment was made in the firebase firestore
     * database.
     *
     * firestore path: groups/$groupId
     */
    var groupId: String? = null

    /**
     * The group name.
     */
    var groupName: String? = null

    /**
     * The date the payment was made.
     */
    @ServerTimestamp var date: Date? = null

    /**
     * The amount of money of the payment.
     */
    var payment: Double = 0.0
}
