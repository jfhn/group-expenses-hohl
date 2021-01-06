package de.thm.ap.groupexpenses.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

class GroupsFragment : Fragment() {
	private val GroupsViewModel: GroupsViewModel by viewModels()
	private var _binding: FragmentGroupsBinding? = null

	private val binding get() = _binding!!

	override fun onCreateView(
			inflater: LayoutInflater,
			container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		_binding = FragmentGroupsBinding.inflate(inflater, container, false)
		val root: View = binding.root

		val textView: TextView = binding.textGroups
		GroupsViewModel.text.observe(viewLifecycleOwner, Observer {
			textView.text = it
		})
		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
