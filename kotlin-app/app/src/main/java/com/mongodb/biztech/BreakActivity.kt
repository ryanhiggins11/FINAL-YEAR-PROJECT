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
import com.mongodb.biztech.model.breakfinishtime
import com.mongodb.biztech.model.breakstarttime
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
* BreakActivity: allows an employee to start and finish their break
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

        user = realmApp.currentUser()

        val breakStartButton = findViewById<Button>(R.id.button_toStartBreak)

        breakStartButton.setOnClickListener{
            // Returns current time to breakstarttime collection
            val currentBreakStartTime = LocalTime.now()
            val employee = breakstarttime(currentBreakStartTime.format
                                (DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)))

            // All realm writes need to occur inside of a transaction
            breakRealm?.executeTransactionAsync { realm ->
                realm.insert(employee)
            }

            // Disable start break button
            breakStartButton.isEnabled = false
        }

        val breakFinishButton = findViewById<Button>(R.id.button_toGoBackToWork)

        breakFinishButton.setOnClickListener{
            // Returns current time to breakfinishtime collection
            val currentBreakFinishTime = LocalTime.now()
            val employee = breakfinishtime(currentBreakFinishTime.format
                                (DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)))

            // All realm writes need to occur inside of a transaction
            breakRealm?.executeTransactionAsync { realm ->
                realm.insert(employee)
            }

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
}
