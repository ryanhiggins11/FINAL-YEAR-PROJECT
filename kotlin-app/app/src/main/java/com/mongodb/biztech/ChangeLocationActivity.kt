package com.mongodb.biztech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.mongodb.biztech.model.location
import io.realm.DynamicRealm
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.mongo.options.UpdateOptions
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.Document
import org.bson.types.ObjectId
import kotlin.math.round
import kotlin.properties.Delegates

/*
* ChangeLocationActivity: Allow Manager to set location of where employees have to clock in
*/
class ChangeLocationActivity : AppCompatActivity() {
    private var locationRealm: Realm? = null
    private var user: User? = null
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var changeLocationButton: Button

    override fun onStart() {
        super.onStart()
        user = realmApp.currentUser()
        if (user == null) {
            // If no user is currently logged in, start the login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else {
            val config = SyncConfiguration.Builder(user!!, "user=${user!!.id}")
                    .build()

            // Sync all realm changes via a new instance, and when that instance has been successfully created connect it to an on-screen list (a recycler view)
            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    // Since this realm should live exactly as long as this activity, assign the realm to a member variable
                    this@ChangeLocationActivity.locationRealm = realm
                }
            })
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location)
        latitude = findViewById(R.id.input_lat_location)
        longitude = findViewById(R.id.input_long_location)
        changeLocationButton = findViewById(R.id.button_changeLocation)

        changeLocationButton.setOnClickListener {
            changeLocation()
            val intent = Intent(this@ChangeLocationActivity, ManagerActivity::class.java)
            startActivity(intent)
        }
    }

    // Allows manager to change latitude and longitude of clock in point
    private fun changeLocation() {
        // Gets latitude and longitude values entered by manager
        val inputLatitude = this.latitude.text.toString().toDouble().round(3)
        val inputLongitude = this.longitude.text.toString().toDouble().round(3)

        // Find location collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollection =
                mongoDatabase.getCollection("location")

        // Find document in collection with manager's ID
        val query = Document("_partition", "user="+user.id)
        // Update document with the latitude and longitude entered by manager
        val update = Document("_partition", "user="+user.id).append("latitude", inputLatitude).append("longitude", inputLongitude)
        // Allow upsert (if document not found, create one)
        val updateOptions = UpdateOptions().upsert(true)
        // Update/upsert document
        mongoCollection?.updateOne(query, update, updateOptions)?.getAsync { task ->
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_task_menu, menu)
        return true
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

    // Round double to 3 decimal places
    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}

