package de.thm.ap.groupexpenses.timeline

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_timeline.view.*

class ExpensesTimelineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

    private val timeline = itemView.timeline!!
    private val divider  = itemView.divider!!

    val date = itemView.text_timeline_date!!
    val name = itemView.text_timeline_name!!
    val cost = itemView.text_timeline_cost!!

    init {
        timeline.initLine(viewType)
    }

    fun bind(timelineModel: ExpensesTimelineModel, position: Int) {
        itemView.setOnClickListener {
            TODO("start intent with the specified timeline model's id")
        }
    }

    fun setVisibility(visibility: Int) {
        divider.visibility = visibility
    }
}