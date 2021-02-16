package com.mongodb.biztech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_clock_in.*
import kotlin.math.roundToInt

/*
* ClockInActivity: allows an employee to clock in.
*/
class ClockInActivity : AppCompatActivity() {
    private var user: io.realm.mongodb.User? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null

    override fun onStart() {
        super.onStart()
        user = realmApp.currentUser()
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }

        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_in)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val button = findViewById<Button>(R.id.button_clockin)

        button.setOnClickListener {
            val intent = Intent(this@ClockInActivity, ClockOutActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_task_menu, menu)
        return true
    }

    override fun onBackPressed() {
        // Disable going back to LoginActivity and ClockOutActivity
        moveTaskToBack(true)
    }

    // Logout functionality
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                user?.logOutAsync {
                    if (it.isSuccess) {
                        user = null
                        Log.v(TAG(), "user logged out")
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Log.e(TAG(), "log out failed! Error: ${it.error}")
                    }
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    // Finds the location of the employee using the FusedLocationProviderClient
    private fun getLastLocation() {
        // Permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return
        }

        // Find last location
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result

                val latLocation = (lastLocation)!!.latitude.roundToInt()
                val longLocation = (lastLocation)!!.longitude.roundToInt()

                // Set workplace location here, will disable clock in button if employee not at work
                button_clockin.isEnabled = latLocation in 10..11 && longLocation in -10 downTo -13
            }
            else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                Toast.makeText(this@ClockInActivity,
                    "No location detected. Make sure location is enabled on the device.", Toast.LENGTH_LONG)
            }
        }
    }

    // Check if permissions have been granted by employee
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    // Requests permissions to be granted to this application
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
                this@ClockInActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    // Request use of location
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        )
        // Show UI with rationale for requesting a permission
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showMessage("Location permission is needed for core functionality", "Okay",
                    View.OnClickListener {
                        startLocationPermissionRequest()
                    })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    // Check if employee accepted or denied request for permissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                // Go to device location settings
                else -> {
                    showMessage("Permission was denied", "Settings",
                        View.OnClickListener {
                                // Build intent that displays the App settings screen.
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts(
                                        "package",
                                        Build.DISPLAY, null
                                )
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                    )
                }
            }
        }
    }
    companion object {
        private const val TAG = "LocationProvider"
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    private fun showMessage(mainTextStringId: String, actionStringId: String,
                            listener: View.OnClickListener) {
        Toast.makeText(this@ClockInActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }
}