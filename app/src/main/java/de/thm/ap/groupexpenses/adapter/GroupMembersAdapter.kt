package de.thm.ap.groupexpenses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.ItemGroupMemberBinding
import de.thm.ap.groupexpenses.model.GroupMember
import java.lang.IllegalStateException

open class GroupMembersAdapter(query: Query)
    : FirestoreAdapter<GroupMembersAdapter.ViewHolder>(query)
{
    companion object {
        const val TAG = "GroupMembersAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGroupMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    class ViewHolder(val binding: ItemGroupMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot) {
            val groupMember: GroupMember = snapshot.toObject() ?: return

            binding.itemGroupMemberName.text = groupMember.userName
            binding.itemGroupMemberRole.text = when (groupMember.role) {
                "admin"  -> binding.root.resources.getString(R.string.admin)
                "member" -> binding.root.resources.getString(R.string.member)
                else     -> throw IllegalStateException("$TAG: must not occur")
            }
        }
    }
}
