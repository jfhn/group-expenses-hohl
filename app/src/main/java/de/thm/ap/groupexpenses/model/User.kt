package de.thm.ap.groupexpenses.model

data class User(
        /**
         * The name of the user.
         */
        val name: String,
        /**
         * The last name of the user.
         */
        val lastName: String,
        /**
         * The email of the user, if present.
         */
        val email: String? = null,
        /**
         * The transfer token of the user, if present.
         */
        val transferToken: String? = null) {

    private var _groups: List<Group>? = null

    /**
     * The groups, the user belongs to.
     */
    var groups: List<Group>
        get() {
            if (this._groups == null) {
                TODO("initialize from database")
            }

            return this._groups!!
        }
        private set(value) {
            TODO()
        }
}
