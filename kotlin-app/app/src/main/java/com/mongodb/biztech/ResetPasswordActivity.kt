package com.mongodb.biztech

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.realm.mongodb.User
/*
* ResetPasswordActivity: Allow employee to reset their password
*/
class ResetPasswordActivity : AppCompatActivity(){
    private var user: User? = null
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var resetPassword: Button

    override fun onStart() {
        super.onStart()
        user = realmApp.currentUser()
        if (user == null) {
            // If no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        password = findViewById(R.id.input_password)
        confirmPassword = findViewById(R.id.input_password_confirm)
        resetPassword = findViewById(R.id.button_password_reset)

        resetPassword.setOnClickListener {
            resetPassword()
        }
    }

    private fun onResetPasswordFailed(errorMsg: String) {
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun onResetPasswordSuccess(successMsg: String) {
        Log.i(TAG(), successMsg)
        Toast.makeText(baseContext, successMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length passwords are not valid (or secure)
        password.text.toString().isEmpty() -> false
        confirmPassword.text.toString().isEmpty() -> false
        password.text.toString().count() <= 5 -> false
        // passwords entered have to match
        password.text.toString() != confirmPassword.text.toString() -> false
        else -> true
    }

    // Functionality to let employee reset their password
    private fun resetPassword() {
        if (!validateCredentials()) {
            onResetPasswordFailed("Invalid password, please try again (must be more than 5 characters)")
            this.password.text = null
            this.confirmPassword.text = null
            return
        }

        // Get employees email
        val user = realmApp.currentUser()
        val email : Any? = user?.customData?.get("name")

        // Password that employee wants to change their password to
        val password = this.password.text.toString()

        // Change employees password
        realmApp.emailPassword.callResetPasswordFunction(email as String, password)

        onResetPasswordSuccess("Password changed!")

        // Go back to clock in page
        val intent = Intent(this@ResetPasswordActivity, ClockInActivity::class.java)
        startActivity(intent)
    }
}