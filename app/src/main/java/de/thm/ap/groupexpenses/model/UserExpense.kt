package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class UserExpense { // TODO(@Renke): unused class (and vars)
    @DocumentId var id: String? = null
    var name: String? = null
    var groupId: String? = null
    var groupName: String? = null
    @ServerTimestamp var date: Date? = null
    var cost: Double = 0.0
}