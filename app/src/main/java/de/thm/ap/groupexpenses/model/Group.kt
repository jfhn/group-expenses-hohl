package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Group {
    var name: String? = null
    var numOfMembers: Int = 0
    var expenses: Double = 0.0
    @ServerTimestamp var latestUpdate: Date? = Date()
}
