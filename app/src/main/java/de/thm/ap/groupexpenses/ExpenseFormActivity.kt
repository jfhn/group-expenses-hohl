package de.thm.ap.groupexpenses

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import de.thm.ap.groupexpenses.databinding.ActivityExpenseFormBinding
import de.thm.ap.groupexpenses.util.DateUtil.dateFromValues
import de.thm.ap.groupexpenses.util.DateUtil.format
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import kotlinx.android.synthetic.main.activity_expense_form.*
import java.util.*

class ExpenseFormActivity : AppCompatActivity() {
    private val viewModel: ExpenseFormViewModel by viewModels()
    private lateinit var binding: ActivityExpenseFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIntervalData()

        showIntervalControls(expense_recurring.isChecked)

        expense_recurring.setOnCheckedChangeListener { _, isChecked ->
            showIntervalControls(isChecked)
        }

        binding.buttonSelectExpenseDate.setOnClickListener {
            DatePickerDialog(this).apply {
                setOnDateSetListener { _, year, month, day ->
                    viewModel.date.value = dateFromValues(year, month, day)
                }
                show()
            }
        }

        viewModel.date.observe(this) {
            binding.expenseDate.setText(it.formatGerman())
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
}