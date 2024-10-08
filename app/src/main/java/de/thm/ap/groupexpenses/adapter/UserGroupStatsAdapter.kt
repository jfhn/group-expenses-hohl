package de.thm.ap.groupexpenses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.databinding.ItemGroupStatsBinding
import de.thm.ap.groupexpenses.model.Group

/**
 * The user group stats adapter contains the group statistics data for all groups the user is in.
 * Data changes in the backend will be reflected in real time.
 *
 * @param query The (data) query for the firestore adapter
 *
 * @see FirestoreAdapter
 */
open class UserGroupStatsAdapter(query: Query)
    : FirestoreAdapter<UserGroupStatsAdapter.ViewHolder>(query) {

    class ViewHolder(val binding: ItemGroupStatsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot) {
            val group: Group = snapshot.toObject()!!
            binding.group = group
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGroupStatsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }
}