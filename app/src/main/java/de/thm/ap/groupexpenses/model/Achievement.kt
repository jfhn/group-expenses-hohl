package de.thm.ap.groupexpenses.model

import android.content.res.Resources
import de.thm.ap.groupexpenses.R
import kotlin.math.absoluteValue

@Suppress("unused") // used in companion object: getAchievements()
enum class Achievement(val isShamement: Boolean,
                       private val fulfillment: (UserData) -> Boolean,
                       private val displayName: Int,
                       private val displayDescription: Int) {

    // ACHIEVEMENTS
    EXPENSES1(  false, {getExpensesCount(it) >= 1}  , R.string.achievement_expenses_1, R.string.achievement_expenses_1_desc),
    EXPENSES5(  false, {getExpensesCount(it) >= 5}  , R.string.achievement_expenses_5, R.string.achievement_expenses_5_desc),
    EXPENSES10( false, {getExpensesCount(it) >= 10} , R.string.achievement_expenses_10, R.string.achievement_expenses_10_desc),
    EXPENSES50( false, {getExpensesCount(it) >= 50} , R.string.achievement_expenses_50, R.string.achievement_expenses_50_desc),
    EXPENSES100(false, {getExpensesCount(it) >= 100}, R.string.achievement_expenses_100, R.string.achievement_expenses_100_desc),

    PAYMENTS1(  false, {getPaymentsCount(it) >= 1}  , R.string.achievement_payments_1, R.string.achievement_payments_1_desc),
    PAYMENTS5(  false, {getPaymentsCount(it) >= 5}  , R.string.achievement_payments_5, R.string.achievement_payments_5_desc),
    PAYMENTS10( false, {getPaymentsCount(it) >= 10} , R.string.achievement_payments_10, R.string.achievement_payments_10_desc),
    PAYMENTS50( false, {getPaymentsCount(it) >= 50} , R.string.achievement_payments_50, R.string.achievement_payments_50_desc),
    PAYMENTS100(false, {getPaymentsCount(it) >= 100}, R.string.achievement_payments_100, R.string.achievement_payments_100_desc),


    // SHAMEMENTS
    NEGATIVE_BALANCE50( true, {getNegativeBalance(it) > 50} , R.string.shamement_negative_balance_50 , R.string.shamement_negative_balance_50_desc),
    NEGATIVE_BALANCE100(true, {getNegativeBalance(it) > 100}, R.string.shamement_negative_balance_100, R.string.shamement_negative_balance_100_desc),
    NEGATIVE_BALANCE250(true, {getNegativeBalance(it) > 250}, R.string.shamement_negative_balance_250, R.string.shamement_negative_balance_250_desc),
    NEGATIVE_BALANCE500(true, {getNegativeBalance(it) > 500}, R.string.shamement_negative_balance_500, R.string.shamement_negative_balance_500_desc);

    fun getName(resources: Resources): String = resources.getString(this.displayName)

    fun getDescription(resources: Resources): String = resources.getString(this.displayDescription)

    fun getType(resources: Resources): String = resources.getString(if (this.isShamement) SHAMEMENT else ACHIEVEMENT)

    companion object {

        private const val SHAMEMENT  : Int = R.string.shamement
        private const val ACHIEVEMENT: Int = R.string.achievement
        
        private fun getPaymentsCount(userData: UserData): Int =
            userData.achievementProgress["paymentsCount"] ?: 0

        private fun getExpensesCount(userData: UserData): Int =
            userData.achievementProgress["expensesCount"] ?: 0

        private fun getNegativeBalance(userData: UserData): Int =
            userData.achievementProgress["maxNegativeBalance"]?.absoluteValue ?: 0

        fun getAchievements(userData: UserData): List<Achievement> = values().filter {
            it.fulfillment.invoke(userData)
        }.reversed()

        fun getAchievementCount(achievements: List<Achievement>): Int =
                achievements.size - getShamementCount(achievements)

        fun getShamementCount(achievements: List<Achievement>): Int =
                achievements.count { it.isShamement }
    }
}