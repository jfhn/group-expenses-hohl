package de.thm.ap.groupexpenses.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.thm.ap.groupexpenses.MainActivity
import de.thm.ap.groupexpenses.databinding.FragmentSettingsBinding
import de.thm.ap.groupexpenses.ui.user.UserViewModel

class SettingsFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                // TODO: show user data
            } else {
                (requireActivity() as MainActivity).startSignIn()
            }
        }

        binding.actionSignOut.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.signOut()
            mainActivity.startSignIn()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
