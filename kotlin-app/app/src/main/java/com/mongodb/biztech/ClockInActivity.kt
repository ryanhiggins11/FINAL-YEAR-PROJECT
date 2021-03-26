package com.mongodb.biztech

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.mongodb.biztech.model.clockintimes
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_clock_in.*
import kotlin.math.roundToInt

/*
* ClockInActivity: allows an employee to clock in.
*/
class ClockInActivity : AppCompatActivity() {
    private var clockInRealm: Realm? = null
    private var user: User? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private lateinit var locationRequest: LocationRequest

    override fun onStart() {
        super.onStart()
        user = realmApp.currentUser()
        if (user == null) {
            // if no user is currently logged in, start the login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        // for manager
        else if(user?.customData?.get("name") == "jackmcnamee2@gmail.com"){
            // if user is manager, start the add employee activity
            startActivity(Intent(this, ManagerActivity::class.java))
        }
        // for employees
        else {
            val config = SyncConfiguration.Builder(user!!, "user=${user!!.id}")
                .build()

            // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    // since this realm should live exactly as long as this activity, assign the realm to a member variable
                    this@ClockInActivity.clockInRealm = realm
                }
            })
        }

        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        } else {
            getLastLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_in)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create();
        // The priority of the request
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        // Set the desired interval for active location updates, in milliseconds
        locationRequest.interval = 20 * 1000;

        val clockInButton = findViewById<Button>(R.id.button_clockin)

        // allow employee to clock in
        clockInButton.setOnClickListener {
            // returns employee name to clockintimes collection
            val employee = clockintimes(user?.customData?.get("name").toString())

            // all realm writes need to occur inside of a transaction
            clockInRealm?.executeTransactionAsync { realm ->
                realm.insert(employee)
            }

            val intent = Intent(this@ClockInActivity, ClockOutActivity::class.java)
            startActivity(intent)
        }

        val resetPasswordButton = findViewById<Button>(R.id.button_password_reset)

        // allow employee to reset password
        resetPasswordButton.setOnClickListener{
            val intent = Intent(this@ClockInActivity, ResetPasswordActivity::class.java)
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

    // https://www.tutorialspoint.com/how-to-track-the-current-location-latitude-and-longitude-in-an-android-device-using-kotlin
    // Finds the location of the employee using the FusedLocationProviderClient
    private fun getLastLocation() {
        // Check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result

                val latLocation = (lastLocation)!!.latitude.roundToInt()
                val longLocation = (lastLocation)!!.longitude.roundToInt()

                // Set workplace location here, will disable clock in button if employee not at work
                button_clockin.isEnabled = latLocation in 0..200 && longLocation in -1 downTo -100
            } else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.")

                // Disable clock in button if location is not turned on
                button_clockin.isEnabled = false
            }
        }
    }

    // Provides toast notification to employee depending on method
    private fun showMessage(string: String) {
        val container = findViewById<View>(R.id.linearLayout)
        if (container != null) {
            Toast.makeText(this@ClockInActivity, string, Toast.LENGTH_LONG).show()
        }
    }

    // Shows employee some options to select, alongside a toast notification
    private fun showSnackBar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@ClockInActivity, mainTextStringId, Toast.LENGTH_LONG).show()
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
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackBar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })

            // Disable clock in button if location permission is denied
            button_clockin.isEnabled = false
        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    // Check if employee accepted or denied request for permissions
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
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

                else -> {
                    showSnackBar("Permission was denied", "Settings",
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
}
