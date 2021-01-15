package de.thm.ap.groupexpenses.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import de.thm.ap.groupexpenses.MainActivity
import de.thm.ap.groupexpenses.databinding.FragmentPaymentsBinding
import de.thm.ap.groupexpenses.ui.user.UserViewModel

class PaymentsFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private val paymentsViewModel: PaymentsViewModel by viewModels()
    private var _binding: FragmentPaymentsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                // TODO: show user data
            } else {
                (requireActivity() as MainActivity).startSignIn()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}