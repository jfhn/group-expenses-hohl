package de.thm.ap.groupexpenses.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.adapter.AchievementsAdapter
import de.thm.ap.groupexpenses.databinding.FragmentAchievementsBinding
import de.thm.ap.groupexpenses.model.Achievement
import de.thm.ap.groupexpenses.model.Achievement.Companion.getAchievements
import de.thm.ap.groupexpenses.model.UserData

/**
 * The achievements fragment used to display the user's achievements.
 */
class AchievementsFragment : Fragment() {

    private val viewModel: AchievementsViewModel by viewModels()

    private lateinit var binding: FragmentAchievementsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false)

        val user    = Firebase.auth.currentUser!!
        val userRef = Firebase.firestore.document("users/${user.uid}")

        userRef.addSnapshotListener { snapshot, _ ->
            viewModel.userData.value = snapshot!!.toObject<UserData>()
        }

        viewModel.userData.observe(viewLifecycleOwner) { userData ->
            val achievements = getAchievements(userData)

            val adapter = AchievementsAdapter(achievements)

            if (adapter.itemCount == 0) {
                binding.achievementsEmptyView.visibility    = View.VISIBLE
                binding.achievementsRecyclerView.visibility = View.GONE
            } else {
                binding.achievementsEmptyView.visibility    = View.GONE
                binding.achievementsRecyclerView.visibility = View.VISIBLE

                binding.achievementsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.achievementsRecyclerView.adapter       = adapter
            }

            binding.achievementsCount.text = Achievement.getAchievementCount(achievements).toString()
            binding.shamementsCount.text   = Achievement.getShamementCount(achievements).toString()
        }

        return binding.root
    }
}