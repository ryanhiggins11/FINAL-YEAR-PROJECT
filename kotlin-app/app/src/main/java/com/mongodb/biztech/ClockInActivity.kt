package com.mongodb.biztech

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Handler
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
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.mongo.options.UpdateOptions
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_clock_in.*
import org.bson.Document
import java.lang.NullPointerException
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
* ClockInActivity: Allow an employee to clock in,
* reset their password, and notify the manager if
* they are sick or not. The time that the employee
* clocked in to work at and if the employee is sick
* or not is pushed to mongodb for the manager to view
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
            Log.d(TAG(), "No user")
            // If no user is currently logged in, start the login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        // For manager
        else if(user?.customData?.get("name") == "admin@biztech.com"){
            // if user is manager, start the manager activity
            startActivity(Intent(this, ManagerActivity::class.java))
        }
        // For employees
        else{
            val config = SyncConfiguration.Builder(user!!, "user=${user!!.id}")
                    .build()

            // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    // since this realm should live exactly as long as this activity, assign the realm to a member variable
                    this@ClockInActivity.clockInRealm = realm
                }
            })
            if (!checkPermissions()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions()
                }
            } else {
                // Get employees location
                getLastLocation()
                // Wait for 5 seconds before checking data pushed to mongodb from
                // Clock out activity
                Handler().postDelayed(
                        {
                            // Check if employee is clocked in
                            isClockedIn()
                        },
                        5000 // value in milliseconds
                )
            }
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
        // Button will be enabled after app checks if employee is already clocked in
        clockInButton.isEnabled = false
        clockInButton.setOnClickListener {
            clockIn()

            val intent = Intent(this@ClockInActivity, ClockOutActivity::class.java)
            startActivity(intent)
        }

        val resetPasswordButton = findViewById<Button>(R.id.button_password_reset)
        resetPasswordButton.setOnClickListener{
            val intent = Intent(this@ClockInActivity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        val isNotSickButton = findViewById<Button>(R.id.button_sick_yes)
        val isSickButton = findViewById<Button>(R.id.button_sick_no)
        // Buttons will be enabled after app checks if employee is already clocked in
        isNotSickButton.isEnabled = false
        isSickButton.isEnabled = false

        isNotSickButton.setOnClickListener{
            isNotSick()
            // Disable buttons when clicked
            isNotSickButton.isEnabled = false
            isSickButton.isEnabled = false
        }

        isSickButton.setOnClickListener{
            isSick()
            // Disable buttons when clicked
            isNotSickButton.isEnabled = false
            isSickButton.isEnabled = false
            clockInButton.isEnabled = false
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

    // Push clock in to mongodb when employee clocks in,
    // and update isclockedin to true
    @RequiresApi(Build.VERSION_CODES.O)
    private fun clockIn(){
        // Get current time
        val currentClockInTime = LocalTime.now()

        // Find clockintimes collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollectionClockInTimes =
                mongoDatabase.getCollection("clockintimes")

        // Find document in collection with user's ID
        val queryOne = Document("_partition", "user="+user.id)
        // Update document with the current time
        val updateClockInTimes = Document("_partition", "user="+user.id)
                .append("clockedInTime", currentClockInTime.format
                (DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)))
                .append("name", user?.customData?.get("name"))
        // Allow upsert (if document not found, create one)
        val updateOptions = UpdateOptions().upsert(true)
        // Update/upsert document
        mongoCollectionClockInTimes?.updateOne(queryOne, updateClockInTimes, updateOptions)?.getAsync { task ->
            if (task.isSuccess) {
                if(task.get().upsertedId != null){
                    Log.v("EXAMPLE", "upserted document")
                } else {
                    Log.v("EXAMPLE", "updated document")
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ${task.error}")
            }
        }

        // Find isclockedin collection on mongodb
        val mongoCollectionIsClockedIn =
                mongoDatabase.getCollection("isclockedin")

        // Find document in collection with user's ID
        val queryTwo = Document("_partition", "user="+user.id)
        // Update document with the current time
        val updateIsClockedIn = Document("_partition", "user="+user.id).append("clockedIn", true)
        // Update/upsert document
        mongoCollectionIsClockedIn?.updateOne(queryTwo, updateIsClockedIn, updateOptions)?.getAsync { task ->
            if (task.isSuccess) {
                if(task.get().upsertedId != null){
                    Log.v("EXAMPLE", "upserted document")
                } else {
                    Log.v("EXAMPLE", "updated document")
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ${task.error}")
            }
        }
    }

    // Used to check if employee is already clocked in
    private fun isClockedIn(){
        // Find isclockedin collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollection =
                mongoDatabase.getCollection("isclockedin")

        // Find users document in collection with their id
        val query = Document("_partition", "user="+user.id)
        mongoCollection?.findOne(query)?.getAsync { task ->
            if (!task.isSuccess) {
                Log.e("EXAMPLE", "failed to find document with: ${task.error}")
            } else {
                try {
                    // Clocked in true or false in document
                    val clockedIn = task.get()["clockedIn"]
                    // Output is clocked in true or false to console
                    Log.v("EXAMPLE", "is user clocked in: $clockedIn")
                    // Go to clock out activity if employee is already clocked in
                    if(clockedIn == true){
                        startActivity(Intent(this@ClockInActivity,
                                ClockOutActivity::class.java))
                    }
                    // Enable sick feature buttons if employee is not clocked in
                    button_sick_yes.isEnabled = true
                    button_sick_no.isEnabled = true
                }catch(e:NullPointerException){
                    // Find document in collection with user's ID
                    val query = Document("_partition", "user="+user.id)
                    Log.v("EXAMPLE", "query: $query")
                    // Update document with false
                    val updateIsClockedIn = Document("_partition", "user="+user.id).append("clockedIn", false)
                    // Allow upsert (if document not found, create one)
                    val updateOptions = UpdateOptions().upsert(true)
                    // Update/upsert document
                    mongoCollection?.updateOne(query, updateIsClockedIn, updateOptions)?.getAsync { task ->
                        if (task.isSuccess) {
                            if(task.get().upsertedId != null){
                                Log.v("EXAMPLE", "upserted document")
                            } else {
                                Log.v("EXAMPLE", "updated document")
                            }
                        } else {
                            Log.e("EXAMPLE", "failed to update document with: ${task.error}")
                        }
                    }// Wait for 1 seconds for data input above to go to mongodb
                    Handler().postDelayed(
                            {
                                // Enable sick feature buttons if employee is not clocked in
                                button_sick_yes.isEnabled = true
                                button_sick_no.isEnabled = true
                            },
                            1000 // value in milliseconds
                    )
                }
            }
        }
    }

    // Sickness Feature
    // Update employees document in isSicks collection to Yes
    private fun isSick(){
        // Find issick collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollection =
                mongoDatabase.getCollection("issicks")

        // Find document in collection with user's ID
        val query = Document("_partition", "user="+user.id)
        // Update document with Yes
        val updateIsSick = Document("_partition", "user="+user.id)
                .append("isEmployeeSick", "Yes")
                .append("name", user?.customData?.get("name"))
        // Allow upsert (if document not found, create one)
        val updateOptions = UpdateOptions().upsert(true)
        // Update/upsert document
        mongoCollection?.updateOne(query, updateIsSick, updateOptions)?.getAsync { task ->
            if (task.isSuccess) {
                if(task.get().upsertedId != null){
                    Log.v("EXAMPLE", "upserted document")
                } else {
                    Log.v("EXAMPLE", "updated document")
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ${task.error}")
            }
        }

        Toast.makeText(baseContext, "Manager notified!", Toast.LENGTH_LONG).show()
    }

    // Update employees document in isSick collection to No
    private fun isNotSick(){
        // Find issick collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollection =
                mongoDatabase.getCollection("issicks")

        // Find document in collection with user's ID
        val query = Document("_partition", "user="+user.id)
        // Update document with No
        val updateIsSick = Document("_partition", "user="+user.id)
                .append("isEmployeeSick", "No")
                .append("name", user?.customData?.get("name"))
        // Allow upsert (if document not found, create one)
        val updateOptions = UpdateOptions().upsert(true)
        // Update/upsert document
        mongoCollection?.updateOne(query, updateIsSick, updateOptions)?.getAsync { task ->
            if (task.isSuccess) {
                if(task.get().upsertedId != null){
                    Log.v("EXAMPLE", "upserted document")
                } else {
                    Log.v("EXAMPLE", "updated document")
                }
            } else {
                Log.e("EXAMPLE", "failed to update document with: ${task.error}")
            }
        }

        Toast.makeText(baseContext, "Great to hear!", Toast.LENGTH_LONG).show()
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

                // employees current location
                val latLocation = (lastLocation)!!.latitude.round(3)
                val longLocation = (lastLocation)!!.longitude.round(3)

                // Output employee location details to console
                Log.v("EXAMPLE", "employee latitude: $latLocation")
                Log.v("EXAMPLE", "employee longitude: $longLocation")

                // Find location collection on mongodb
                val user = realmApp.currentUser()
                val mongoClient =
                        user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
                val mongoDatabase =
                        mongoClient.getDatabase("tracker")
                val mongoCollection =
                        mongoDatabase.getCollection("location")

                // Find first document in collection
                val query = Document()
                mongoCollection?.findOne(query)?.getAsync { task ->
                    if (task.isSuccess) {
                        // Latitude and Longitude in document
                        val latitude = task.get()["latitude"]
                        val longitude = task.get()["longitude"]
                        // Output location details in document to console
                        Log.v("EXAMPLE", "successfully found latitude: $latitude")
                        Log.v("EXAMPLE", "successfully found longitude: $longitude")

                        // Enable button if employee location is equal to latitude & longitude in document

                        button_clockin.isEnabled = latLocation == latitude && longLocation == longitude

                        if(latLocation == latitude && longLocation == longitude){
                            button_clockin.isEnabled
                        }
                        else{
                            Toast.makeText(baseContext, "Your location is not in the workplace, please close app and try again", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Log.e("EXAMPLE", "failed to find document with: ${task.error}")
                    }
                }
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

    // Round double to 3 decimal places
    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }
}
