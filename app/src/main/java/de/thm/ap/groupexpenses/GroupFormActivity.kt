package de.thm.ap.groupexpenses

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_GROUP_ID
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

        binding.progressBar.visibility = View.VISIBLE
        FirebaseWorker.createGroup(name).addOnSuccessListener { groupId ->
            val resultIntent = Intent().putExtra(KEY_GROUP_ID, groupId)
            setResult(RESULT_OK, resultIntent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gruppe konnte nicht erstellt werden", Toast.LENGTH_LONG).show()
            binding.progressBar.visibility = View.GONE
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