package de.thm.ap.groupexpenses.timeline

class ExpensesTimelineModel(
    var date: String,
    var name: String,
    private var cost: Double
) {

    fun getCost(): String {
        return String.format("%.2f €", cost)
    }

    fun setCost(value: Double) {
        cost = value
    }
} // TODO ensure correct date format -- necessary?



