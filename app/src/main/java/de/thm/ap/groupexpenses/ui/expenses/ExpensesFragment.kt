package de.thm.ap.groupexpenses.ui.expenses

import android.content.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import de.thm.ap.groupexpenses.*
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_EXPENSE_ID
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_GROUP_ID
import de.thm.ap.groupexpenses.adapter.ExpensesAdapter
import de.thm.ap.groupexpenses.databinding.FragmentExpensesBinding
import de.thm.ap.groupexpenses.worker.FirebaseWorker.groupExpensesQuery
import java.util.*

class ExpensesFragment : Fragment(), ExpensesAdapter.OnExpenseSelectedListener {

    private val groupViewModel: GroupViewModel by activityViewModels()

    private lateinit var binding: FragmentExpensesBinding
    private lateinit var adapter: ExpensesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpensesBinding.inflate(inflater, container, false)

        this.setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.addExpenseButton.setOnClickListener {
            val intent = Intent(context, ExpenseFormActivity::class.java).apply {
                putExtra(KEY_GROUP_ID, groupViewModel.groupId)
            }
            startActivity(intent)
        }

        val tabLayout = groupViewModel.parentBinding.groupTabs
        val viewPager = groupViewModel.parentBinding.groupViewpager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ausgaben"
                1 -> "Statistik"
                2 -> "Mitglieder"
                3 -> "Zahlungen"
                else -> ""
            }

        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.expenses, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_jump_to_current_date -> {
                if (adapter.itemCount != 0) {
                    val pos = adapter.getDividerPosition()

                    binding.expensesRecyclerView.smoothScrollToPosition(pos)
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        val query: Query = groupExpensesQuery(groupViewModel.groupId)

        this.adapter = object : ExpensesAdapter(query, this@ExpensesFragment) {
            override fun onDataChanged() {
                super.onDataChanged()

                if (itemCount == 0) {
                    binding.expensesRecyclerView.visibility = View.GONE
                    binding.expensesEmptyView.visibility    = View.VISIBLE
                } else {
                    binding.expensesRecyclerView.visibility = View.VISIBLE
                    binding.expensesEmptyView.visibility    = View.GONE

                    // initialize view elements to enable scroll to current date
                    binding.expensesRecyclerView.smoothScrollToPosition(this.itemCount - 1)
                    binding.expensesRecyclerView.smoothScrollToPosition(0)
                }
            }
        }

        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.expensesRecyclerView.adapter       = this.adapter

        this.adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        this.adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        this.adapter.stopListening()
    }

    override fun onExpenseSelected(expense: DocumentSnapshot) {
        val intent = Intent(context, ExpensesDetailActivity::class.java)

        intent.putExtra(KEY_GROUP_ID, groupViewModel.groupId)
        intent.putExtra(KEY_EXPENSE_ID, expense.id)

        startActivity(intent)
    }
}
