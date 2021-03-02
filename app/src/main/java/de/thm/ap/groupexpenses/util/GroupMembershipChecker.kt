package de.thm.ap.groupexpenses.util

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.model.GroupMember
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupMembersQuery

/**
 * This class is responsible to return from the group activity if the current user was
 * kicked by the group admin.
 *
 * @param groupId  the document id for the group in the firebase firestore database
 * @param onKicked the function object that gets invoked when the client was kicked
 */
class GroupMembershipChecker(private val groupId: String,
                             private val onKicked: () -> Unit) : EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null

    fun startListening() {
        if (this.registration == null) {
            this.registration = groupMembersQuery(groupId).addSnapshotListener(this)
        }
    }

    fun stopListening() {
        this.registration?.remove()
        this.registration = null
    }

    override fun onEvent(documentSnapshots: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null || documentSnapshots == null) {
            return
        }

        for (change in documentSnapshots.documentChanges) {
            if (change.type == DocumentChange.Type.REMOVED) {
                val groupMember: GroupMember = change.document.toObject()

                if (groupMember.id == Firebase.auth.currentUser!!.uid) {
                    this.onKicked.invoke()
                }
            }
        }
    }
}