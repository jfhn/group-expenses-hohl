package de.thm.ap.groupexpenses.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.MainActivity
import de.thm.ap.groupexpenses.adapter.UserPaymentsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentUserPaymentsBinding
import de.thm.ap.groupexpenses.ui.RecyclerFragment
import de.thm.ap.groupexpenses.ui.user.UserViewModel
import de.thm.ap.groupexpenses.worker.FirebaseWorker.userPaymentsQuery

class UserPaymentsFragment : RecyclerFragment() {
    companion object {
        const val TAG = "UserPaymentsFragment"
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentUserPaymentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserPaymentsBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    override fun initRecyclerView() {
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                val query: Query = userPaymentsQuery(user.uid)

                adapter = object : UserPaymentsAdapter(query) {
                    override fun onDataChanged() {
                        if (itemCount == 0) {
                            binding.recyclerUserPayments.visibility  = View.GONE
                            binding.userPaymentsEmptyView.visibility = View.VISIBLE
                        } else {
                            binding.recyclerUserPayments.visibility  = View.VISIBLE
                            binding.userPaymentsEmptyView.visibility = View.GONE
                        }
                    }
                }

                binding.recyclerUserPayments.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerUserPayments.adapter = adapter

                adapter?.startListening()
            } else {
                (requireActivity() as MainActivity).startSignIn()
            }
        }
    }
}