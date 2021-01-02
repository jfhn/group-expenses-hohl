package de.thm.ap.groupexpenses.timeline

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import de.thm.ap.groupexpenses.R

class ExpensesTimelineAdapter(expensesTimelineModels: List<ExpensesTimelineModel>)
    : RecyclerView.Adapter<ExpensesTimelineViewHolder>() {

    private val expensesTimelineModels: MutableList<ExpensesTimelineModel> = mutableListOf()

    init {
        // TODO find latest expense for displaying divider, sort timelineModels by date -> tbi
        this.expensesTimelineModels.addAll(expensesTimelineModels)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesTimelineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timeline, parent, false)

        return ExpensesTimelineViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holderExpenses: ExpensesTimelineViewHolder, position: Int) {
        val timelineModel = this.expensesTimelineModels[position]

        holderExpenses.bind(timelineModel, position)

        holderExpenses.date.text = timelineModel.date//.formatDateTime("")
        holderExpenses.name.text = timelineModel.name
        holderExpenses.cost.text = timelineModel.getCost()

        if (holderExpenses.date.text == "Fr. 18.12.2020") { // TODO
            holderExpenses.setVisibility(VISIBLE)
        }
    }

    override fun getItemCount(): Int = this.expensesTimelineModels.size

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, this.itemCount)
    }
}