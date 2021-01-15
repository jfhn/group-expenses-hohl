package de.thm.ap.groupexpenses.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.ItemGroupPaymentBinding
import de.thm.ap.groupexpenses.model.GroupPayment
import java.util.*

open class GroupPaymentsAdapter(query: Query)
    : FirestoreAdapter<GroupPaymentsAdapter.ViewHolder>(query)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGroupPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    class ViewHolder(val binding: ItemGroupPaymentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot) {
            val groupPayment: GroupPayment = snapshot.toObject() ?: return
            val resources = binding.root.resources

            binding.itemGroupPaymentUserName.text = groupPayment.userName
            binding.itemPaymentDate.text = FORMAT.format(groupPayment.date)
            binding.itemGroupPaymentCost.text = String.format(
                    Locale.GERMANY,
                    resources.getString(R.string.fmt_expenses_EUR),
                    groupPayment.payment
            )
        }
    }

    companion object {
        private val FORMAT = SimpleDateFormat("EE dd.MM.yyyy", Locale.GERMANY)
    }
}
