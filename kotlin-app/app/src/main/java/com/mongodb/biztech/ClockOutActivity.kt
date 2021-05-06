package com.mongodb.biztech

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.mongo.options.UpdateOptions
import io.realm.mongodb.sync.SyncConfiguration
import org.bson.Document
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
* ClockOutActivity: Allow an employee to clock out of work and
* go on break. The time that the employee clocked out at is pushed
* to mongodb for the manager to view.
*/
class ClockOutActivity : AppCompatActivity() {
    private var clockOutRealm: Realm? = null
    private var user: User? = null

    override fun onStart() {
        super.onStart()
        user = realmApp.currentUser()
        if (user == null) {
            // If no employee is currently logged in, start LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else {
            val config = SyncConfiguration.Builder(user!!, "user=${user!!.id}")
                .build()

            // Sync all realm changes via a new instance
            Realm.getInstanceAsync(config, object : Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    // Assign the realm so it lasts as long as the activity
                    this@ClockOutActivity.clockOutRealm = realm
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_out)

        val clockOutButton = findViewById<Button>(R.id.button_clockout)
        clockOutButton.setOnClickListener {
            clockOut()
            val intent = Intent(this@ClockOutActivity, ClockInActivity::class.java)
            startActivity(intent)
        }

        val breakButton = findViewById<Button>(R.id.button_toBreak)
        breakButton.setOnClickListener{
            val intent = Intent(this@ClockOutActivity, BreakActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_task_menu, menu)
        return true
    }

    override fun onBackPressed() {
        // Disable going back to LoginActivity and ClockInActivity
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

    // Push clock out time to mongodb when employee clocks out,
    // and update isclockedin to false
    @RequiresApi(Build.VERSION_CODES.O)
    private fun clockOut(){
        // Get current time
        val currentClockOutTime = LocalTime.now()

        // Find clockouttimes collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollection =
                mongoDatabase.getCollection("clockouttimes")

        // Find document in collection with user's ID
        val query = Document("_partition", "user="+user.id)
        // Update document with the current time
        val update = Document("_partition", "user="+user.id).append("clockedOutTime", currentClockOutTime.format
                                                                    (DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)))
                                                            .append("name", user?.customData?.get("name"))
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

        // Find isclockedin collection on mongodb
        val mongoCollectionIsClockedIn =
                mongoDatabase.getCollection("isclockedin")

        // Find document in collection with user's ID
        val queryTwo = Document("_partition", "user="+user.id)
        // Update document with the current time
        val updateIsClockedIn = Document("_partition", "user="+user.id).append("clockedIn", false)
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
}
