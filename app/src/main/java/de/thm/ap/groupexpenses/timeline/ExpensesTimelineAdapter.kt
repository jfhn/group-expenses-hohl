package de.thm.ap.groupexpenses.timeline

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import de.thm.ap.groupexpenses.R

class ExpensesTimelineAdapter(expensesTimelineModels: List<ExpensesTimelineModel>)
    : RecyclerView.Adapter<ExpensesTimelineViewHolder>() {

    private val models: MutableList<ExpensesTimelineModel> = mutableListOf()
    private val modelViewHolderMap:
            MutableMap<ExpensesTimelineModel, ExpensesTimelineViewHolder> = mutableMapOf()

    var dividerPosition: Int = -1
        private set

    init {
        // TODO find latest expense for displaying divider, sort timelineModels by date -> tbi
        this.models.addAll(expensesTimelineModels)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesTimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)

        return ExpensesTimelineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(viewHolder: ExpensesTimelineViewHolder, position: Int) {
        val model = this.models[position]

        this.modelViewHolderMap[model] = viewHolder

        viewHolder.date.text = model.date//.formatDateTime("")
        viewHolder.name.text = model.name
        viewHolder.cost.text = model.getCost()

        if (viewHolder.date.text == "Fr. 18.12.2020") { // TODO
            viewHolder.divider.visibility = VISIBLE

            this.dividerPosition = viewHolder.layoutPosition
        }
    }

    override fun getItemCount(): Int = this.models.size

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, this.itemCount)
    }
}