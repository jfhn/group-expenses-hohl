package de.thm.ap.groupexpenses.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.ItemUserPaymentBinding
import de.thm.ap.groupexpenses.model.UserPayment
import java.util.*

/**
 * The user payments adapter contains all payments the user has made in its groups.
 * Data changes in the backend will be reflected in real time.
 *
 * @param query The (data) query for the firestore adapter
 *
 * @see FirestoreAdapter
 */
open class UserPaymentsAdapter(query: Query)
    : FirestoreAdapter<UserPaymentsAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUserPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    class ViewHolder(val binding: ItemUserPaymentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot) {
            val userPayment: UserPayment = snapshot.toObject() ?: return
            val resources = binding.root.resources

            binding.itemPaymentGroupName.text = userPayment.groupName
            binding.itemPaymentDate.text = FORMAT.format(userPayment.date)
            binding.itemPaymentCost.text = String.format(
                    Locale.GERMANY,
                    resources.getString(R.string.fmt_double_EUR),
                    userPayment.payment
            )
        }
    }

    companion object {
        private val FORMAT = SimpleDateFormat("EE dd.MM.yyyy", Locale.GERMANY)
    }
}
