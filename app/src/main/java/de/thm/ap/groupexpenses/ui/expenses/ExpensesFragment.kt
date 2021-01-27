package de.thm.ap.groupexpenses.ui.expenses

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.internal.GetServiceRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction.ASCENDING
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.*
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_GROUP_ID
import de.thm.ap.groupexpenses.adapter.ExpensesAdapter
import de.thm.ap.groupexpenses.databinding.FragmentExpensesBinding
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
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.expenses, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_jump_to_current_date -> {
                val pos = adapter.dividerPosition

                binding.expensesRecyclerView.smoothScrollToPosition(pos)
                true
            }

            R.id.action_edit -> {
                TODO("Not yet implemented")
            }

            R.id.action_stats -> {
                TODO("Not yet implemented")
            }

            R.id.action_invite -> {
                val link = getInvitationLink()
                val ctx = requireContext()
                AlertDialog.Builder(ctx).apply {
                    setTitle(getString(R.string.group_invitation_link))
                    setMessage(link)
                    setPositiveButton("Teilen") { _, _ ->
                        val i = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Group Expenses - Gruppeneinladung")
                            putExtra(Intent.EXTRA_TEXT, link)
                        }
                        startActivity(Intent.createChooser(i, "Teilen mit..."))
                    }
                    setNegativeButton("Kopieren") { _, _ ->
                        val clipboardManager = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("text", link)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(ctx, "Link wurde kopiert", Toast.LENGTH_LONG).show()
                    }
                    show()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        val query: Query = Firebase.firestore
            .collection("groups/${groupViewModel.groupId}/expenses")
            .orderBy("date", ASCENDING)

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

    fun getInvitationLink(): String {
        return "http://de.thm.ap.groupexpenses/group_invite/${groupViewModel.groupId}"
    }

    companion object {
        const val KEY_EXPENSE_ID = "key_expense_id"
    }
}
/*
TODO:
- Add color config to entries (elapsed, completed, open)
 */