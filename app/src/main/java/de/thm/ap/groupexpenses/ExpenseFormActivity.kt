package de.thm.ap.groupexpenses

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_EXPENSE_ID
import de.thm.ap.groupexpenses.databinding.ActivityExpenseFormBinding
import de.thm.ap.groupexpenses.model.Expense
import de.thm.ap.groupexpenses.util.DateUtil.dateFromValues
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import de.thm.ap.groupexpenses.util.DateUtil.getYearMonthDay
import de.thm.ap.groupexpenses.worker.FirebaseWorker.addExpense
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getExpense
import de.thm.ap.groupexpenses.worker.FirebaseWorker.updateExpense
import java.util.*

class ExpenseFormActivity : AppCompatActivity() {
    private val viewModel: ExpenseFormViewModel by viewModels()
    private lateinit var binding: ActivityExpenseFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.groupId = intent.extras?.getString(GroupActivity.KEY_GROUP_ID)
                ?: throw IllegalArgumentException("Must pass extra ${GroupActivity.KEY_GROUP_ID}")

        viewModel.expenseId = intent.extras?.getString(KEY_EXPENSE_ID)

        if (viewModel.expenseId != null) {
            getExpense(viewModel.groupId, viewModel.expenseId!!).addOnSuccessListener {
                val expense: Expense = it

                binding.expenseName.setText(expense.name)
                binding.expenseValue.setText(String.format(
                        Locale.ENGLISH,
                        "%.2f",
                        expense.cost
                ))
                viewModel.date.value = expense.date!!
            }
        }

        initIntervalData()

        showIntervalControls(binding.expenseRecurring.isChecked)

        binding.expenseRecurring.setOnCheckedChangeListener { _, isChecked ->
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        binding.expenseIntervalUnit.minValue = 0
        binding.expenseIntervalUnit.maxValue = 3
        binding.expenseIntervalUnit.displayedValues = resources.getStringArray(R.array.expense_interval_units)

        binding.expenseIntervalValue.minValue = 1
        binding.expenseIntervalValue.maxValue = 100
    }

    private fun showIntervalControls(checked: Boolean) {
        if (checked) {
            binding.expenseSetInterval.visibility   = View.VISIBLE
            binding.expenseLabelInterval.visibility = View.VISIBLE
        } else {
            binding.expenseSetInterval.visibility   = View.GONE
            binding.expenseLabelInterval.visibility = View.GONE
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
}