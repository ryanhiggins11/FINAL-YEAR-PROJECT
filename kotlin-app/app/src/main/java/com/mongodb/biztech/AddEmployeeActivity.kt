package com.mongodb.biztech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.realm.mongodb.mongo.options.UpdateOptions
import org.bson.Document
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
/*
* AddEmployeeActivity: Allow manager to add add
* employees to the app
*/
class AddEmployeeActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var createEmployeeButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        createEmployeeButton = findViewById(R.id.button_create)

        createEmployeeButton.setOnClickListener {
            registerEmployee()
            val intent = Intent(this@AddEmployeeActivity, ManagerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_task_menu, menu)
        return true
    }

    private fun onRegisterEmployeeFailed(errorMsg: String) {
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun onRegisterEmployeeSuccess(successMsg: String) {
        Log.i(TAG(), successMsg)
        Toast.makeText(baseContext, successMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure)
        username.text.toString().isEmpty() -> false
        password.text.toString().isEmpty() -> false
        else -> true
    }

    // Functionality to add employees to the app
    private fun registerEmployee() {
        if (!validateCredentials()) {
            onRegisterEmployeeFailed("Invalid credentials, please try again")
            return
        }

        // while this operation completes, disable the buttons to create a new account
        createEmployeeButton.isEnabled = false

        val username = this.username.text.toString()
        val password = this.password.text.toString()

        // Register an employee using the Realm App
        realmApp.emailPassword.registerUserAsync(username, password) {
            // Re-enable create employee button
            createEmployeeButton.isEnabled = true
            if (!it.isSuccess) {
                onRegisterEmployeeFailed("Could not register employee, please try again")
                Log.e(TAG(), "Error: ${it.error}")
            } else {
                onRegisterEmployeeSuccess("Employee added to app!")
                Log.i(TAG(), "Successfully registered employee.")
            }
        }
    }
}