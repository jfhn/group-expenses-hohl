package de.thm.ap.groupexpenses.ui.groups

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.adapter.GroupsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupsBinding

class GroupsFragment : Fragment(), GroupsAdapter.OnGroupSelectedListener {
    companion object {
        const val TAG = "GroupsFragment"
    }
    private val groupsViewModel: GroupsViewModel by viewModels()
    private var _binding: FragmentGroupsBinding? = null
    private var adapter: GroupsAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecyclerView()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun initRecyclerView() {
        val query: Query = Firebase.firestore.collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("groups")

        object : GroupsAdapter(query, this@GroupsFragment) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    binding.recyclerGroups.visibility  = View.GONE
                    binding.groupsEmptyView.visibility = View.VISIBLE
                } else {
                    binding.recyclerGroups.visibility  = View.VISIBLE
                    binding.groupsEmptyView.visibility = View.GONE
                }
            }
        }.also { adapter = it }

        binding.recyclerGroups.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGroups.adapter = adapter
    }

    override fun onGroupSelected(group: DocumentSnapshot) {
//        val intent = Intent(context, GroupDetailActivity::class.java)
//        intent.putExtra(GroupDetailActivity.KEY_GROUP_ID, group.id)
//
//        startActivity(intent)
//        requireActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }
}
