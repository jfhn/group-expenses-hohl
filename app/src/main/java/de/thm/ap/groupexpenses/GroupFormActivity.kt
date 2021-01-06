package de.thm.ap.groupexpenses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.thm.ap.groupexpenses.databinding.ActivityGroupFormBinding

class GroupFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGroupFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = resources.getString(R.string.create_group)

    }
}