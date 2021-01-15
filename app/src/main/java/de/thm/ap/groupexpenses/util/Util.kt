package de.thm.ap.groupexpenses.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
object Util {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")

    fun Date.format(): String {
        return this.format(dateFormat)
    }

    fun Date.format(format: SimpleDateFormat): String {
        return format.format(this)
    }
}