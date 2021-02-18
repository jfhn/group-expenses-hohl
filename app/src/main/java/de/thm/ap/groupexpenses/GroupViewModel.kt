package de.thm.ap.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.thm.ap.groupexpenses.model.Group

class GroupViewModel : ViewModel() {
    lateinit var groupId: String

    val group: MutableLiveData<Group?> = MutableLiveData()

    init {
        group.value = null
    }
}