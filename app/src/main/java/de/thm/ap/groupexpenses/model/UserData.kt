package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId

class UserData {
    @DocumentId var id     : String?                 = null
    var totalExpenses      : Double                  = 0.0
    var totalPayments      : Double                  = 0.0
    var numGroups          : Int                     = 0
    var achievementProgress: MutableMap<String, Int> = mutableMapOf()

//    var expensesCount : Int     = 0
//    var paymentsCount : Int     = 0
//    var missedPayments: Int     = 0
}
