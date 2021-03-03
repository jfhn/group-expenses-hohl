package de.thm.ap.groupexpenses.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * This object is used to define some (extension) functions that help with dates.
 */
@Suppress("unused")
object DateUtil {
    /**
     * The default german date format of this object.
     *
     * Format: (Day of the week) day.month.year
     */
    private val dateFormat = SimpleDateFormat("EE dd.MM.yyyy", Locale.GERMANY)

    /**
     * Formats the date by the default dateFormat of this object.
     *
     * @return the date as a string
     *
     * @see dateFormat
     */
    fun Date.format(): String {
        return this.format(dateFormat)
    }

    /**
     * Formats the date by the provided date format.
     *
     * @return the date as a string
     */
    fun Date.format(format: SimpleDateFormat): String {
        return format.format(this)
    }

    /**
     * Creates a new date from the provided parameters.
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     *
     * @return the newly created date
     */
    fun dateFromValues(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance(Locale.GERMANY)
        calendar.set(year, month, day)
        return calendar.time
    }

    /**
     * Creates a string from the date.
     * The string can be a relative description.
     * If the date is today, the hours and minutes will be returned.
     * If the date was yesterday, "gestern" will be returned.
     * If the date is not older than a week, the day of the week will be returned.
     * Otherwise the date will be returned with the german format.
     *
     * @param asAbsoluteDate determines whether the resulting string should be a relative
     *                       description
     *
     * @return the date as a string.
     */
    fun Date.formatGerman(asAbsoluteDate: Boolean = false): String {
        if (asAbsoluteDate) return dateFormat.format(this)

        return when (dayOffset(Date(), this)) {
            0L      -> SimpleDateFormat("HH:mm", Locale.GERMANY).format(this)
            1L      -> "gestern"
            in 2..6 -> SimpleDateFormat("E", Locale.GERMANY).format(this)
            else    -> dateFormat.format(this)
        }
    }

    /**
     * Creates a triple with the year, month and day.
     *
     * @return a triple with the year, month and day.
     */
    fun Date.getYearMonthDay(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance(Locale.GERMANY)
        calendar.time = this

        val year     = calendar.get(Calendar.YEAR)
        val month    = calendar.get(Calendar.MONTH)
        val day      = calendar.get(Calendar.DAY_OF_MONTH)

        return Triple(year, month, day)
    }

    /**
     * Creates a new date without the hours, minutes, seconds and milliseconds.
     *
     * @return a new date without the hours, minutes, seconds and milliseconds.
     */
    fun Date.toDateOnly(): Date {
        val calendar = Calendar.getInstance()

        calendar.time = this

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    /**
     * Calculates the day offset between two dates.
     *
     * @return the number of days between the two dates
     */
    private fun dayOffset(date1: Date, date2: Date): Long =
        (date1.toDateOnly().time - date2.toDateOnly().time) / (1000L * 60 * 60 * 24)
}