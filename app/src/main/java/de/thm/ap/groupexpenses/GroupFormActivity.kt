package de.thm.ap.groupexpenses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.databinding.ActivityGroupFormBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.worker.FirebaseWorker

class GroupFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = resources.getString(R.string.create_group)

        binding.buttonCreateGroup.setOnClickListener {
            createGroup()
        }
    }

    private fun createGroup() {
        if (!validateForm()) return

        val group = Group().apply {
            name = binding.groupFormName.text.toString().trim()
        }

        FirebaseWorker.createGroup(group).addOnSuccessListener { groupRef ->
            FirebaseWorker.addGroupMember(
                    groupRef,
                    Firebase.auth.currentUser!!,
                    FirebaseWorker.ROLE_ADMIN
            )
            setResult(RESULT_OK)
            finish()
        }.addOnFailureListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        binding.groupFormName.text.toString().trim().ifEmpty {
            binding.groupFormName.error = getString(R.string.group_name_not_empty)
            isValid = false
        }

        return isValid
    }
}