package de.thm.ap.groupexpenses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import de.thm.ap.groupexpenses.databinding.ActivityPickImageBinding

class PickImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.pick_image)

        val uri = "/storage/emulated/O/Pictures/IMG_20210205_180831.jpg".toUri()

        binding.cropImage.of(uri).asSquare().initialize(this)
    }
}