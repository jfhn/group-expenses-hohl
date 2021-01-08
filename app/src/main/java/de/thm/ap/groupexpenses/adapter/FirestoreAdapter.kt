package de.thm.ap.groupexpenses.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

abstract class FirestoreAdapter<VH : RecyclerView.ViewHolder>(query: Query)
    : RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot>
{
    companion object {
        const val TAG = "FirestoreAdapter"
    }

    private var mQuery: Query? = query
    private var mRegistration: ListenerRegistration? = null

    private val mSnapshots: MutableList<DocumentSnapshot> = mutableListOf()

    override fun onEvent(documentSnapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        // HandleErrors
        if (error != null) {
            Log.w(TAG, "onEvent:error", error)
            return
        }

        // Dispatch the event
        documentSnapshot?.documentChanges?.forEach { change ->
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }

        onDataChanged()
    }

    protected open fun onDocumentAdded(change: DocumentChange) {
        mSnapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    protected open fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            mSnapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            mSnapshots.removeAt(change.oldIndex)
            mSnapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    protected open fun onDocumentRemoved(change: DocumentChange) {
        mSnapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    fun startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery!!.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        if (mRegistration != null) {
            mRegistration!!.remove()
            mRegistration = null
        }
        mSnapshots.clear()
        notifyDataSetChanged()
    }

    open fun setQuery(query: Query) {
        // Stop listening
        stopListening()

        // Clear existing data
        mSnapshots.clear()
        notifyDataSetChanged()

        // Listen to new query
        mQuery = query
        startListening()
    }

    override fun getItemCount(): Int = mSnapshots.size

    protected open fun getSnapshot(index: Int): DocumentSnapshot? = mSnapshots[index]

    protected open fun onError(e: FirebaseFirestoreException?) {}

    protected open fun onDataChanged() {}
}