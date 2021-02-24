package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class GroupExpense {
    /**
     * Id of the expense document
     */
    @DocumentId
    var id: String? = null

    /**
     * name/purpose of the expense
     */
    var name: String? = null

    /**
     * cost of the expense
     */
    var cost: Double = 0.0

    /**
     * date of the expense
     */
    @ServerTimestamp
    var date: Date? = null

    /**
     *
     */
    var isRecurring: Boolean = false

    /**
     *
     */
    var alreadyRecurred: Boolean = false

    /**
     * First bit is unused, because firebase uses 32 bit signed integers
     *      _ = unused,
     *      b = bit mask bits
     *      v = value bit
     * _bbv vvvv  vvvv vvvv  vvvv vvvv  vvvv vvvv
     *
     * bitmask:
     *      00... = day
     *      01... = week
     *      10... = month
     *      11... = year
     *      rest  = value
     */
    var recurringInterval: Int = 0

    /**
     * id of the user, who made the expense
     */
    var userId: String? = null

    /**
     * name of the user, who made the expense
     */
    var userName: String? = null

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

        fun transformRecurringInterval(intervalType: Int, intervalValue: Int): Int {
            return (intervalType shl 29) or (intervalValue)
        }

        fun transformRecurringInterval(recurringInterval: Int): Pair<Int, Int> {
            val intervalType  = recurringInterval shr 29
            val intervalValue = recurringInterval and 0x1FFFFFFF

            return Pair(intervalType, intervalValue)
        }
    }
}
