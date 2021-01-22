package de.thm.ap.groupexpenses.util

import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
object DateUtil {

    private val dateFormat = SimpleDateFormat("EE dd.MM.yyyy", Locale.GERMANY)

    fun Date.format(): String {
        return this.format(dateFormat)
    }

    fun Date.format(format: SimpleDateFormat): String {
        return format.format(this)
    }

    fun DatePicker.getDate(): Date = dateFromValues(this.year, this.month, this.dayOfMonth)

    fun dateFromValues(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance(Locale.GERMANY)
        calendar.set(year, month, day)
        return calendar.time
    }

    fun DatePicker.setDate(date: Date) {
        val (year, month, day) = date.getYearMonthDay()
        this.updateDate(year, month, day)
    }

    fun Date.formatGerman(asAbsoluteDate: Boolean = false): String {
        if (asAbsoluteDate) return dateFormat.format(this)

        val currentDate = Date()
        return when (dayOffset(currentDate, this)) {
            0L -> if (secondsOffset(currentDate, this) < 60) "gerade eben" else "heute"
            1L -> "gestern"
            else -> dateFormat.format(this)
        }
    }

    fun Date.getYearMonthDay(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance(Locale.GERMANY)
        calendar.time = this

        val year     = calendar.get(Calendar.YEAR)
        val month    = calendar.get(Calendar.MONTH)
        val day      = calendar.get(Calendar.DAY_OF_MONTH)

        return Triple(year, month, day)
    }

    fun Date.toDateOnly(): Date {
        val calendar = Calendar.getInstance()

        calendar.time = this

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    private fun dayOffset(date1: Date, date2: Date): Long =
        (date1.toDateOnly().time - date2.toDateOnly().time) / (1000L * 60 * 60 * 24)

    private fun secondsOffset(date1: Date, date2: Date): Long = (date1.time - date2.time) / 1000


}