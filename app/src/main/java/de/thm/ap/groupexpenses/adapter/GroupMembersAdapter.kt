package de.thm.ap.groupexpenses.adapter

import android.app.Activity
import android.app.AlertDialog
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
                               private val groupId: String,
                               private val activity: Activity)
    : FirestoreAdapter<GroupMembersAdapter.ViewHolder>(query) {

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
                else     -> "???"
            }

            val isLastMember = this@GroupMembersAdapter.itemCount == 1
            val canLeave     = !isAdmin || isLastMember

            if (Firebase.auth.currentUser!!.uid == groupMember.id && canLeave) {
                binding.itemGroupMemberKick.visibility  = View.GONE
                binding.itemGroupMemberLeave.visibility = View.VISIBLE

                binding.itemGroupMemberLeave.setOnClickListener {
                    AlertDialog.Builder(activity).apply {
                        setTitle(R.string.leave_group)
                        val message =
                            if (isLastMember) activity.getString(R.string.confirm_leave_descA)
                            else activity.getString(R.string.confirm_leave_descB)
                        setMessage(message)
                        setNeutralButton(R.string.cancel, null)
                        setNegativeButton(R.string.leave_group) { _, _ ->
                            FirebaseWorker.leaveGroup(groupId)
                            activity.finish()
                        }
                        show()
                    }
                }
            } else if (groupMember.role != "admin") {
                binding.itemGroupMemberLeave.visibility = View.GONE

                binding.itemGroupMemberKick.visibility = if (isAdmin) {
                    binding.itemGroupMemberKick.setOnClickListener {
                        AlertDialog.Builder(activity).apply {
                            setTitle(R.string.kick_member)
                            val message = context.getString(R.string.confirm_kick_desc1) + groupMember.userName + context.getString(R.string.confirm_kick_desc2)
                            setMessage(message)
                            setNeutralButton(R.string.cancel, null)
                            setNegativeButton(R.string.kick_member) { _, _ ->
                                FirebaseWorker.kickMemberFromGroup(groupId, groupMember.id!!)
                            }
                            show()
                        }
                    }

                    View.VISIBLE
                } else View.GONE
            } else {
                binding.itemGroupMemberLeave.visibility = View.GONE
                binding.itemGroupMemberKick.visibility  = View.GONE
            }
        }
    }
}
