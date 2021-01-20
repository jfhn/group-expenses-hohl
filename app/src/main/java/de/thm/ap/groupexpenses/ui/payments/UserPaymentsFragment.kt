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
import de.thm.ap.groupexpenses.ui.user.UserViewModel

class UserPaymentsFragment : Fragment() {
    companion object {
        const val TAG = "UserPaymentsFragment"
    }

    private val userViewModel: UserViewModel by activityViewModels()
    private val paymentsViewModel: PaymentsViewModel by viewModels()
    private var _binding: FragmentUserPaymentsBinding? = null
    private var adapter: UserPaymentsAdapter? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPaymentsBinding.inflate(inflater, container, false)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                initRecyclerView(user)
            } else {
                (requireActivity() as MainActivity).startSignIn()
            }
        }
        return binding.root
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

    fun initRecyclerView(user: FirebaseUser) {
        val query: Query = Firebase.firestore
            .collection("users/${user.uid}/payments")
            .orderBy("date", Query.Direction.DESCENDING)

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
    }
}