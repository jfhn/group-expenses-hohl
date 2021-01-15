package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Group {
    @DocumentId var id: String? = null
    var name: String? = null
    var expenses: Double = 0.0
    @ServerTimestamp var latestUpdate: Date? = Date()
    var members: List<String>? = null
}
