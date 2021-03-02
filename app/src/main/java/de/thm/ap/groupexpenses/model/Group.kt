package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * This class represents the document of a group in the firestore database.
 */
class Group {
    /**
     * The document id for the group in the firebase firestore database.
     *
     * firestore path: groups/$id
     */
    @DocumentId var id: String? = null

    /**
     * The group name.
     */
    var name: String? = null

    /**
     * The sum of all expenses made in the group.
     */
    var totalExpenses: Double = 0.0

    /**
     * The sum of all payments made in the group.
     */
    var totalPayments: Double = 0.0

    /**
     * The sum of all payments made by the current user.
     */
    var personalPayments: Double = 0.0

    /**
     * The latest update for the group.
     */
    @ServerTimestamp var latestUpdate: Date? = null

    /**
     * The number of members in the group.
     */
    var numMembers: Int = 0

    /**
     * Calculates the payment per user.
     *
     * @return total expenses divided by number of members
     */
    fun getPaymentPerUser(): Double = totalExpenses / numMembers

    /**
     * Calculates the personal balance.
     *
     * @return personal payments minus (total expenses divided by number of members)
     */
    fun getPersonalBalance(): Double = personalPayments - totalExpenses / numMembers

    /**
     * Calculates the total balance.
     *
     * @return total payments minus total expenses
     */
    fun getTotalBalance(): Double = totalPayments - totalExpenses
}
