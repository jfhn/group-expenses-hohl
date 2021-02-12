package de.thm.ap.groupexpenses.worker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import de.thm.ap.groupexpenses.model.*
import java.io.ByteArrayOutputStream


object FirebaseWorker {
    private const val TAG = "FirebaseWorker"
    private val db = Firebase.firestore
    private val usersRef  = db.collection("users")
    private val groupsRef = db.collection("groups")
    private val usersGroupsRef = db.collection("usersGroups")

    const val ROLE_MEMBER = "member"
    const val ROLE_ADMIN  = "admin"

    fun userGroupsQuery(uid: String): Query = db
            .collection("users/$uid/groups")
            .orderBy("latestUpdate", Query.Direction.DESCENDING)

    fun userPaymentsQuery(uid: String): Query = db
            .collection("users/$uid/payments")
            .orderBy("date", Query.Direction.DESCENDING)

    fun userExpensesQuery(uid: String): Query = db
            .collection("users/$uid/expenses")
            .orderBy("date", Query.Direction.DESCENDING)

    fun groupMembersQuery(groupId: String): Query = db
            .collection("groups/$groupId/members")

    fun groupPaymentsQuery(groupId: String): Query = db
            .collection("groups/$groupId/payments")
            .orderBy("date", Query.Direction.DESCENDING)

    fun groupExpensesQuery(groupId: String): Query = db
            .collection("groups/$groupId/expenses")
            .orderBy("date", Query.Direction.ASCENDING)

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

    fun createGroup(name: String): Task<String> = Firebase.functions
            .getHttpsCallable("createGroup")
            .call(mapOf("name" to name))
            .continueWith { task ->
                task.result?.data as String // groupId
            }

    fun joinGroup(groupId: String, role: String): Task<String> = Firebase.functions
            .getHttpsCallable("joinGroup")
            .call(mapOf("groupId" to groupId, "role" to role))
            .continueWith { task ->
                task.result?.data as String // groupId
            }

    fun leaveGroup(groupId: String): Task<String> = Firebase.functions
            .getHttpsCallable("leaveGroup")
            .call(mapOf("groupId" to groupId))
            .continueWith { task ->
                task.result?.data as String // groupId
            }

    fun getExpense(groupId: String, expenseId: String): Task<GroupExpense> = db
            .document("groups/$groupId/expenses/$expenseId").get()
            .continueWith { it.result!!.toObject<GroupExpense>() }

    fun removeExpense(groupId: String, expenseId: String): Task<Void> = db
            .document("groups/$groupId/expenses/$expenseId").delete()

    fun updateExpense(groupId: String, expenseId: String, expense: GroupExpense): Task<Void> = db
            .document("groups/$groupId/expenses/$expenseId").set(expense)

    fun addExpense(groupId: String, expense: GroupExpense): Task<DocumentReference> = db
            .collection("groups/$groupId/expenses").add(expense)

    fun setExpense(groupId: String, expenseId: String?, expense: GroupExpense)
    : Task<DocumentReference> =
        if (expenseId != null) {
            val expenseRef = db.document("groups/$groupId/expenses/$expenseId")
            expenseRef.set(expense).continueWith { expenseRef }
        } else {
            db.collection("groups/$groupId/expenses").add(expense)
        }

    fun addPayment(groupId: String, payment: GroupPayment): Task<DocumentReference> = db
            .collection("groups/$groupId/payments").add(payment)

    fun getGroup(groupId: String): Task<Group> = db
            .document("groups/$groupId").get()
            .continueWith { it.result!!.toObject<Group>() }

    fun getGroupMember(groupId: String, memberId: String): Task<DocumentSnapshot> = db
            .document("groups/$groupId/members/$memberId").get()
}



