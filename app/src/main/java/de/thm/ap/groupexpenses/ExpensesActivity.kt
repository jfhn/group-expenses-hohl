package de.thm.ap.groupexpenses

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.thm.ap.groupexpenses.timeline.ExpensesTimelineAdapter
import de.thm.ap.groupexpenses.timeline.ExpensesTimelineModel
import kotlinx.android.synthetic.main.activity_expenses.*

class ExpensesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        title = "Group name" // TODO get group name

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter       = ExpensesTimelineAdapter(getTimelineData())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.expenses, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_jump_to_current_date -> {
                TODO("Not yet implemented")
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

        exampleData.add(ExpensesTimelineModel("Mo. 14.12.2020", "Tanken", 50.00))
        exampleData.add(ExpensesTimelineModel("Di. 15.12.2020", "Einkauf", 25.48))
        exampleData.add(ExpensesTimelineModel("Mi. 16.12.2020", "Weihnachtsbaum", 27.00))
        exampleData.add(ExpensesTimelineModel("Do. 17.12.2020", "Einkauf", 12.98))
        exampleData.add(ExpensesTimelineModel("Fr. 18.12.2020", "Metzger", 16.32))
        exampleData.add(ExpensesTimelineModel("Sa. 19.12.2020", "Brötchen", 2.50))
        exampleData.add(ExpensesTimelineModel("So. 20.12.2020", "Lieferservice (Essen)", 32.50))
        exampleData.add(ExpensesTimelineModel("Mo. 21.12.2020", "Einkauf", 18.76))
        exampleData.add(ExpensesTimelineModel("Di. 22.12.2020", "Einkauf", 12.60))

        return exampleData
    }

    @Suppress("UNUSED_PARAMETER")
    fun onAddExpense(view: View) {
        TODO("Not yet implemented")
    }
}
/*
TODO:
- Scroller to current date (with highlight)
- Line to separate past from future expenses
- Button jump to today
- Rework model
- Add color config to entries (elapsed, completed, open)
 */