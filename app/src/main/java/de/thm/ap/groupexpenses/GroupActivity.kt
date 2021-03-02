package de.thm.ap.groupexpenses

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.databinding.ActivityGroupBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.ui.expenses.ExpensesFragment
import de.thm.ap.groupexpenses.ui.group.GroupMembersFragment
import de.thm.ap.groupexpenses.ui.group.GroupPaymentsFragment
import de.thm.ap.groupexpenses.ui.group.GroupStatisticsFragment
import de.thm.ap.groupexpenses.util.GroupMembershipChecker

/**
 * The group activity, containing four fragments for displaying a single group's data:
 * - ExpensesFragment
 * - GroupMembersFragment
 * - GroupPaymentsFragment
 * - GroupStatisticsFragment
 *
 * @see ExpensesFragment
 * @see GroupMembersFragment
 * @see GroupPaymentsFragment
 * @see GroupStatisticsFragment
 */
class GroupActivity : AppCompatActivity() {

    private val viewModel: GroupViewModel by viewModels()
    private lateinit var binding: ActivityGroupBinding

    private lateinit var registration: ListenerRegistration
    private lateinit var membershipChecker: GroupMembershipChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.groupId = intent.extras?.getString(KEY_GROUP_ID)
                ?: throw IllegalArgumentException("Must pass extra $KEY_GROUP_ID")

        title = ""

        viewModel.parentBinding = binding
        viewModel.group.observe(this) { group ->
            if (group != null) {
                title = group.name
            }
        }

        binding.groupViewpager.adapter = GroupViewPagerAdapter(this)

        membershipChecker = GroupMembershipChecker(viewModel.groupId) {
            Toast.makeText(this, R.string.kicked_message, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun initRegistration(groupId: String) {
        val groupRef = Firebase.firestore.document("groups/$groupId")

        registration = groupRef.addSnapshotListener { snapshot, _ ->
            viewModel.group.value = snapshot!!.toObject<Group>()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.groups, menu)

        return true
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.action_remind -> {
                val groupName = viewModel.group.value!!.name!!
                val subject   = "${getString(R.string.reminder_subject)} $groupName"
                val text      = "${getString(R.string.reminder_text_a)} $groupName " +
                        getString(R.string.reminder_text_b)

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "*/*"
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, text)
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                    true
                } else false
            }

            R.id.action_invite -> {
                val link = getInvitationLink()
                val ctx = this
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

    private fun getInvitationLink(): String {
        return "http://de.thm.ap.groupexpenses/group_invite/${viewModel.groupId}"
    }

    override fun onStart() {
        super.onStart()
        initRegistration(viewModel.groupId)
        membershipChecker.startListening()
    }

    override fun onStop() {
        super.onStop()
        registration.remove()
        membershipChecker.stopListening()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

    private inner class GroupViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        private val fragments: List<() -> Fragment> = listOf(
            { ExpensesFragment() },
            { GroupStatisticsFragment() },
            { GroupMembersFragment() },
            { GroupPaymentsFragment() },
        )

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]()
    }

    companion object {
        const val KEY_GROUP_ID = "key_group_id"
        const val KEY_EXPENSE_ID = "key_expense_id"
    }
}