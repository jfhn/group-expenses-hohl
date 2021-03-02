package de.thm.ap.groupexpenses

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.databinding.ActivityPaymentFormBinding
import de.thm.ap.groupexpenses.model.GroupPayment
import de.thm.ap.groupexpenses.util.DateUtil
import de.thm.ap.groupexpenses.util.DateUtil.formatGerman
import de.thm.ap.groupexpenses.util.DateUtil.getYearMonthDay
import de.thm.ap.groupexpenses.worker.FirebaseWorker
import java.util.*

/**
 * This activity is used to create payments.
 */
class PaymentFormActivity : AppCompatActivity() {

    private val viewModel: PaymentFormViewModel by viewModels()
    private lateinit var binding: ActivityPaymentFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.groupId = intent.extras?.getString(GroupActivity.KEY_GROUP_ID)
                ?: throw IllegalArgumentException("Must pass extra ${GroupActivity.KEY_GROUP_ID}")

        binding.buttonSelectPaymentDate.setOnClickListener {
            viewModel.date.value = Date()
        }

        binding.paymentDate.setOnClickListener { showDatePickerDialog() }

        binding.paymentSave.setOnClickListener { addPayment() }

        viewModel.date.observe(this) {
            binding.paymentDate.setText(it.formatGerman(true))
        }
    }

    private fun showDatePickerDialog() {
        val (year, month, day) = viewModel.date.value!!.getYearMonthDay()

        DatePickerDialog(this, null, year, month, day).apply {
            setOnDateSetListener { _, year, month, day ->
                viewModel.date.value = DateUtil.dateFromValues(year, month, day)
            }
            show()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val value = binding.paymentValue.text.toString().trim()
        if (value.isEmpty()) {
            binding.paymentValue.error = getString(R.string.payment_value_missing)
            isValid = false
        } else {
            value.toDoubleOrNull()?.run {
                if (this <= 0.0) {
                    binding.paymentValue.error = getString(R.string.payment_value_too_small)
                    isValid = false
                }
            }
        }

        return isValid
    }

    private fun addPayment() {
        if (!validateForm()) return

        binding.progressBar.visibility = View.VISIBLE
        val value = binding.paymentValue.text.toString().trim().toDouble()
        val user = Firebase.auth.currentUser!!

        val payment = GroupPayment().apply {
            userId   = user.uid
            userName = user.displayName
            payment  = value
            date     = viewModel.date.value
        }

        FirebaseWorker.addPayment(viewModel.groupId, payment).addOnSuccessListener {
            finish()
        }.addOnFailureListener {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(
                    this,
                    getString(R.string.payment_save_error),
                    Toast.LENGTH_LONG
            ).show()
        }
    }
}
