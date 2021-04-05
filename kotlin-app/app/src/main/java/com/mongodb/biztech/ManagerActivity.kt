package com.mongodb.biztech

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.realm.mongodb.User

class ManagerActivity : AppCompatActivity(){
    private var user: User? = null
    private lateinit var addEmployeeButton: Button
    private lateinit var changeLocationButton: Button

    override fun onStart() {
        super.onStart()
        user = realmApp.currentUser()
        if (user == null) {
            // If no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else if(user?.customData?.get("name") != "admin@biztech.com"){
            // If user is not the manager, start the clock in activity
            startActivity(Intent(this, ClockInActivity::class.java))
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        addEmployeeButton = findViewById(R.id.button_addEmployee)

        // Go to add employee activity
        addEmployeeButton.setOnClickListener {
            val intent = Intent(this@ManagerActivity, AddEmployeeActivity::class.java)
            startActivity(intent)
        }

        changeLocationButton = findViewById(R.id.button_changeLocation)

        // Go to change location activity
        changeLocationButton.setOnClickListener {
            val intent = Intent(this@ManagerActivity, ChangeLocationActivity::class.java)
            startActivity(intent)
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

    override fun onBackPressed() {
        // Disable going back to ClockInActivity
        moveTaskToBack(true)
    }
}