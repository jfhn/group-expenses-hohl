package de.thm.ap.groupexpenses.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupViewModel
import de.thm.ap.groupexpenses.adapter.GroupMembersAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupMembersBinding
import de.thm.ap.groupexpenses.model.GroupMember
import de.thm.ap.groupexpenses.ui.RecyclerFragment
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getGroupMember
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupMembersQuery

/**
 * The group members fragment, used to display the group's members in the group activity.
 */
class GroupMembersFragment : RecyclerFragment() {
    private lateinit var binding: FragmentGroupMembersBinding
    private val groupViewModel: GroupViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
    : View {
        binding = FragmentGroupMembersBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    override fun initRecyclerView() {
        val query: Query = groupMembersQuery(groupViewModel.groupId)

        getGroupMember(groupViewModel.groupId, Firebase.auth.currentUser!!.uid)
            .addOnSuccessListener {
                val isAdmin = it!!.toObject<GroupMember>()!!.role == "admin"

                adapter = object : GroupMembersAdapter(
                    query,
                    isAdmin,
                    groupViewModel.groupId,
                    requireActivity()
                ) {
                    override fun onDataChanged() {
                        super.onDataChanged()

                        binding.recyclerGroupMembers.adapter = adapter
                    }
                }

                binding.recyclerGroupMembers.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerGroupMembers.adapter       = adapter

                adapter?.startListening()
            }
    }
}