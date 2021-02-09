package de.thm.ap.groupexpenses

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_EXPENSE_ID
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_GROUP_ID
import de.thm.ap.groupexpenses.databinding.ActivityExpensesDetailBinding
import de.thm.ap.groupexpenses.model.ExpensesDetailViewModel
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import de.thm.ap.groupexpenses.worker.FirebaseWorker
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getExpense
import java.util.*

class ExpensesDetailActivity : AppCompatActivity() {

    private val viewModel: ExpensesDetailViewModel by viewModels()
    private lateinit var binding: ActivityExpensesDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.groupId = intent.extras?.getString(KEY_GROUP_ID)
            ?: throw IllegalArgumentException("must pass extra: $KEY_EXPENSE_ID")

        viewModel.expenseId = intent.extras?.getString(KEY_EXPENSE_ID)
            ?: throw IllegalArgumentException("must pass extra: $KEY_EXPENSE_ID")

        getExpense(viewModel.groupId, viewModel.expenseId).addOnSuccessListener {
            viewModel.expense.value = it
        }

        viewModel.expense.observe(this) { expense ->
            if (expense != null) {
                binding.date.text = expense.date!!.formatGerman()
                binding.cost.text = String.format(
                    Locale.GERMANY,
                    getString(R.string.fmt_expenses_EUR),
                    expense.cost
                )
                binding.user.text = expense.userName
                title = expense.name
            }
        }

        viewModel.image.observe(this) { bitmap ->
            if (bitmap != null) {
                binding.receiptImage.visibility     = View.VISIBLE
                binding.buttonAddReceipt.visibility = View.GONE
                Glide.with(this).load(bitmap).into(binding.receiptImage)
            } else {
                binding.receiptImage.visibility     = View.GONE
                binding.buttonAddReceipt.visibility = View.VISIBLE
            }
        }

        binding.receiptImage.setOnClickListener { pickImage() }
        binding.buttonAddReceipt.setOnClickListener { pickImage() }
    }

    private fun pickImage() {
        val intent = Intent(this, PickImageActivity::class.java)
        intent.putExtra(KEY_EXPENSE_ID, viewModel.expenseId)
        startActivity(intent)
    }

    private fun loadReceiptImage() {
        FirebaseWorker
            .downloadImage("images/expenses/${viewModel.expenseId}.jpg")
            .addOnSuccessListener {
                viewModel.image.value = it
            }
            .addOnFailureListener {
                viewModel.image.value = null
            }
    }

    override fun onStart() {
        super.onStart()
        loadReceiptImage()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.expenses_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }
            R.id.action_edit -> {
                val intent = Intent(this, ExpenseFormActivity::class.java)

                intent.putExtra(KEY_EXPENSE_ID, this.viewModel.expenseId)
                intent.putExtra(KEY_GROUP_ID, this.viewModel.groupId)

                startActivity(intent)

                true
            }
            R.id.action_delete -> {
                val ctx = applicationContext

                AlertDialog.Builder(this).apply {
                    setTitle(R.string.stats)
                    setMessage(R.string.confirm_delete)
                    setNegativeButton(R.string.delete) { _, _ ->
                        FirebaseWorker
                            .removeExpense(viewModel.groupId, viewModel.expenseId)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    ctx,
                                    getString(R.string.expense_remove_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    ctx,
                                    getString(R.string.expense_remove_failure),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        finish()
                    }
                    setNeutralButton(R.string.cancel, null)
                    show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}