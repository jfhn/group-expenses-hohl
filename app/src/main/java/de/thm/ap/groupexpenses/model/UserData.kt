package de.thm.ap.groupexpenses.model

import com.google.firebase.firestore.DocumentId

/**
 * This class contains the relevant user data for statistics and achievement calculation.
 * @see de.thm.ap.groupexpenses.ui.user.UserViewModel
 * @see de.thm.ap.groupexpenses.ui.achievements.AchievementsViewModel
 */
class UserData {
    /**
     * The user id in the firebase firestore database.
     */
    @DocumentId var id: String? = null

    /**
     * The sum of all expenses that the user has made in its groups.
     */
    var totalExpenses: Double = 0.0

    /**
     * The sum of all payments that the user has made in its groups.
     */
    var totalPayments: Double = 0.0

    /**
     * The number of groups the user is in.
     */
    var numGroups: Int = 0

    /**
     * This map is used to evaluate a JSON object. It contains the following fields:
     *
     * The expensesCount sums the number of expenses the user has made in its groups.
     * The maxNegativeBalance contains the maximum debt the user had.
     * The paymentsCount sums the number of payments the user has made in its groups.
     *
     * {
     *   expensesCount     : number,
     *   maxNegativeBalance: number,
     *   paymentsCount     : number
     * }
     */
    var achievementProgress: MutableMap<String, Int> = mutableMapOf()
}
