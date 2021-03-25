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
* ResetPasswordActivity: Allow Employee to reset password
*/
class ResetPasswordActivity : AppCompatActivity(){
    private var user: User? = null
    private lateinit var password: EditText
    private lateinit var resetPassword: Button

    override fun onStart() {
        super.onStart()
        user = realmApp.currentUser()
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        password = findViewById(R.id.input_password)
        resetPassword = findViewById(R.id.button_password_reset)

        resetPassword.setOnClickListener {
            forgotPassword()
            val intent = Intent(this@ResetPasswordActivity, ClockInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onResetPasswordFailed(errorMsg: String) {
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length passwords are not valid (or secure)
        password.text.toString().isEmpty() -> false
        else -> true
    }

    private fun forgotPassword() {
        if (!validateCredentials()) {
            onResetPasswordFailed("Invalid password")
            return
        }

        // get employees email
        val user = realmApp.currentUser()
        val email : Any? = user?.customData?.get("name")

        // password that employee wants to change their password to
        val password = this.password.text.toString()

        // changes employees password
        realmApp.emailPassword.callResetPasswordFunction(email as String?, password)
    }
}