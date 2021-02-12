package de.thm.ap.groupexpenses.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupViewModel
import de.thm.ap.groupexpenses.adapter.GroupMembersAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupMembersBinding
import de.thm.ap.groupexpenses.ui.RecyclerFragment
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupMembersQuery

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

        adapter = GroupMembersAdapter(query)

        binding.recyclerGroupMembers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGroupMembers.adapter       = adapter

        adapter?.startListening()
    }

    companion object {
        const val TAG = "GroupMembersFragment"
    }
}