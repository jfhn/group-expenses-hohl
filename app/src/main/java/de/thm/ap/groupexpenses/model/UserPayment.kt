package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class UserPayment {
    var groupId: String? = null
    var groupName: String? = null
    @ServerTimestamp var date: Date? = null
    var payment: Double = 0.0
}
