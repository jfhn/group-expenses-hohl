package de.thm.ap.groupexpenses

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import de.thm.ap.groupexpenses.model.ExpensesViewModel
import kotlinx.android.synthetic.main.activity_expenses.*

class ExpensesDetailActivity : AppCompatActivity() {

    private val viewModel: ExpensesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_detail)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.expenses_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                TODO("Not yet implemented")

//                val intent = Intent(this, ExpenseFormActivity::class.java) // TODO requires expense form activity
//
//                intent.putExtra("id", this.viewModel.expense.id) // TODO set id to the current expenses id
//
//                startActivity(intent)
//
//                true
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

    fun onShowReceipt(view: View) {
        TODO("Not yet implemented")
    }

    fun onAddReceipt(view: View) {
        TODO("Not yet implemented")
    }
}