package de.thm.ap.groupexpenses

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.thm.ap.groupexpenses.databinding.ActivityMainBinding
import de.thm.ap.groupexpenses.model.Group
import de.thm.ap.groupexpenses.ui.user.UserViewModel
import de.thm.ap.groupexpenses.worker.FirebaseWorker.addGroupMember
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getGroup
import de.thm.ap.groupexpenses.worker.FirebaseWorker.getGroupRef
import java.util.*

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

        val appLinkIntent = intent
        val appLinkAction = appLinkIntent.action
        val appLinkData   = appLinkIntent.data
        if (appLinkData != null) {
            val groupId = appLinkData.lastPathSegment
            if (groupId != null) {
                joinGroupDialog(groupId)
            }
        }
    }

    fun createGroup(item: MenuItem) {
        val intent = Intent(this, GroupFormActivity::class.java)
        startActivityForResult(intent, RC_CREATE_GROUP)
    }

    private fun joinGroupDialog(groupId: String) {
        val ctx = this
        getGroup(groupId).addOnSuccessListener { group ->
            /*if (!group.members!!.contains(userViewModel.user.value!!.uid)) {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.group_invitation))
                    setMessage(getString(R.string.fmt_group_invitation) + "\n${group.name}")
                    setPositiveButton(getString(R.string.accept)) { _, _ ->
                        addGroupMember(getGroupRef(groupId), userViewModel.user.value!!)
                        openGroupActivity(groupId)
                    }
                    setNegativeButton(getString(R.string.decline), null)
                    show()
                }
            } else {
                openGroupActivity(groupId)
            }*/
        }.addOnFailureListener {
            Toast.makeText(ctx, getString(R.string.group_doesnt_exist), Toast.LENGTH_LONG).show()
        }
    }

    private fun openGroupActivity(groupId: String) {
        startActivity(Intent(this, GroupActivity::class.java).apply {
            putExtra(GroupActivity.KEY_GROUP_ID, groupId)
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
        }
    }
}