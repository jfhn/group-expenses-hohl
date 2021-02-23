package de.thm.ap.groupexpenses

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PickImageViewModel : ViewModel() {

    val imageUri: MutableLiveData<Uri?> = MutableLiveData()

    init {
        imageUri.value = null
    }
}