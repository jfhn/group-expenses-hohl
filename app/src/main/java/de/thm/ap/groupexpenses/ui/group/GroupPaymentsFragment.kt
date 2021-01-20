package de.thm.ap.groupexpenses.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupViewModel
import de.thm.ap.groupexpenses.adapter.GroupPaymentsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupPaymentsBinding
import de.thm.ap.groupexpenses.ui.user.UserViewModel

class GroupPaymentsFragment : Fragment() {
    companion object {
        const val TAG = "GroupPaymentsFragment"
    }

    private lateinit var binding: FragmentGroupPaymentsBinding
    private val groupViewModel: GroupViewModel by activityViewModels()
    private var adapter: GroupPaymentsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupPaymentsBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    fun initRecyclerView() {
        val query: Query = Firebase.firestore
            .collection("groups/${groupViewModel.groupId}/payments")
            .orderBy("date", Query.Direction.DESCENDING)

        adapter = object : GroupPaymentsAdapter(query) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    binding.recyclerGroupPayments.visibility  = View.GONE
                    binding.groupPaymentsEmptyView.visibility = View.VISIBLE
                } else {
                    binding.recyclerGroupPayments.visibility  = View.VISIBLE
                    binding.groupPaymentsEmptyView.visibility = View.GONE
                }
            }
        }

        binding.recyclerGroupPayments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGroupPayments.adapter = adapter

        adapter?.startListening()
    }
}
