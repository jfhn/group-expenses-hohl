package de.thm.ap.groupexpenses

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.thm.ap.groupexpenses.timeline.ExpensesTimelineAdapter
import de.thm.ap.groupexpenses.timeline.ExpensesTimelineModel
import de.thm.ap.groupexpenses.timeline.ExpensesTimelineViewHolder
import kotlinx.android.synthetic.main.activity_expenses.*

class ExpensesActivity : AppCompatActivity() {

    private lateinit var adapter      : ExpensesTimelineAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        this.title = "Group name" // TODO get group name

        this.adapter       = ExpensesTimelineAdapter(getTimelineData())
        this.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recyclerView.layoutManager = this.layoutManager
        recyclerView.adapter       = this.adapter

        this.recyclerView.smoothScrollToPosition(this.adapter.itemCount - 1)
        this.recyclerView.smoothScrollToPosition(0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.expenses, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_jump_to_current_date -> {
                val pos = this.adapter.dividerPosition

                this.recyclerView.smoothScrollToPosition(pos) // TODO add offset
                true
            }

            R.id.action_edit -> {
                TODO("Not yet implemented")
            }

            R.id.action_stats -> {
                TODO("Not yet implemented")
            }

            R.id.action_invite -> {
                TODO("Not yet implemented")
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getTimelineData(): List<ExpensesTimelineModel> { // TODO implement real data access
        val exampleData = mutableListOf<ExpensesTimelineModel>()

        exampleData.add(ExpensesTimelineModel("Fr. 11.12.2020", "Tanken", 50.00))
        exampleData.add(ExpensesTimelineModel("Sa. 12.12.2020", "Tanken", 50.00))
        exampleData.add(ExpensesTimelineModel("So. 13.12.2020", "Tanken", 50.00))
        exampleData.add(ExpensesTimelineModel("Mo. 14.12.2020", "Tanken", 50.00))
        exampleData.add(ExpensesTimelineModel("Di. 15.12.2020", "Einkauf", 25.48))
        exampleData.add(ExpensesTimelineModel("Mi. 16.12.2020", "Weihnachtsbaum", 27.00))
        exampleData.add(ExpensesTimelineModel("Do. 17.12.2020", "Einkauf", 12.98))
        exampleData.add(ExpensesTimelineModel("Fr. 18.12.2020", "Metzger", 16.32))
        exampleData.add(ExpensesTimelineModel("Sa. 19.12.2020", "Brötchen", 2.50))
        exampleData.add(ExpensesTimelineModel("So. 20.12.2020", "Lieferservice (Essen)", 32.50))
        exampleData.add(ExpensesTimelineModel("Mo. 21.12.2020", "Einkauf", 18.76))
        exampleData.add(ExpensesTimelineModel("Di. 22.12.2020", "Einkauf", 12.60))
        exampleData.add(ExpensesTimelineModel("Mi. 23.12.2020", "Einkauf", 12.60))
        exampleData.add(ExpensesTimelineModel("Do. 24.12.2020", "Einkauf", 12.60))
        exampleData.add(ExpensesTimelineModel("Fr. 25.12.2020", "Einkauf", 12.60))

        return exampleData
    }

    @Suppress("UNUSED_PARAMETER")
    fun onAddExpense(view: View) {
        val intent = Intent(this, ExpenseFormActivity::class.java)

        startActivity(intent)
    }

    fun onSelectExpense(view: View) {
        this.recyclerView.findContainingViewHolder(view)
                ?.takeIf { it is ExpensesTimelineViewHolder }
                ?.let { it as ExpensesTimelineViewHolder }
                ?.let { this.adapter.getModelByViewHolder(it) }
                ?.let { model: ExpensesTimelineModel ->
                    val intent = Intent(this, ExpensesDetailActivity::class.java)

//                    intent.putExtra("id", this.viewModel.expense.id) // TODO
                    startActivity(intent)
                }
    }
}
/*
TODO:
- Add color config to entries (elapsed, completed, open)
 */