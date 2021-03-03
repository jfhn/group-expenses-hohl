package de.thm.ap.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.thm.ap.groupexpenses.databinding.ActivityGroupBinding
import de.thm.ap.groupexpenses.model.Group

/**
 * This class contains the UI related data for the group activity.
 *
 * @see GroupActivity
 */
class GroupViewModel : ViewModel() {
    /**
     * Contains the group id in the firebase firestore database.
     * This value is provided by the intent extras.
     *
     * firestore path: groups/$groupId
     */
    lateinit var groupId: String

    /**
     * Contains the binding to the parent activity.
     *
     * @see GroupActivity
     */
    lateinit var parentBinding: ActivityGroupBinding

    /**
     * Contains the data for the current group.
     * Data changes in the backend will be reflected in real time.
     *
     * firestore path: groups/$groupId/
     */
    val group: MutableLiveData<Group?> = MutableLiveData()

    init {
        group.value = null
    }
}