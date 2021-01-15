package de.thm.ap.groupexpenses.util

import android.widget.DatePicker
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
object DateUtil {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    fun Date.format(): String {
        return this.format(dateFormat)
    }

    fun Date.format(format: SimpleDateFormat): String {
        return format.format(this)
    }

    fun DatePicker.getDate(): Date {
        val day   = this.dayOfMonth
        val month = this.month
        val year  = this.year

        val calendar = Calendar.getInstance(Locale.GERMANY)
        calendar.set(day, month, year)
        return calendar.time
    }

    fun DatePicker.setDate(date: Date) {
        val calendar = Calendar.getInstance(Locale.GERMANY)
        calendar.time = date
        val day   = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year  = calendar.get(Calendar.YEAR)

        this.updateDate(year, month, day)
    }

    fun Date.formatGerman(): String = Date().let {
        when (dayOffset(it, this)) {
            0L -> if (secondsOffset(it, this) < 60) "gerade eben" else "heute"
            1L -> "gestern"
            else -> dateFormat.format(this)
        }
    }

    private fun dayOffset(date1: Date, date2: Date): Long = (date1.time - date2.time) / (1000L * 60 * 60 * 24)

    private fun secondsOffset(date1: Date, date2: Date): Long = (date1.time - date2.time) / 1000


}