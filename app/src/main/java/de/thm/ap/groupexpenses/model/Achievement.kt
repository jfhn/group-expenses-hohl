package de.thm.ap.groupexpenses.model

import android.content.res.Resources
import de.thm.ap.groupexpenses.R
import kotlin.math.absoluteValue

/**
 * This enum contains all available achievements and shamements.
 *
 * @param isShamement        Describes whether the achievement is a shamement.
 * @param fulfillment        The criteria to check if the achievement is reached. This data should
 *                           be saved in the achievementProgress map of the UserData class.
 * @param displayName        The name / title of the achievement.
 * @param displayDescription The description of the achievement.
 */
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

    /**
     * @return The name of the achievement to display.
     */
    fun getName(resources: Resources): String = resources.getString(this.displayName)

    /**
     * @return The description of the achievement to display.
     */
    fun getDescription(resources: Resources): String = resources.getString(this.displayDescription)

    /**
     * @return The type of the achievement to display.
     */
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

        /**
         * @return A list of all reached achievements.
         */
        fun getAchievements(userData: UserData): List<Achievement> = values().filter {
            it.fulfillment.invoke(userData)
        }.reversed()

        /**
         * @param achievements A list of achievements and shamements.
         * @return The number of achievements in the provided list.
         */
        fun getAchievementCount(achievements: List<Achievement>): Int =
                achievements.size - getShamementCount(achievements)

        /**
         * @param achievements A list of achievements and shamements.
         * @return The number of shamements in the provided list.
         */
        fun getShamementCount(achievements: List<Achievement>): Int =
                achievements.count { it.isShamement }
    }
}