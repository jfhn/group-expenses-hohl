package de.thm.ap.groupexpenses.ui.achievements

import android.R.layout.simple_list_item_1
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.FragmentAchievementsBinding
import de.thm.ap.groupexpenses.model.Achievement

class AchievementsFragment : Fragment() {

    private val viewModel: AchievementsViewModel by viewModels()

    private lateinit var binding: FragmentAchievementsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false)

        val adapter = ArrayAdapter<Achievement>(requireContext(), simple_list_item_1, mutableListOf())

        binding.achievementsListView.emptyView = binding.achievementsListEmptyView
        binding.achievementsListView.adapter   = adapter

        viewModel.achievements.observe(viewLifecycleOwner) {
            adapter.clear()
            adapter.addAll(it)
        }

        return binding.root
    }
}