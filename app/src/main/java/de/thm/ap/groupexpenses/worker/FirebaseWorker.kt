package de.thm.ap.groupexpenses.worker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.app.TaskStackBuilder
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import de.thm.ap.groupexpenses.model.*
import java.io.ByteArrayOutputStream
import java.util.*


object FirebaseWorker {
    private const val TAG = "FirebaseWorker"
    private val db = Firebase.firestore
    private val usersRef  = db.collection("users")
    private val groupsRef = db.collection("groups")
    private val usersGroupsRef = db.collection("usersGroups")

    const val ROLE_MEMBER = "member"
    const val ROLE_ADMIN  = "admin"

    fun uploadImage(path: String, bitmap: Bitmap): UploadTask {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, baos)
        val data = baos.toByteArray()

        return Firebase.storage.reference.child(path).putBytes(data)
    }

    fun getImageUri(path: String): Task<Uri> {
        return Firebase.storage.reference.child(path).downloadUrl
    }

    fun downloadImage(path: String): Task<Bitmap> {
        return Firebase.storage.reference.child(path).getBytes(Long.MAX_VALUE).onSuccessTask {
            Tasks.call { BitmapFactory.decodeByteArray(it, 0, it!!.size) }
        }.addOnFailureListener {
            Tasks.forException<Bitmap>(it)
        }
    }

    fun createGroup(group: Group): Task<DocumentReference> {
        return groupsRef.add(group)
    }

    fun addGroupMember(
            groupRef: DocumentReference,
            user: FirebaseUser,
            role: String = ROLE_MEMBER
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

    fun getExpense(groupId: String, expenseId: String): Task<Expense> {
        return db.document("groups/$groupId/expenses/$expenseId")
                .get()
                .onSuccessTask { snapshot ->
                    Tasks.call { snapshot!!.toObject<Expense>() }
                }
    }

    fun removeExpense(groupId: String, expenseId: String): Task<Transaction> {
        val expenseRef = db.document("groups/$groupId/expenses/$expenseId")
        val groupRef   = db.document("groups/$groupId")

        return db.runTransaction { transaction ->
            val group: Group = transaction.get(groupRef).toObject()!!
            val expense: Expense = transaction.get(expenseRef).toObject()!!
            val newExpenses = group.expenses - expense.cost

            transaction.delete(expenseRef)
            transaction.update(groupRef, "expenses", newExpenses)
            transaction.update(groupRef, "latestUpdate", Date())
        }
    }

    fun updateExpense(groupId: String, expenseId: String, expense: Expense): Task<Transaction> {
        val expenseRef = db.document("groups/$groupId/expenses/$expenseId")
        val groupRef   = db.document("groups/$groupId")

        return db.runTransaction { transaction ->
            val group: Group = transaction.get(groupRef).toObject()!!
            val oldExpense: Expense = transaction.get(expenseRef).toObject()!!
            val newExpenses = group.expenses - oldExpense.cost + expense.cost

            transaction.set(expenseRef, expense)
            transaction.update(groupRef, "expenses", newExpenses)
            transaction.update(groupRef, "latestUpdate", Date())
        }
    }

    fun addExpense(groupId: String, expense: Expense): Task<Transaction> {
        val expenseRef = db.collection("groups/$groupId/expenses").document()
        val groupRef   = db.document("groups/$groupId")

        return db.runTransaction { transaction ->
            val group: Group = transaction.get(groupRef).toObject()!!
            val newExpenses = group.expenses + expense.cost

            transaction.set(expenseRef, expense)
            transaction.update(groupRef, "expenses", newExpenses)
            transaction.update(groupRef, "latestUpdate", Date())
        }
    }

    fun getGroupRef(groupId: String): DocumentReference {
        return groupsRef.document(groupId)
    }

    fun addPayment(groupId: String, user: FirebaseUser, payment: Double, date: Date?): Task<Transaction> {
        val groupRef        = groupsRef.document(groupId)
        val userPaymentRef  = db.collection("users/${user.uid}/payments").document()
        val groupPaymentRef = groupRef.collection("payments").document()

        return db.runTransaction { transaction ->
            val group: Group = transaction.get(groupRef).toObject()!!
            val userPayment = UserPayment().apply {
                this.groupId   = group.id
                this.groupName = group.name
                this.payment   = payment
                this.date      = date
            }

            val groupPayment = GroupPayment().apply {
                this.userId   = user.uid
                this.userName = user.displayName
                this.payment  = payment
                this.date     = date
            }

            group.apply {
                this.latestUpdate = null
                this.expenses    -= payment
            }

            transaction.set(groupRef, group)
            transaction.set(userPaymentRef, userPayment)
            transaction.set(groupPaymentRef, groupPayment)
        }
    }

    fun getGroup(groupId: String): Task<Group> {
        return db.document("groups/$groupId").get().onSuccessTask { snapshot ->
            Tasks.call { snapshot!!.toObject<Group>() }
        }
    }
}



