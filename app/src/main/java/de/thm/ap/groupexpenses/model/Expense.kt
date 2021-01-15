package de.thm.ap.groupexpenses.model

import java.util.*

data class Expense(
        /**
         * The id of the expense.
         */
        val id: Int,
        /**
         * The group in which the expense was made.
         */
        val group: Group,
        /**
         * The user who has paid.
         */
        val payedUser: User,
        /**
         * The date of the expense.
         */
        val date: Date,
        /**
         * The name of the expense.
         */
        val name: String,
        /**
         * The cost of the expense in cents.
         * (prevent arithmetic errors produced by floats or doubles)
         */
        val cost: Int) {

    private var _receipts: List<String>? = null

    /**
     * The uris to receipts, bound to the expense.
     */
    var receipts: List<String>
        get() {
            if (this._receipts == null) {
                TODO("initialize from database")
            }

            return this._receipts!!
        }
        set(value) {
            TODO()
        }
}
