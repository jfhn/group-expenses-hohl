package de.thm.ap.groupexpenses.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import de.thm.ap.groupexpenses.GroupViewModel
import de.thm.ap.groupexpenses.adapter.GroupStatsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupStatisticsBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupMembersQuery

class GroupStatisticsFragment : Fragment() {
    private lateinit var binding: FragmentGroupStatisticsBinding
    private val groupViewModel: GroupViewModel by activityViewModels()

    private var adapter: GroupStatsAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
    : View? {
        binding = FragmentGroupStatisticsBinding.inflate(inflater, container, false)

        groupViewModel.group.observe(viewLifecycleOwner) { group ->
            if (group != null) {
                binding.group = group
                initRecyclerView(group)
            }
        }

        return binding.root
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}