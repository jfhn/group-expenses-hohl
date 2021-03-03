package de.thm.ap.groupexpenses.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.ItemTimelineBinding
import de.thm.ap.groupexpenses.model.GroupExpense
import de.thm.ap.groupexpenses.util.DateUtil.format
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import de.thm.ap.groupexpenses.util.DateUtil.toDateOnly
import java.util.*

/**
 * The group expenses adapter, containing the group's expenses data to be displayed in a timeline.
 * Data changes in the backend will be reflected in real time.
 *
 * @param query    The (data) query for the firestore adapter
 * @param listener The on expense selected listener to process the selection of an element
 *                 of the group expenses timeline.
 *
 * @see FirestoreAdapter
 */
open class ExpensesAdapter(query: Query, private val listener: OnExpenseSelectedListener)
    : FirestoreAdapter<ExpensesAdapter.ViewHolder>(query) {

    private var dividerPosition: Int? = null

    /**
     * @return The divider position of the current date's entry.
     */
    fun getDividerPosition(): Int {
        return dividerPosition ?: itemCount - 1
    }

    interface OnExpenseSelectedListener {
        fun onExpenseSelected(expense: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTimelineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val snapshot: DocumentSnapshot = getSnapshot(position)
        val expense:  GroupExpense     = snapshot.toObject()!!
        val today:    Date             = Calendar.getInstance().time.toDateOnly()

        holder.bind(snapshot, listener)

        if (this.dividerPosition == null && // no other expense found yet
            expense.date!!.toDateOnly() >= today // is today (or later)
        ) {
            this.dividerPosition = holder.adapterPosition
        }

        if (this.dividerPosition == holder.adapterPosition) {
            holder.binding.timelineDivider.visibility = View.VISIBLE
            holder.binding.currentDate.text           = today.format()
        } else {
            holder.binding.timelineDivider.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDataChanged() {
        super.onDataChanged()

        this.dividerPosition = null

        this.notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemTimelineBinding,
                     viewType:    Int) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.timeline.initLine(viewType)
        }

        fun bind(snapshot: DocumentSnapshot, listener: OnExpenseSelectedListener) {
            val expense: GroupExpense = snapshot.toObject() ?: return
            val resources = binding.root.resources

            binding.textTimelineName.text = expense.name
            binding.textTimelineCost.text = String.format(
                    Locale.GERMANY,
                    resources.getString(R.string.fmt_double_EUR),
                    expense.cost
            )
            binding.textTimelineDate.text = expense.date!!.formatGerman()

            // Forward an on click listener to the provided on expense selected listener
            binding.expenseCard.setOnClickListener {
                listener.onExpenseSelected(snapshot)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, this.itemCount)
    }
}
