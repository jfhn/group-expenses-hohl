package de.thm.ap.groupexpenses.worker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.model.GroupExpense
import de.thm.ap.groupexpenses.model.GroupPayment
import java.io.ByteArrayOutputStream

/**
 * This object contains all needed functions to communicate with the firebase firestore.
 */
object FirebaseWorker {

    const val ROLE_MEMBER = "member"

    /**
     * Gets all the groups the user with the specified id is in.
     * The groups will be ordered by the latest update.
     *
     * @param uid the document id for the user in the firebase firestore database
     *
     * @return the resulting query object
     */
    fun userGroupsQuery(uid: String): Query = Firebase.firestore
            .collection("users/$uid/groups")
            .orderBy("latestUpdate", Query.Direction.DESCENDING)

    /**
     * Gets all the groups the user with the specified id is in.
     * The groups will be ordered by their name.
     *
     * @param uid the document id for the user in the firebase firestore database
     *
     * @return the resulting query object
     */
    fun userGroupsStatsQuery(uid: String): Query = Firebase.firestore
            .collection("users/$uid/groups")
            .orderBy("name", Query.Direction.ASCENDING)

    /**
     * Gets all payments made by the user with the specified id.
     * The payments will be ordered by their date.
     *
     * @param uid the document id for the user in the firebase firestore database
     *
     * @return the resulting query object
     */
    fun userPaymentsQuery(uid: String): Query = Firebase.firestore
            .collection("users/$uid/payments")
            .orderBy("date", Query.Direction.DESCENDING)

    /**
     * Gets all members of the group with the specified id.
     *
     * @param groupId the document id for the group in the firebase firestore database
     *
     * @return the resulting query object
     */
    fun groupMembersQuery(groupId: String): Query = Firebase.firestore
            .collection("groups/$groupId/members")

    /**
     * Gets all payments made in the group with the specified id.
     *
     * @param groupId the document id for the group in the firebase firestore database
     *
     * @return the resulting query object
     */
    fun groupPaymentsQuery(groupId: String): Query = Firebase.firestore
            .collection("groups/$groupId/payments")
            .orderBy("date", Query.Direction.DESCENDING)

    /**
     * Gets all expenses made in the group with the specified id.
     *
     * @param groupId the document id for the group in the firebase firestore database
     *
     * @return the resulting query object
     */
    fun groupExpensesQuery(groupId: String): Query = Firebase.firestore
            .collection("groups/$groupId/expenses")
            .orderBy("date", Query.Direction.ASCENDING)

    /**
     * Creates a bitmap and uploads it in the firebase storage.
     *
     * @param path   the path to the image in the firebase storage
     * @param bitmap the bitmap to be filled
     *
     * @return the resulting upload task
     */
    fun uploadImage(path: String, bitmap: Bitmap): UploadTask {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, baos)
        val data = baos.toByteArray()

        return Firebase.storage.reference.child(path).putBytes(data)
    }

    /**
     * Downloads an image from the firebase storage.
     *
     * @param path the path to the image in the firebase storage
     *
     * @return the resulting task containing the bitmap
     */
    fun downloadImage(path: String): Task<Bitmap> {
        return Firebase.storage.reference.child(path).getBytes(Long.MAX_VALUE).onSuccessTask {
            Tasks.call { BitmapFactory.decodeByteArray(it, 0, it!!.size) }
        }.addOnFailureListener {
            Tasks.forException<Bitmap>(it)
        }
    }

    /**
     * Calls the firebase server function "createGroup" with the specified group name.
     *
     * @param name the name for the new group
     *
     * @return the resulting task containing the document id for the group in the firebase firestore database
     */
    fun createGroup(name: String): Task<String> = Firebase.functions
            .getHttpsCallable("createGroup")
            .call(mapOf("name" to name))
            .continueWith { task ->
                task.result?.data as String // groupId
            }

    /**
     * Calls the firebase server function "joinGroup" with the specified group id and role.
     *
     * @param groupId the document id for the group in the firebase firestore database
     * @param role    the member role
     *
     * @return the resulting task containing the document id for the group in the firebase firestore database
     */
    fun joinGroup(groupId: String, role: String): Task<String> = Firebase.functions
            .getHttpsCallable("joinGroup")
            .call(mapOf("groupId" to groupId, "role" to role))
            .continueWith { task ->
                task.result?.data as String // groupId
            }

    /**
     * Calls the firebase server function "leaveGroup" with the specified group id.
     *
     * @param groupId the document id for the group in the firebase firestore database
     *
     * @return the resulting task containing the document id for the group in the firebase firestore database
     */
    fun leaveGroup(groupId: String): Task<String> = Firebase.functions
            .getHttpsCallable("leaveGroup")
            .call(mapOf("groupId" to groupId))
            .continueWith { task ->
                task.result?.data as String // groupId
            }

    /**
     * Calls the firebase server function "kickMemberFromGroup" with the specified group id
     * and memberId.
     *
     * @param groupId  the document id for the group in the firebase firestore database
     * @param memberId the document id for the group member in the firebase firestore database
     *
     * @return the resulting task containing the document id for the group in the firebase firestore database
     */
    fun kickMemberFromGroup(groupId: String, memberId: String): Task<String> = Firebase.functions
        .getHttpsCallable("kickMemberFromGroup")
        .call(mapOf("groupId" to groupId, "memberId" to memberId))
        .continueWith { task ->
            task.result?.data as String // groupId
        }

    /**
     * Gets a specific expense identified by the document id for the group in the firebase
     * firestore database and the document id for the expense in the firebase firestore.
     *
     * @param groupId   the document id for the group in the firebase firestore database
     * @param expenseId the document id for the group expense in the firebase firestore
     *
     * @return the resulting task containing a group expense object
     *
     * @see GroupExpense
     */
    fun getExpense(groupId: String, expenseId: String): Task<GroupExpense> = Firebase.firestore
            .document("groups/$groupId/expenses/$expenseId").get()
            .continueWith { it.result!!.toObject<GroupExpense>() }

    /**
     * Removes a specific expense identified by the document id for the expense in the group
     * identified by the document id for the firebase firestore.
     *
     * @param groupId   the document id for the group in the firebase firestore database
     * @param expenseId the document id for the group expense in the firebase firestore
     *
     * @return the resulting task containing nothing
     */
    fun removeExpense(groupId: String, expenseId: String): Task<Void> = Firebase.firestore
            .document("groups/$groupId/expenses/$expenseId").delete()

    /**
     * Sets a specific group expense identified by its document id in a specific group
     * identified by its document id to the provided group expense.
     *
     * @param groupId   the document id for the group in the firebase firestore database
     * @param expenseId the document id for the group expense in the firebase firestore
     * @param expense   the new expense
     *
     * @return the resulting task containing the document reference with the updated expense
     */
    fun setExpense(groupId: String, expenseId: String?, expense: GroupExpense)
    : Task<DocumentReference> =
        if (expenseId != null) {
            val expenseRef = Firebase.firestore.document("groups/$groupId/expenses/$expenseId")
            expenseRef.set(expense).continueWith { expenseRef }
        } else {
            Firebase.firestore.collection("groups/$groupId/expenses").add(expense)
        }

    /**
     * Adds a group payment to a group identified by its document id in the firebase firestore.
     *
     * @param groupId the document id for the group in the firebase firestore database
     * @param payment the new payment
     *
     * @return the resulting task containing the created document reference to the newly
     *         created payment
     */
    fun addPayment(groupId: String, payment: GroupPayment): Task<DocumentReference> = Firebase.firestore
            .collection("groups/$groupId/payments").add(payment)

    /**
     * Deletes a specific group payment from a group identified by its document id in the
     * firebase firestore.
     *
     * @param groupId the document id for the group in the firebase firestore database
     * @param payment the new payment
     *
     * @return the resulting task containing nothing
     */
    fun deletePayment(groupId: String, payment: GroupPayment): Task<Void> = Firebase.firestore
        .document("groups/$groupId/payments/${payment.id}").delete()

    /**
     * Gets a specific group identified by its document id in the firebase firestore.
     *
     * @param groupId the document id for the group in the firebase firestore database
     *
     * @return the resulting task containing a group object
     */
    fun getGroup(groupId: String): Task<Group> = Firebase.firestore
            .document("groups/$groupId").get()
            .continueWith { it.result!!.toObject<Group>() }

    /**
     * Gets a specific group member identified by its document id out of a specific group
     * identified by its document id in the firebase firestore.
     *
     * @param groupId the document id for the group in the firebase firestore database
     * @param memberId the document id for the group member in the firebase firestore database
     *
     * @return the resulting task containing a document reference to the group member
     */
    fun getGroupMember(groupId: String, memberId: String): Task<DocumentSnapshot> = Firebase.firestore
            .document("groups/$groupId/members/$memberId").get()
}
