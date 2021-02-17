package de.thm.ap.groupexpenses.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupViewModel
import de.thm.ap.groupexpenses.adapter.GroupStatsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupStatisticsBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.ui.statistics.GroupStatisticsViewModel
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupMembersQuery

class GroupStatisticsFragment : Fragment() {
    private lateinit var binding: FragmentGroupStatisticsBinding
    private val groupViewModel: GroupViewModel by activityViewModels()
    private val groupStatisticsViewModel: GroupStatisticsViewModel by viewModels()

    private var adapter: GroupStatsAdapter? = null
    private lateinit var registration: ListenerRegistration

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
    : View? {
        binding = FragmentGroupStatisticsBinding.inflate(inflater, container, false)

        groupStatisticsViewModel.group.observe(viewLifecycleOwner) { group ->
            if (group != null) {
                binding.group = group
                initRecyclerView(group)
            } else {
                initRegistration(groupViewModel.groupId)
            }
        }

        return binding.root
    }

    private fun initRegistration(groupId: String) {
        val groupRef = Firebase.firestore.document("groups/$groupId")

        registration = groupRef.addSnapshotListener { snapshot, _ ->
            groupStatisticsViewModel.group.value = snapshot!!.toObject<Group>()
        }
    }

    private fun initRecyclerView(group: Group) {
        val query = groupMembersQuery(groupViewModel.groupId)
        adapter = object : GroupStatsAdapter(query, group) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    binding.recyclerMemberStats.visibility = View.GONE
                } else {
                    binding.recyclerMemberStats.visibility = View.VISIBLE
                }
            }
        }

        binding.recyclerMemberStats.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMemberStats.adapter = adapter

        adapter?.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
        registration.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}