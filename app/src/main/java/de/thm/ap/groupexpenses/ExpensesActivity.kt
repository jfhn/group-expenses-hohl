package de.thm.ap.groupexpenses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.thm.ap.groupexpenses.timeline.TimelineAdapter
import de.thm.ap.groupexpenses.timeline.TimelineModel
import kotlinx.android.synthetic.main.activity_expenses.*

class ExpensesActivity : AppCompatActivity() {
    
    private val exampleData = mutableListOf<TimelineModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        initExampleData()
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = TimelineAdapter(exampleData)
    }

    private fun initExampleData() { // TODO create "real" model for expense entry: date, name, cost, elapsed, completed
        exampleData.add(TimelineModel("Mo. 14.12.2020", "Tanken", 50.00))
        exampleData.add(TimelineModel("Di. 15.12.2020", "Einkauf", 25.48))
        exampleData.add(TimelineModel("Mi. 16.12.2020", "Weihnachtsbaum", 27.00))
        exampleData.add(TimelineModel("Do. 17.12.2020", "Einkauf", 12.98))
        exampleData.add(TimelineModel("Fr. 18.12.2020", "Metzger", 16.32))
        exampleData.add(TimelineModel("Sa. 19.12.2020", "Brötchen", 2.50))
        exampleData.add(TimelineModel("So. 20.12.2020", "Lieferservice (Essen)", 32.50))
        exampleData.add(TimelineModel("Mo. 21.12.2020", "Einkauf", 18.76))
        exampleData.add(TimelineModel("Di. 22.12.2020", "Einkauf", 12.60))
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClick(view: View) {
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