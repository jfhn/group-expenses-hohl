package de.thm.ap.groupexpenses

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.databinding.ActivityExpenseFormBinding
import de.thm.ap.groupexpenses.model.Expense
import de.thm.ap.groupexpenses.util.DateUtil.dateFromValues
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import de.thm.ap.groupexpenses.util.DateUtil.getYearMonthDay
import de.thm.ap.groupexpenses.worker.FirebaseWorker.addExpense
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getExpense
import de.thm.ap.groupexpenses.worker.FirebaseWorker.updateExpense
import kotlinx.android.synthetic.main.activity_expense_form.*
import java.util.*

class ExpenseFormActivity : AppCompatActivity() {
    private val viewModel: ExpenseFormViewModel by viewModels()
    private lateinit var binding: ActivityExpenseFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.groupId = intent.extras?.getString(GroupActivity.KEY_GROUP_ID)
                ?: throw IllegalArgumentException("Must pass extra ${GroupActivity.KEY_GROUP_ID}")

        viewModel.expenseId = intent.extras?.getString(KEY_EXPENSE_ID)

        if (viewModel.expenseId != null) {
            getExpense(viewModel.groupId, viewModel.expenseId!!).addOnSuccessListener {
                val expense: Expense = it.toObject()!!

                binding.expenseName.setText(expense.name)
                binding.expenseValue.setText(String.format(
                        Locale.GERMANY,
                        "%.2f",
                        expense.cost
                ))
                viewModel.date.value = expense.date!!
            }
        }

        initIntervalData()

        showIntervalControls(expense_recurring.isChecked)

        expense_recurring.setOnCheckedChangeListener { _, isChecked ->
            showIntervalControls(isChecked)
        }

        binding.buttonSelectExpenseDate.setOnClickListener {
            viewModel.date.value = Date()
        }

        binding.expenseDate.setOnClickListener { showDatePickerDialog() }

        binding.expenseSave.setOnClickListener { saveExpense() }

        viewModel.date.observe(this) {
            binding.expenseDate.setText(it.formatGerman(true))
        }
    }

    // TODO(refactor): pull out
    private fun showDatePickerDialog() {
        val (year, month, day) = viewModel.date.value!!.getYearMonthDay()

        DatePickerDialog(this, null, year, month, day).apply {
            setOnDateSetListener { _, year, month, day ->
                viewModel.date.value = dateFromValues(year, month, day)
            }
            show()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        binding.expenseName.text.toString().trim().ifEmpty {
            binding.expenseName.error = getString(R.string.expense_name_missing)
            isValid = false
        }

        val cost = binding.expenseValue.text.toString().trim()
        if (cost.isEmpty()) {
            binding.expenseValue.error = getString(R.string.expense_cost_missing)
            isValid = false
        } else {
            cost.toDoubleOrNull()?.run {
                if (this <= 0.0) {
                    binding.expenseValue.error = getString(R.string.expense_cost_too_small)
                    isValid = false
                }
            }
        }

        return isValid
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

    private fun saveExpense() {
        if (!validateForm()) return

        val expense = Expense().apply {
            name     = binding.expenseName.text.toString().trim()
            cost     = binding.expenseValue.text.toString().toDouble()
            date     = viewModel.date.value
            userId   = Firebase.auth.currentUser!!.uid
            userName = Firebase.auth.currentUser!!.displayName
        }

        // TODO(refactor): Define listeners once
        if (viewModel.expenseId == null) {
            addExpense(viewModel.groupId, expense).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(
                        this,
                        "Ausgabe konnte nicht gespeichert werden",
                        Toast.LENGTH_LONG
                ).show()
            }
        } else {
            updateExpense(viewModel.groupId, viewModel.expenseId!!, expense).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(
                        this,
                        "Ausgabe konnte nicht gespeichert werden",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        const val KEY_EXPENSE_ID = "key_expense_id"
    }
}