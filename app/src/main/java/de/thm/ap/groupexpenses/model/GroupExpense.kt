package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

/**
 * This class represents the document of a group in the firestore database.
 */
class GroupExpense {
    /**
     * The document id for the expense in the firebase firestore database.
     *
     * firestore path: groups/$groupId/expenses/$id
     */
    @DocumentId
    var id: String? = null

    /**
     * Name/purpose of the expense
     */
    var name: String? = null

    /**
     * Cost of the expense
     */
    var cost: Double = 0.0

    /**
     * Date of the expense
     */
    @ServerTimestamp
    var date: Date? = null

    /**
     * Determines if the expense is recurring or not.
     */
    var isRecurring: Boolean = false

    /**
     * Determines if the expense already recurred.
     * This is important to know for the database.
     */
    var alreadyRecurred: Boolean = false

    /**
     * This variable encodes the recurring interval.
     * The first bit is unused, because firebase uses 32 bit signed integers.
     *
     *     _ = unused,
     *     b = bit mask bits
     *     v = value bit
     *     _bbv vvvv  vvvv vvvv  vvvv vvvv  vvvv vvvv
     *
     * bitmask:
     *     00... = day
     *     01... = week
     *     10... = month
     *     11... = year
     *     rest  = value
     */
    var recurringInterval: Int = 0

    /**
     * The document id for the user who made the expense in the firebase firestore database.
     *
     * firestore path: users/$userId
     */
    var userId: String? = null

    /**
     * The name of the user who made the expense
     */
    var userName: String? = null

    /**
     * Gets and formats the information text about the recurring expense.
     *
     * @return The information text about the recurring expense
     */
    @Exclude
    fun getRecurringText(): String {
        val pair     = transformRecurringInterval(this.recurringInterval)
        val value    = pair.second
        val interval = when (pair.first) {
            0    -> "Tag${  if (value != 1) "e" else ""}"
            1    -> "Woche${if (value != 1) "n" else ""}"
            2    -> "Monat${if (value != 1) "e" else ""}"
            3    -> "Jahr${ if (value != 1) "e" else ""}"
            else -> "???"
        }
        return "$value $interval"
    }

    companion object {
        /**
         * Encodes the recurring interval for a recurring expense.
         *
         * @param intervalType determines if the expense recurs daily, weekly,
         *                     monthly or yearly
         * @param intervalValue the number of days/weeks/months/years until the
         *                      expense recurs
         *
         * @return Encoding for the recurring interval
         */
        fun transformRecurringInterval(intervalType: Int, intervalValue: Int): Int {
            return (intervalType shl 29) or (intervalValue)
        }

        /**
         * Decodes the recurring interval for a recurring expense.
         *
         * @param recurringInterval the number to be encoded
         *
         * @return Pair with the interval type and the interval value.
         */
        fun transformRecurringInterval(recurringInterval: Int): Pair<Int, Int> {
            val intervalType  = recurringInterval shr 29
            val intervalValue = recurringInterval and 0x1FFFFFFF

            return Pair(intervalType, intervalValue)
        }
    }
}
