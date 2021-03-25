package com.mongodb.biztech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.realm.mongodb.Credentials
import io.realm.mongodb.User

/*
* LoginActivity: launched whenever a user isn't already logged in. Allows a user to enter email
* and password credentials to log in to an existing account or create a new account.
*/
class LoginActivity : AppCompatActivity() {
    private var user: User? = null
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)

        loginButton.setOnClickListener { login(false) }
    }

    override fun onBackPressed() {
        // Disable going back to ClockInActivity
        moveTaskToBack(true)
    }

    private fun onLoginSuccess() {
        // successful login ends this activity, bringing the user back to the ClockInActivity
        finish()
    }

    private fun onLoginFailed(errorMsg: String) {
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
    private fun login(createUser: Boolean) {
        if (!validateCredentials()) {
            onLoginFailed("Invalid username or password")
            return
        }

        // while this operation completes, disable the button to login
        loginButton.isEnabled = false

        val username = this.username.text.toString()
        val password = this.password.text.toString()

//        if (createUser) {
//            // register a user using the Realm App we created in the RealmApp class
//            realmApp.emailPassword.registerUserAsync(username, password) {
//                // re-enable the buttons after user registration returns a result
//                createUserButton.isEnabled = true
//                loginButton.isEnabled = true
//                if (!it.isSuccess) {
//                    onLoginFailed("Could not register user.")
//                    Log.e(TAG(), "Error: ${it.error}")
//                } else {
//                    Log.i(TAG(), "Successfully registered user.")
//                    // when the account has been created successfully, log in to the account
//                    login(false)
//                }
//            }
//        } else {
            // log in with the supplied username and password when the "Log in" button is pressed.
            val creds = Credentials.emailPassword(username, password)
            realmApp.loginAsync(creds) {
                // re-enable the buttons after user login returns a result
                loginButton.isEnabled = true
                //createUserButton.isEnabled = true
                if (!it.isSuccess) {
                    onLoginFailed(it.error.message ?: "An error occurred.")
                } else {
                    val user = realmApp.currentUser()
                    val customUserData : Any? = user?.customData?.get("name")
                    Log.i("EXAMPLE", "Fetched custom user data: $customUserData")
                    onLoginSuccess()
                }
            //}
        }
    }
}