package de.thm.ap.groupexpenses.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import de.thm.ap.groupexpenses.R

class TimelineAdapter(private val timelineModels: List<TimelineModel>)
    : RecyclerView.Adapter<TimelineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)

        return TimelineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val timelineModel = timelineModels[position]

        holder.date.text = timelineModel.date//.formatDateTime("")
        holder.name.text = timelineModel.name
        holder.cost.text = timelineModel.getCost()
    }

    override fun getItemCount(): Int = timelineModels.size

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }
}