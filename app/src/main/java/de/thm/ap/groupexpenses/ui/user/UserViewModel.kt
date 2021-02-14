package de.thm.ap.groupexpenses.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.model.UserData

class UserViewModel : ViewModel() {
    val user: MutableLiveData<FirebaseUser?> = MutableLiveData()

    val userData: MutableLiveData<UserData?> = MutableLiveData()

    init {
        user.value = Firebase.auth.currentUser
    }
}