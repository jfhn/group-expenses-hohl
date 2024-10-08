package de.thm.ap.groupexpenses

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import de.thm.ap.groupexpenses.ExpensesDetailActivity.Companion.KEY_PICK_WITH_CAMERA
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_EXPENSE_ID
import de.thm.ap.groupexpenses.databinding.ActivityPickImageBinding
import de.thm.ap.groupexpenses.worker.FirebaseWorker
import java.io.File

/**
 * This activity is used to pick and crop receipts.
 */
class PickImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickImageBinding
    private val viewModel: PickImageViewModel by viewModels()

    private var pickImageWithCam: Boolean = false
    private var tmpImageUri     : Uri?    = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val expenseId = intent.extras?.getString(KEY_EXPENSE_ID)
            ?: throw IllegalArgumentException("must pass extra: $KEY_EXPENSE_ID")

        pickImageWithCam = (intent.extras?.getString(KEY_PICK_WITH_CAMERA)
            ?: throw IllegalArgumentException("must pass extra: $KEY_PICK_WITH_CAMERA")) == "true"

        title = getString(R.string.pick_image)

        viewModel.imageUri.observe(this) { uri ->
            if (uri != null) {
                binding.cropImage.of(uri)
                    .asSquare()
                    .withOutputSize(500, 500)
                    .initialize(this)
            } else {
                pickImageIntent(RC_PICK_IMAGE_ON_START)
            }
        }

        binding.saveCroppedImage.setOnClickListener {
            val bitmap: Bitmap = binding.cropImage.output
            val path = "images/expenses/$expenseId.jpg"

            FirebaseWorker.uploadImage(path, bitmap).addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.failed_to_upload_receipt),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_pick_image, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_pick_image -> {
                pickImageIntent(RC_PICK_IMAGE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_PICK_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    if (pickImageWithCam) {
                        viewModel.imageUri.value = tmpImageUri
                    } else {
                        val uri: Uri? = data?.data
                        viewModel.imageUri.value = uri
                    }
                }
            }
            RC_PICK_IMAGE_ON_START -> {
                if (resultCode == RESULT_OK) {
                    if (pickImageWithCam) {
                        viewModel.imageUri.value = tmpImageUri
                    } else {
                        val uri: Uri? = data?.data
                        viewModel.imageUri.value = uri
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    finish()
                }
            }
        }
    }

    private fun pickImageIntent(requestCode: Int) {
        if (pickImageWithCam) {
            val photo = File(externalCacheDir, TMP_FILE_NAME)
            val photoUri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", photo)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            /*
            // we wanted to implement an error check for the intent,
            // but that lead to issues for some newer android versions
            if (intent.resolveActivity(packageManager) == null) {
                // error
                Log.e(TAG, "Could not resolve activity")
                finish()
                return
            }
            */

            tmpImageUri = photoUri
            startActivityForResult(intent, requestCode)
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")

            startActivityForResult(intent, requestCode)
        }
    }

    companion object {
        const val TAG = "PickImageActivity"
        const val RC_PICK_IMAGE = 52
        const val RC_PICK_IMAGE_ON_START = 84

        const val TMP_FILE_NAME = "pickImageTempFile.jpg"
    }
}
