package de.thm.ap.groupexpenses.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import de.thm.ap.groupexpenses.MainActivity
import de.thm.ap.groupexpenses.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSettings
        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

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
