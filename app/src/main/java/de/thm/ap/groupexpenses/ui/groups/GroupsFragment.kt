package de.thm.ap.groupexpenses.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupActivity
import de.thm.ap.groupexpenses.MainActivity
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.adapter.GroupsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupsBinding
import de.thm.ap.groupexpenses.ui.RecyclerFragment
import de.thm.ap.groupexpenses.ui.user.UserViewModel

class GroupsFragment : RecyclerFragment(), GroupsAdapter.OnGroupSelectedListener {
    companion object {
        const val TAG = "GroupsFragment"
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private val groupsViewModel: GroupsViewModel by viewModels()
    private lateinit var binding: FragmentGroupsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initRecyclerView()
        return root
    }

    override fun initRecyclerView() {
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                val query: Query = Firebase.firestore.collection("groups")
                        .whereArrayContains("members", user.uid)
                        .orderBy("latestUpdate", Query.Direction.DESCENDING)

                adapter = object : GroupsAdapter(query, this@GroupsFragment) {
                    override fun onDataChanged() {
                        if (itemCount == 0) {
                            binding.recyclerGroups.visibility  = View.GONE
                            binding.groupsEmptyView.visibility = View.VISIBLE
                        } else {
                            binding.recyclerGroups.visibility  = View.VISIBLE
                            binding.groupsEmptyView.visibility = View.GONE
                        }
                    }
                }

                binding.recyclerGroups.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerGroups.adapter = adapter

                adapter?.startListening()
            } else {
                (requireActivity() as MainActivity).startSignIn()
            }
        }
    }

    override fun onGroupSelected(group: DocumentSnapshot) {
        val intent = Intent(context, GroupActivity::class.java)
        intent.putExtra(GroupActivity.KEY_GROUP_ID, group.id)

        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }
}
