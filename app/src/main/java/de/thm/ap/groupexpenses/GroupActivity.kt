package de.thm.ap.groupexpenses

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.databinding.ActivityGroupBinding
import de.thm.ap.groupexpenses.model.Group

class GroupActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    private val viewModel: GroupViewModel by viewModels()
    private lateinit var binding: ActivityGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.groupId = intent.extras?.getString(KEY_GROUP_ID)
                ?: throw IllegalArgumentException("Must pass extra $KEY_GROUP_ID")

        title = ""
        db.document("groups/${viewModel.groupId}")
                .get().addOnSuccessListener {
                    val group: Group = it.toObject()!!
                    title = group.name
                }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }

    companion object {
        private const val TAG = "GroupDetail"
        const val KEY_GROUP_ID = "key_group_id"
    }
}