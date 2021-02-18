package de.thm.ap.groupexpenses

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.GroupActivity.Companion.KEY_GROUP_ID
import de.thm.ap.groupexpenses.databinding.ActivityMainBinding
import de.thm.ap.groupexpenses.ui.user.UserViewModel
import de.thm.ap.groupexpenses.worker.FirebaseWorker.ROLE_MEMBER
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getGroup
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getGroupMember
import de.thm.ap.groupexpenses.worker.FirebaseWorker.joinGroup

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val RC_SIGN_IN = 9001
        const val RC_CREATE_GROUP = 123
    }

    private var isSigningIn: Boolean = false

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.user.observe(this) { user ->
            if (user == null) {
                startSignIn()
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_groups,
            R.id.navigation_payments,
            R.id.navigation_statistics,
            R.id.navigation_settings
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun createGroup(item: MenuItem) {
        val intent = Intent(this, GroupFormActivity::class.java)
        startActivityForResult(intent, RC_CREATE_GROUP)
    }

    private fun joinGroupDialog(groupId: String) {
        val ctx = this
        getGroup(groupId).addOnSuccessListener { group ->
            getGroupMember(groupId, userViewModel.user.value!!.uid).addOnSuccessListener {
                if (it.exists()) {
                    openGroupActivity(groupId)
                } else {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.group_invitation))
                        setMessage(getString(R.string.fmt_group_invitation) + "\n${group.name}")
                        setPositiveButton(getString(R.string.accept)) { _, _ ->
                            joinGroup(groupId, ROLE_MEMBER)
                            openGroupActivity(groupId)
                        }
                        setNegativeButton(getString(R.string.decline), null)
                        show()
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(ctx, getString(R.string.group_doesnt_exist), Toast.LENGTH_LONG).show()
        }
    }

    private fun openGroupActivity(groupId: String) {
        startActivity(Intent(this, GroupActivity::class.java).apply {
            putExtra(KEY_GROUP_ID, groupId)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        })
    }

    private fun shouldStartSigningIn(): Boolean {
        return !isSigningIn && FirebaseAuth.getInstance().currentUser == null
    }

    override fun onStart() {
        super.onStart()

        // Start sign in if necessary
        if (shouldStartSigningIn()) {
            startSignIn()
            return
        }

        // TODO(bug): open the group when the invitation link is clicked once
        val appLinkData = intent.data
        if (appLinkData != null) {
            val groupId = appLinkData.lastPathSegment
            intent.data = null
            if (groupId != null) {
                joinGroupDialog(groupId)
            }
        }
    }

    fun startSignIn() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
        )

        // Create and launch sign-in intent
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .build()

        startActivityForResult(intent, RC_SIGN_IN)
        isSigningIn = true
    }

    fun signOut() {
        AuthUI.getInstance().signOut(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> {
                isSigningIn = false
                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    // Successfully signed in
                    userViewModel.user.value = Firebase.auth.currentUser
                } else {
                    startSignIn()
                }
            }
            RC_CREATE_GROUP -> {
                if (resultCode == Activity.RESULT_OK) {
                    val groupId = data?.extras?.getString(KEY_GROUP_ID)
                            ?: throw IllegalStateException("groupId must be passed from GroupFormActivity")
                    openGroupActivity(groupId)
                }
            }
        }
    }
}