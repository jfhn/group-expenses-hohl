package de.thm.ap.groupexpenses.timeline

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_timeline.view.*

class TimelineViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

    val timeline = itemView.timeline!!
    val date     = itemView.text_timeline_date!!
    val name     = itemView.text_timeline_name!!
    val cost     = itemView.text_timeline_cost!!

    init {
        timeline.initLine(viewType)
    }
}