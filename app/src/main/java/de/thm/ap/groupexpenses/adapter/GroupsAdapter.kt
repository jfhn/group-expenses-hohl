package de.thm.ap.groupexpenses.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.model.Group
import java.util.*

open class GroupsAdapter(query: Query)
    : FirestoreAdapter<GroupsAdapter.Companion.ViewHolder>(query)
{
    companion object {
        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            private val groupName: TextView = itemView.findViewById(R.id.item_group_name)
            private val groupNumOfMembers: TextView = itemView.findViewById(R.id.item_group_members)
            private val groupExpenses: TextView = itemView.findViewById(R.id.item_group_expenses)
            private val groupLatestUpdate: TextView = itemView.findViewById(R.id.item_group_latest_update)

            fun bind(snapshot: DocumentSnapshot) {
                val group: Group = snapshot.toObject()!!
                val resources = itemView.resources

                groupName.text = group.name?: ""
                groupNumOfMembers.text = String.format(
                        Locale.getDefault(),
                        resources.getString(R.string.fmt_members),
                        group.numOfMembers
                )
                groupExpenses.text = String.format(
                        Locale.getDefault(),
                        resources.getString(R.string.fmt_expenses_EUR),
                        group.expenses
                )
                groupLatestUpdate.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        .format(group.latestUpdate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_group, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position)!!)
    }
}