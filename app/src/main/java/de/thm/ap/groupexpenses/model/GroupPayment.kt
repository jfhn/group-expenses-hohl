package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class GroupPayment {
    var userId: String? = null
    var userName: String? = null
    @ServerTimestamp var date: Date? = null
    var payment: Double = 0.0
}
