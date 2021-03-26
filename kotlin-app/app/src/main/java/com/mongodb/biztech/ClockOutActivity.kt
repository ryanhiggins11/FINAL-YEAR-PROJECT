package com.mongodb.biztech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mongodb.biztech.model.clockouttimes
import io.realm.Realm
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

/*
* ClockOutActivity: allows an employee to clock out
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_out)

        val clockOutButton = findViewById<Button>(R.id.button_clockout)

        user = realmApp.currentUser()

        // allow employee to clock out
        clockOutButton.setOnClickListener {
            // returns employee name to clockouttimes collection
            val employee = clockouttimes(user?.customData?.get("name").toString())

            // All realm writes need to occur inside of a transaction
            clockOutRealm?.executeTransactionAsync { realm ->
                realm.insert(employee)
            }

            val intent = Intent(this@ClockOutActivity, ClockInActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // Disable going back to ClockInActivity without clocking out
        moveTaskToBack(true)
    }
}
