package de.thm.ap.groupexpenses.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {
    val user: MutableLiveData<FirebaseUser?> = MutableLiveData()

    init {
        user.value = null
    }
}