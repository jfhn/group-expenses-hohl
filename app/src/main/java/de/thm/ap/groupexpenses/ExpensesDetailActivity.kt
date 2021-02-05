package de.thm.ap.groupexpenses

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import de.thm.ap.groupexpenses.databinding.ActivityExpensesDetailBinding
import de.thm.ap.groupexpenses.model.ExpensesDetailViewModel

class ExpensesDetailActivity : AppCompatActivity() {

    private val viewModel: ExpensesDetailViewModel by viewModels()
    private lateinit var binding: ActivityExpensesDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO: process extras
        binding.receiptImage.setOnClickListener {
            startActivity(Intent(this, PickImageActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.expenses_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_edit -> {
                val intent = Intent(this, ExpenseFormActivity::class.java)

//                intent.putExtra("id", this.viewModel.expense.id) // TODO: set id to the current expenses id (requires database)

                startActivity(intent)

                true
            }

            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.stats)
                    setMessage(R.string.confirm_delete)
                    setNegativeButton(R.string.delete) { _, _ ->
                        TODO("delete expense")
                    }
                    setNeutralButton(R.string.cancel, null)
                    show()
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onShowReceipt(view: View) {
        TODO("Not yet implemented")
    }
}