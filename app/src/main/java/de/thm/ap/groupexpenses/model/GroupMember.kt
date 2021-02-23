package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class GroupMember {
    @DocumentId var id: String? = null
    var userName: String? = null
    var role: String? = null
    @ServerTimestamp var joinDate: Date? = null // TODO(@Renke): unused var
    var totalExpenses: Double = 0.0
    var totalPayments: Double = 0.0
    @Exclude var personalBalance: Double = 0.0
}
