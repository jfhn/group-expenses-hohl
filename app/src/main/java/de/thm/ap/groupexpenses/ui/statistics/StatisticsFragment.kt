package de.thm.ap.groupexpenses.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import de.thm.ap.groupexpenses.MainActivity
import de.thm.ap.groupexpenses.databinding.FragmentStatisticsBinding
import de.thm.ap.groupexpenses.ui.user.UserViewModel

class StatisticsFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
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
