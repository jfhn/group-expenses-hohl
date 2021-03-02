package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * This class represents the document of a group payment in the firestore database.
 */
class GroupPayment {
    /**
     * The document id for the group payment in the firebase firestore database.
     *
     * firestore path: groups/$groupId/payments/$id
     */
    @DocumentId var id: String? = null

    /**
     * The document id for the user that made the payment in the firebase firestore database.
     *
     * firestore path: users/$userId
     */
    var userId: String? = null

    /**
     * The user name of the user that made the payment.
     */
    var userName: String? = null

    /**
     * The date the payment was made.
     */
    @ServerTimestamp var date: Date? = null

    /**
     * The amount of money of the payment.
     */
    var payment: Double = 0.0
}
