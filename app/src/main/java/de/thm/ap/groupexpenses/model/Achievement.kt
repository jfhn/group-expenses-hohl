package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId

class Achievement {
    /**
     * Id of the achievement document
     */
    @DocumentId
    var id: String? = null

    /**
     * The name of the achievement
     */
    var name: String? = null

    override fun toString(): String {
        return name ?: "???"
    }
}