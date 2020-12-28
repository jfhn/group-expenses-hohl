package de.thm.ap.groupexpenses.model

import java.util.*

data class Payment(
        /**
         * The group for which the payment was made.
         */
        val group: Group,
        /**
         * The user who paid.
         */
        val user: User,
        /**
         * The date of the payment.
         */
        val date: Date,
        /**
         * The amount paid in cents.
         * (prevent arithmetic errors produced by floats or doubles)
         */
        val amount: Int)
