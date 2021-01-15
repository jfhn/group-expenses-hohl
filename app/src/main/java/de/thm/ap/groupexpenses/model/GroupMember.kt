package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId

class GroupMember {
    @DocumentId
    var userId: String? = null
    var userName: String? = null
    var role: String? = null
}
