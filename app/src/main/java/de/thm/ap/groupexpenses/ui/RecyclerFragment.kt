package de.thm.ap.groupexpenses.ui

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import de.thm.ap.groupexpenses.adapter.FirestoreAdapter

/**
 * This class is a base class for some fragments.
 *
 * @see de.thm.ap.groupexpenses.ui.group.GroupMembersFragment
 * @see de.thm.ap.groupexpenses.ui.group.GroupPaymentsFragment
 * @see de.thm.ap.groupexpenses.ui.groups.GroupsFragment
 * @see de.thm.ap.groupexpenses.ui.payments.UserPaymentsFragment
 */
abstract class RecyclerFragment: Fragment() {
    var adapter: FirestoreAdapter<out RecyclerView.ViewHolder>? = null

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    abstract fun initRecyclerView()
}
