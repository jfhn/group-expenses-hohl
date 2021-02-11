package de.thm.ap.groupexpenses

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.thm.ap.groupexpenses.databinding.ActivityGroupFormBinding
import de.thm.ap.groupexpenses.worker.FirebaseWorker
import de.thm.ap.groupexpenses.worker.FirebaseWorker.joinGroup

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

        val name = binding.groupFormName.text.toString().trim()

        Toast.makeText(this, "Erstelle Gruppe ...", Toast.LENGTH_LONG).show()
        FirebaseWorker.createGroup(name).addOnSuccessListener { groupId ->
            Toast.makeText(this, "Tritt Gruppe bei...", Toast.LENGTH_LONG).show()
            joinGroup(groupId, FirebaseWorker.ROLE_ADMIN).addOnSuccessListener {
                setResult(RESULT_OK)
                finish()
            }
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