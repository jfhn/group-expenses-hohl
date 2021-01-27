package de.thm.ap.groupexpenses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class PaymentFormViewModel : ViewModel() {
    lateinit var groupId: String
    val date: MutableLiveData<Date> = MutableLiveData()

    init {
        date.value = Date()
    }
}
