package de.thm.ap.groupexpenses

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import de.thm.ap.groupexpenses.util.Util.format
import kotlinx.android.synthetic.main.activity_expense_form.*
import java.util.*

class ExpenseFormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_form)

        initIntervalData()

        showIntervalControls(expense_recurring.isChecked)

        expense_recurring.setOnCheckedChangeListener { _, isChecked ->
            showIntervalControls(isChecked)
        }
    }

    private fun initIntervalData() {
        expense_interval_unit.minValue = 0
        expense_interval_unit.maxValue = 3
        expense_interval_unit.displayedValues = resources.getStringArray(R.array.expense_interval_units)

        expense_interval_value.minValue = 1
        expense_interval_value.maxValue = 100
    }

    private fun showIntervalControls(checked: Boolean) {
        if (checked) {
            expense_set_interval.visibility   = View.VISIBLE
            expense_label_interval.visibility = View.VISIBLE
        } else {
            expense_set_interval.visibility   = View.GONE
            expense_label_interval.visibility = View.GONE
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSave(view: View) {
        TODO("Not yet implemented")
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSetDateToCurrentDate(view: View) {
        expense_date.setText(Calendar.getInstance().time.format())
    }
}