package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

/**
 * This class represents the document of a group member in the firestore database.
 */
class GroupMember {
    /**
     * The document id for the group member in the firestore database.
     *
     * firestore path: groups/$groupId/members/$id
     */
    @DocumentId var id: String? = null

    /**
     * The user name of the group member.
     */
    var userName: String? = null

    /**
     * The role of the user.
     */
    var role: String? = null

    /**
     * The sum of all expenses that the user has made in groups.
     */
    var totalExpenses: Double = 0.0

    /**
     * The sum of all payments that the user has made in its groups.
     */
    var totalPayments: Double = 0.0

    /**
     * The personal balance.
     *
     * @see Group.getPersonalBalance
     */
    @Exclude var personalBalance: Double = 0.0
}
