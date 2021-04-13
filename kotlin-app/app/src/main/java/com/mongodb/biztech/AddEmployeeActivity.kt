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
* AddEmployeeActivity: Allow Manager to add employees
*/
class AddEmployeeActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var createUserButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)
        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        createUserButton = findViewById(R.id.button_create)

        createUserButton.setOnClickListener {
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

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        username.text.toString().isEmpty() -> false
        password.text.toString().isEmpty() -> false
        else -> true
    }

    // handle user authentication (login) and account creation
    private fun registerEmployee() {
        if (!validateCredentials()) {
            onRegisterEmployeeFailed("Invalid username or password")
            return
        }

        // while this operation completes, disable the buttons to create a new account
        createUserButton.isEnabled = false

        val username = this.username.text.toString()
        val password = this.password.text.toString()

        // register a user using the Realm App we created in the RealmApp class
        realmApp.emailPassword.registerUserAsync(username, password) {
            // re-enable the buttons after user registration returns a result
            createUserButton.isEnabled = true
            if (!it.isSuccess) {
                onRegisterEmployeeFailed("Could not register user.")
                Log.e(TAG(), "Error: ${it.error}")
            } else {
                Log.i(TAG(), "Successfully registered user.")
            }
        }
    }
}