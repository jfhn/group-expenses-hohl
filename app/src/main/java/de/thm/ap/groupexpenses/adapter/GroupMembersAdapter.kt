package de.thm.ap.groupexpenses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.ItemGroupMemberBinding
import de.thm.ap.groupexpenses.model.GroupMember
import de.thm.ap.groupexpenses.worker.FirebaseWorker

open class GroupMembersAdapter(query: Query,
                               private val isAdmin: Boolean,
                               private val groupId: String)
    : FirestoreAdapter<GroupMembersAdapter.ViewHolder>(query) {

    companion object {
        const val TAG = "GroupMembersAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGroupMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    inner class ViewHolder(val binding: ItemGroupMemberBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(snapshot: DocumentSnapshot) {
            val groupMember: GroupMember = snapshot.toObject() ?: return

            binding.itemGroupMemberName.text = groupMember.userName
            binding.itemGroupMemberRole.text = when (groupMember.role) {
                "admin"  -> binding.root.resources.getString(R.string.admin)
                "member" -> binding.root.resources.getString(R.string.member)
                else     -> throw IllegalStateException("$TAG: must not occur")
            }

            if (Firebase.auth.currentUser!!.uid == groupMember.id) {
                binding.itemGroupMemberKick.visibility  = View.GONE
                binding.itemGroupMemberLeave.visibility = View.VISIBLE

                binding.itemGroupMemberLeave.setOnClickListener {
                    // TODO: add confirmation alert dialog
                    FirebaseWorker.leaveGroup(groupId)
                }
            } else if (groupMember.role == "member") {
                binding.itemGroupMemberLeave.visibility = View.GONE

                binding.itemGroupMemberKick.visibility = if (isAdmin) {
                    binding.itemGroupMemberKick.setOnClickListener {
                        // TODO: add confirmation alert dialog
                        FirebaseWorker.kickMemberFromGroup(groupId, groupMember.id!!)
                    }

                    View.VISIBLE
                } else View.GONE
            }
        }
    }
}
