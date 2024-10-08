package de.thm.ap.groupexpenses.ui.statistics

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.AchievementsActivity
import de.thm.ap.groupexpenses.MainActivity
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.adapter.UserGroupStatsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentUserStatisticsBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.model.UserData
import de.thm.ap.groupexpenses.ui.user.UserViewModel
import de.thm.ap.groupexpenses.worker.FirebaseWorker.userGroupsStatsQuery

/**
 * The user statistics fragment, used to display the user's statistics in the main activity.
 */
class UserStatisticsFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    private val userStatisticsViewModel: UserStatisticsViewModel by viewModels()
    private lateinit var binding: FragmentUserStatisticsBinding
    private lateinit var registration: ListenerRegistration

    private var adapter: UserGroupStatsAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserStatisticsBinding.inflate(inflater, container, false)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                initRegistration(user)
                initRecyclerView(user)
            } else {
                (requireActivity() as MainActivity).startSignIn()
            }
        }

        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            binding.userData = userData
        }

        userStatisticsViewModel.expenses.observe(viewLifecycleOwner) {
            binding.personalTotalGroupExpenses.text = getString(R.string.fmt_double_EUR).format(it)
        }

        userStatisticsViewModel.payments.observe(viewLifecycleOwner) {
            binding.personalTotalGroupPayments.text = getString(R.string.fmt_double_EUR).format(it)
        }

        this.setHasOptionsMenu(true)

        return binding.root
    }

    private fun initRegistration(user: FirebaseUser) {
        val userRef = Firebase.firestore.document("users/${user.uid}")

        registration = userRef.addSnapshotListener { snapshot, _ ->
            userViewModel.userData.value = snapshot!!.toObject<UserData>()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.statistics, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_achievements -> {
                startActivity(Intent(requireContext(), AchievementsActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView(user: FirebaseUser) {
        val query = userGroupsStatsQuery(user.uid)

        adapter = object : UserGroupStatsAdapter(query) {
            override fun onDataChanged() {
                if (itemCount == 0) {
                    binding.recyclerGroupStats.visibility = View.GONE
                } else {
                    binding.recyclerGroupStats.visibility = View.VISIBLE
                }

                calculateStats()
            }
        }

        binding.recyclerGroupStats.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGroupStats.adapter = adapter

        adapter?.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
        registration.remove()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    fun calculateStats() {
        var expenses = 0.0
        var payments = 0.0

        adapter!!.map { it.toObject<Group>()!! }.forEach { group ->
            expenses += group.totalExpenses
            payments += group.totalPayments
        }

        userStatisticsViewModel.expenses.value = expenses
        userStatisticsViewModel.payments.value = payments
    }
}
