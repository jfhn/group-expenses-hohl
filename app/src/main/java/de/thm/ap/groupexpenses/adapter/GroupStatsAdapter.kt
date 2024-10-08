package de.thm.ap.groupexpenses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.databinding.ItemMemberStatsBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.model.GroupMember

/**
 * The group stats adapter, containing the group statistics data for the provided group.
 * Data changes in the backend will be reflected in real time.
 *
 * @param query The (data) query for the firestore adapter
 * @param group The current group to display the statistics for
 *
 * @see FirestoreAdapter
 */
open class GroupStatsAdapter(query: Query, var group: Group)
    : FirestoreAdapter<GroupStatsAdapter.ViewHolder>(query) {

    class ViewHolder(val binding: ItemMemberStatsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot, group: Group) {
            val groupMember: GroupMember = snapshot.toObject()!!
            groupMember.personalBalance = groupMember.totalPayments - group.getPaymentPerUser()
            binding.groupMember = groupMember
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMemberStatsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), group)
    }
}