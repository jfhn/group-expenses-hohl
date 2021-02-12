package de.thm.ap.groupexpenses.ui.group

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_GROUP_ID
import de.thm.ap.groupexpenses.GroupViewModel
import de.thm.ap.groupexpenses.PaymentFormActivity
import de.thm.ap.groupexpenses.adapter.GroupPaymentsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentGroupPaymentsBinding
import de.thm.ap.groupexpenses.ui.RecyclerFragment
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupPaymentsQuery

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

        binding.addPaymentButton.setOnClickListener {
            startActivity(Intent(context, PaymentFormActivity::class.java).apply {
                putExtra(KEY_GROUP_ID, groupViewModel.groupId)
            })
        }

        return binding.root
    }

    override fun initRecyclerView() {
        val query: Query = groupPaymentsQuery(groupViewModel.groupId)

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

    companion object {
        const val TAG = "GroupPaymentsFragment"
    }
}
