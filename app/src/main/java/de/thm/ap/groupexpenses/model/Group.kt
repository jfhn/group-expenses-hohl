package de.thm.ap.groupexpenses.model

data class Group(
        /**
         * The name of the group.
         */
        val name: String) {

    private var _expenses: List<Expense>? = null

    private var _members: List<User>? = null

    private var _administrators: List<User>? = null

    /**
     * The expenses of the group.
     */
    var expenses: List<Expense>
        get() {
            if (this._expenses == null) {
                TODO("initialize from database")
            }

            return this._expenses!!
        }
        private set(value) {
            TODO()
        }

    /**
     * The members of the group.
     */
    var members: List<User>
        get() {
            if (this._members == null) {
                TODO("initialize from database")
            }

            return this._members!!
        }
        private set(value) {
            TODO()
        }

    /**
     * The administrators of the group.
     */
    var administrators: List<User>
        get() {
            if (this._administrators == null) {
                TODO("initialize from database")
            }

            return this._administrators!!
        }
        set(value) {
            TODO()
        }
}
