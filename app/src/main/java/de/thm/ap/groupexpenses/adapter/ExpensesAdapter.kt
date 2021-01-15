package de.thm.ap.groupexpenses.adapter

import android.icu.text.SimpleDateFormat
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
import de.thm.ap.groupexpenses.model.Expense
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import java.util.*

open class ExpensesAdapter(query: Query, private val listener: OnExpenseSelectedListener)
    : FirestoreAdapter<ExpensesAdapter.ViewHolder>(query)
{

    interface OnExpenseSelectedListener {
        fun onExpenseSelected(expense: DocumentSnapshot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position), listener)
    }

    class ViewHolder(val binding: ItemTimelineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot, listener: OnExpenseSelectedListener?) {
            val expense: Expense = snapshot.toObject() ?: return
            val resources = binding.root.resources

            binding.textTimelineName.text = expense.name
            binding.textTimelineCost.text = String.format(
                    Locale.GERMANY,
                    resources.getString(R.string.fmt_expenses_EUR),
                    expense.cost
            )

            // TODO: set divider to correct position
//            if (viewHolder.date.text == "Fr. 18.12.2020") {
//                viewHolder.divider.visibility = View.VISIBLE
//
//                this.dividerPosition = viewHolder.layoutPosition
//            }

            binding.textTimelineDate.text = expense.date!!.formatGerman()

            binding.root.setOnClickListener {
                listener?.onExpenseSelected(snapshot)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, this.itemCount)
    }

    companion object {
        private val FORMAT = SimpleDateFormat("EE dd.MM.yyyy", Locale.GERMANY)
    }
}
