package de.thm.ap.groupexpenses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.ItemGroupBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import java.util.*

/**
 * The groups adapter, used for displaying the groups of the user.
 * Data changes in the backend will be reflected in real time.
 *
 * @param query    The (data) query for the firestore adapter
 * @param listener The on expense selected listener to process the selection of an element
 *                 of the groups list.
 *
 * @see FirestoreAdapter
 */
open class GroupsAdapter(query: Query, private val listener: OnGroupSelectedListener)
    : FirestoreAdapter<GroupsAdapter.ViewHolder>(query) {

    interface OnGroupSelectedListener {
        fun onGroupSelected(group: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class ViewHolder(val binding: ItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot, listener: OnGroupSelectedListener) {
            val group: Group = snapshot.toObject() ?: return
            val resources = binding.root.resources

            binding.itemGroupName.text = group.name
            binding.itemGroupMembers.text =
                    if (group.numMembers == 1) resources.getString(R.string.single_member)
                    else String.format(
                            Locale.GERMANY,
                            resources.getString(R.string.fmt_members),
                            group.numMembers
                    )
            binding.itemGroupExpenses.text = String.format(
                    Locale.GERMANY,
                    resources.getString(R.string.fmt_double_EUR),
                    group.getPersonalBalance()
            )
            binding.itemGroupLatestUpdate.text = String.format(
                    Locale.GERMANY,
                    resources.getString(R.string.fmt_latest_update),
                    group.latestUpdate?.formatGerman()
            )

            // Add a color to the group's expenses balance to reflect positive or negative balances
            val color = if (group.getPersonalBalance() < 0) R.color.red else R.color.black

            binding.itemGroupExpenses.setTextColor(resources.getColor(color, null))

            binding.root.setOnClickListener {
                listener.onGroupSelected(snapshot)
            }
        }
    }
}
