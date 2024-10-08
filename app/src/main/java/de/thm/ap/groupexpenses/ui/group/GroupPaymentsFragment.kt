package de.thm.ap.groupexpenses.ui.group

import android.content.Intent
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
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_GROUP_ID
import de.thm.ap.groupexpenses.GroupViewModel
import de.thm.ap.groupexpenses.PaymentFormActivity
import de.thm.ap.groupexpenses.adapter.GroupPaymentsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupPaymentsBinding
import de.thm.ap.groupexpenses.model.GroupMember
import de.thm.ap.groupexpenses.ui.RecyclerFragment
import de.thm.ap.groupexpenses.worker.FirebaseWorker
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupPaymentsQuery

/**
 * The group payments fragment, used to display the group's payments in the group activity.
 */
class GroupPaymentsFragment : RecyclerFragment() {
    private lateinit var binding: FragmentGroupPaymentsBinding
    private val groupViewModel: GroupViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupPaymentsBinding.inflate(inflater, container, false)
        initRecyclerView()

        // Bind the FAB for adding payments
        binding.addPaymentButton.setOnClickListener {
            startActivity(Intent(context, PaymentFormActivity::class.java).apply {
                putExtra(KEY_GROUP_ID, groupViewModel.groupId)
            })
        }

        return binding.root
    }

    override fun initRecyclerView() {
        val query: Query = groupPaymentsQuery(groupViewModel.groupId)

        FirebaseWorker.getGroupMember(groupViewModel.groupId, Firebase.auth.currentUser!!.uid)
            .addOnSuccessListener {
                val isAdmin = it!!.toObject<GroupMember>()!!.role == "admin"

                adapter = object : GroupPaymentsAdapter(
                    query,
                    isAdmin,
                    requireContext(),
                    groupViewModel.groupId
                ) {
                    override fun onDataChanged() {
                        if (itemCount == 0) {
                            binding.recyclerGroupPayments.visibility = View.GONE
                            binding.groupPaymentsEmptyView.visibility = View.VISIBLE
                        } else {
                            binding.recyclerGroupPayments.visibility = View.VISIBLE
                            binding.groupPaymentsEmptyView.visibility = View.GONE
                        }
                    }
                }

                binding.recyclerGroupPayments.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerGroupPayments.adapter = adapter

                adapter?.startListening()
            }
    }
}
