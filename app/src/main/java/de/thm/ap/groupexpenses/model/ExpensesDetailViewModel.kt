package de.thm.ap.groupexpenses.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ExpensesDetailViewModel(app: Application) : AndroidViewModel(app) {

    lateinit var expense: Expense // TODO init from db - change to val
}