package de.thm.ap.groupexpenses.adapter

import android.app.AlertDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.R
import de.thm.ap.groupexpenses.databinding.ItemGroupPaymentBinding
import de.thm.ap.groupexpenses.model.GroupPayment
import de.thm.ap.groupexpenses.worker.FirebaseWorker
import java.util.*

open class GroupPaymentsAdapter(query: Query,
                                private val isAdmin: Boolean,
                                private val context: Context,
                                private val groupId: String)
    : FirestoreAdapter<GroupPaymentsAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGroupPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    inner class ViewHolder(val binding: ItemGroupPaymentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(snapshot: DocumentSnapshot) {
            val groupPayment: GroupPayment = snapshot.toObject() ?: return
            val resources = binding.root.resources

            binding.itemGroupPaymentUserName.text = groupPayment.userName
            binding.itemPaymentDate.text = FORMAT.format(groupPayment.date)
            binding.itemGroupPaymentCost.text = String.format(
                    Locale.GERMANY,
                    resources.getString(R.string.fmt_double_EUR),
                    groupPayment.payment
            )

            binding.itemGroupPaymentDelete.visibility =
                if (isAdmin || groupPayment.userId == Firebase.auth.currentUser?.uid) {
                    binding.itemGroupPaymentDelete.setOnClickListener {
                        AlertDialog.Builder(context).apply {
                            setTitle(R.string.delete_payment)
                            val message = context.getString(R.string.confirm_delete_payment)
                            setMessage(message)
                            setNeutralButton(R.string.cancel, null)
                            setNegativeButton(R.string.delete_payment) { _, _ ->
                                FirebaseWorker.deletePayment(groupId, groupPayment)
                            }
                            show()
                        }
                    }
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
        }
    }

    companion object {
        private val FORMAT = SimpleDateFormat("EE dd.MM.yyyy", Locale.GERMANY)
    }
}
