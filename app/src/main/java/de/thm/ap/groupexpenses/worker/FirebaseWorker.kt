package de.thm.ap.groupexpenses.worker

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.model.Expense
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.model.GroupMember
import de.thm.ap.groupexpenses.model.GroupPayment
import de.thm.ap.groupexpenses.model.UserPayment
import java.util.*


object FirebaseWorker {
    private const val TAG = "FirebaseWorker"
    private val db = Firebase.firestore
    private val usersRef  = db.collection("users")
    private val groupsRef = db.collection("groups")
    private val usersGroupsRef = db.collection("usersGroups")

    const val ROLE_MEMBER = "member"
    const val ROLE_ADMIN  = "admin"

    fun getUsersGroupsQuery(user: FirebaseUser): Query {
        return groupsRef
                .whereArrayContains("members", user.uid)
                .orderBy("latestUpdate", Query.Direction.DESCENDING)
    }

    fun createGroup(group: Group): Task<DocumentReference> {
        return groupsRef.add(group)
    }

    fun addGroupMember(
            groupRef: DocumentReference,
            user: FirebaseUser,
            role: String
    ): Task<Transaction> {
        val groupMember = GroupMember().apply {
            this.userName = user.displayName
            this.role = role
        }

        val groupMemberRef = groupRef.collection("members").document(user.uid)

        return db.runTransaction { transaction ->
            transaction.update(groupRef, "members", FieldValue.arrayUnion(user.uid))
            transaction.set(groupMemberRef, groupMember)
        }
    }

    fun getGroupRef(groupId: String): DocumentReference {
        return groupsRef.document(groupId)
    }

    fun addExpense(groupRef: DocumentReference, expense: Expense): Task<Transaction> {
        val expenseRef = groupRef.collection("expenses").document()

        return db.runTransaction { transaction ->
            val group: Group = transaction.get(groupRef).toObject()!!
            val newExpenses = group.expenses + expense.cost

            transaction.set(expenseRef, expense)
            transaction.update(groupRef, "expenses", newExpenses)
            transaction.update(groupRef, "latestUpdate", Date())
        }
    }

    fun addPayment(groupRef: DocumentReference, user: FirebaseUser, payment: Double) {
        val userPaymentRef  = usersRef.document(user.uid).collection("payments").document()
        val groupPaymentRef = groupRef.collection("payments").document()

        db.runTransaction { transaction ->
            val group: Group = transaction.get(groupRef).toObject()!!
            val userPayment = UserPayment().apply {
                this.groupId = group.id
                this.groupName = group.name
                this.payment = payment
            }

            val groupPayment = GroupPayment().apply {
                this.userId = user.uid
                this.userName = user.displayName
                this.payment = payment
            }

            group.apply {
                this.latestUpdate = null
                this.expenses -= payment
            }

            transaction.set(groupRef, group)
            transaction.set(userPaymentRef, userPayment)
            transaction.set(groupPaymentRef, groupPayment)
        }
    }
}



