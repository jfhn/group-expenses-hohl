package de.thm.ap.groupexpenses

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_EXPENSE_ID
import de.thm.ap.groupexpenses.databinding.ActivityPickImageBinding
import de.thm.ap.groupexpenses.worker.FirebaseWorker
import de.thm.ap.groupexpenses.ExpensesDetailActivity.Companion.KEY_PICK_WITH_CAMERA
import java.io.File

class PickImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickImageBinding
    private val viewModel: PickImageViewModel by viewModels()

    private var pickImageWithCam: Boolean = false
    private lateinit var tempPickImageUri: Uri

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
            R.id.action_pick_image -> {
                pickImageIntent(RC_PICK_IMAGE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.w(TAG, "onActivityResult called")

        when (requestCode) {
            RC_PICK_IMAGE -> {
                if (resultCode == RESULT_OK) {
                    Log.w(TAG, "pickImageWithCam = $pickImageWithCam")
                    if (pickImageWithCam) {
                        Log.w(TAG, "viewModel.imageUri.value.path = ${viewModel.imageUri.value!!.path}")
                    } else {
                        val uri: Uri? = data?.data
                        viewModel.imageUri.value = uri
                    }
                }
            }
            RC_PICK_IMAGE_ON_START -> {
                if (resultCode == RESULT_OK) {
                    Log.w(TAG, "pickImageWithCam = $pickImageWithCam")
                    if (pickImageWithCam) {
                        Log.w(TAG, "viewModel.imageUri.value.path = ${viewModel.imageUri.value!!.path}")
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

    // /data/data/de.thm.ap.groupexpenses/files/images/pickImageTempFile.jpg
    // /data/user/0/de.thm.ap.groupexpenses/files/images/pickImageTempFile.jpg
    private fun pickImageIntent(requestCode: Int) {
        if (pickImageWithCam) {
            val photo  = File(getFileStreamPath("images"), "pickImageTempFile.jpg")
            // val dir   = getDir("images", Context.MODE_PRIVATE)
            // Log.w(TAG, "path = ${filesDir.path}")
            // val photo = File("/data/data/de.thm.ap.groupexpenses/files/images/pickImageTempFile.jpg")
            // Log.w(TAG, "photo.toString() = ${photo.toString()}")

            Log.w(TAG, File("/data/data/de.thm.ap.groupexpenses/files/images").path)

            val state = Environment.getExternalStorageState()
            Log.w(TAG, "state = $state")

            var filesDir: File? = null

            if (Environment.MEDIA_MOUNTED == state) {
                filesDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TMP_FILE_NAME)
                Log.w(TAG, "filesDir = $filesDir")
            }

            Log.w(TAG, Environment.getExternalStorageState())
            // val photo  = File(Environment.getExternalStorageState(), TMP_FILE_NAME)
            Log.w(TAG, "photo.path = ${photo.path}")
            val photoUri = FileProvider.getUriForFile(this, "de.thm.ap.groupexpenses.provider", photo)
            // val photoUri = Uri.fromFile(photo)
            Log.w(TAG, "photoUri = $photoUri")
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            /*
            if (intent.resolveActivity(packageManager) == null) {
                // error
                Log.w(TAG, "Could not resolve activity")
                finish()
                return
            }
             */

            tempPickImageUri = photoUri
            viewModel.imageUri.value = tempPickImageUri
            startActivityForResult(intent, requestCode)
            Log.w(TAG, "viewModel.imageUri.value.path = ${viewModel.imageUri.value!!.path}")
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT).setType("image/*")

            if (intent.resolveActivity(packageManager) == null) {
                // error
                return
            }

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
