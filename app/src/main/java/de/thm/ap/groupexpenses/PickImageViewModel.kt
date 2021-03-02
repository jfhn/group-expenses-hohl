package de.thm.ap.groupexpenses

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This class contains the UI related data for the pick image activity.
 *
 * @see PickImageActivity
 */
class PickImageViewModel : ViewModel() {
    /**
     * Contains the uri to the receipt.
     */
    val imageUri: MutableLiveData<Uri?> = MutableLiveData()

    init {
        imageUri.value = null
    }
}