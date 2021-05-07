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
* BreakActivity: Allow an employee to go on break, which the manager
* can view on the website
*/
class BreakActivity : AppCompatActivity() {
    private var breakRealm: Realm? = null
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
                    this@BreakActivity.breakRealm = realm
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_break)

        val breakStartButton = findViewById<Button>(R.id.button_toStartBreak)
        val breakFinishButton = findViewById<Button>(R.id.button_toGoBackToWork)
        breakFinishButton.isEnabled = false
        breakStartButton.setOnClickListener{
            startBreak()
            // Disable start break button
            breakStartButton.isEnabled = false
            breakFinishButton.isEnabled = true
        }

        breakFinishButton.setOnClickListener{
            finishBreak()
            // Go back to ClockOutActivity
            val intent = Intent(this@BreakActivity, ClockOutActivity::class.java)
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

    // Push break start time to mongodb when employee starts their break
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startBreak(){
        // Get current time
        val currentTime = LocalTime.now()

        // Find breakstarttime collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollection =
                mongoDatabase.getCollection("breakstarttime")

        // Find document in collection with user's ID
        val query = Document("_partition", "user="+user.id)
        // Update document with the current time
        val update = Document("_partition", "user="+user.id).append("breakStartTime", currentTime.format
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
    }

    // Push break finish time to mongodb when employee starts their break
    @RequiresApi(Build.VERSION_CODES.O)
    private fun finishBreak(){
        // Get current time
        val currentTime = LocalTime.now()

        // Find breakfinishtime collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
                user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
                mongoClient.getDatabase("tracker")
        val mongoCollection =
                mongoDatabase.getCollection("breakfinishtime")

        // Find document in collection with user's ID
        val query = Document("_partition", "user="+user.id)
        // Update document with the current time
        val update = Document("_partition", "user="+user.id).append("breakFinishTime", currentTime.format
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
    }
}
