package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Group {
    @DocumentId var id: String? = null
    var name: String? = null
    var totalExpenses: Double = 0.0
    var totalPayments: Double = 0.0
    var personalExpenses: Double = 0.0
    var personalPayments: Double = 0.0
    @ServerTimestamp var latestUpdate: Date? = null
    var numMembers: Int = 0

    fun getPaymentPerUser(): Double = totalExpenses / numMembers

    fun getPersonalBalance(): Double = personalPayments - totalExpenses / numMembers

    fun getTotalBalance(): Double = totalPayments - totalExpenses
}
