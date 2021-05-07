package com.mongodb.biztech

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.mongodb.biztech.model.UserCredentials
import io.realm.mongodb.Credentials
/*
* LoginActivity: Launched whenever a user isn't already logged in.
* Allows a user to enter email and password credentials to log in
* to an existing account. User can also enable biometric authentication
* and then use it to log in.
*/
class LoginActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var enableBiometricsButton: Button
    private lateinit var biometricPrompt: BiometricPrompt
    private val sharedPrefFile = "kotlinsharedpreference"
    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
                applicationContext,
                "biometric_prefs",
                Context.MODE_PRIVATE,
                "ciphertext_wrapper"
        )

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        loginButton = findViewById(R.id.button_login)
        enableBiometricsButton = findViewById(R.id.button_enable_biometrics)

        loginButton.setOnClickListener { login() }

        val canAuthenticate = BiometricManager.from(applicationContext).canAuthenticate()
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            enableBiometricsButton.visibility = View.VISIBLE
            enableBiometricsButton.setOnClickListener {
                if (ciphertextWrapper != null) {
                    showBiometricPromptForDecryption()
                } else {
                    startActivity(Intent(this, EnableBiometricLoginActivity::class.java))
                }
            }
        } else {
            enableBiometricsButton.visibility = View.INVISIBLE
        }
    }

    // Biometric authentication starts when app opened if
    // user has enabled it previously
    override fun onResume() {
        super.onResume()
        if (ciphertextWrapper != null) {
            if (UserCredentials.password == null) {
                showBiometricPromptForDecryption()
            }
        }
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
        // zero-length username and passwords are not valid
        username.text.toString().isEmpty() -> false
        password.text.toString().isEmpty() -> false
        else -> true
    }

    // Handle user authentication (login)
    private fun login() {
        if (!validateCredentials()) {
            onLoginFailed("Invalid login credentials, please try again")
            return
        }
        // while this operation completes, disable the button to login
        loginButton.isEnabled = false

        val username = this.username.text.toString()
        val password = this.password.text.toString()

        // log in with the supplied username and password when the "Log in" button is pressed.
        val creds = Credentials.emailPassword(username, password)
        realmApp.loginAsync(creds) {
            // re-enable the buttons after user login returns a result
            loginButton.isEnabled = true
            if (!it.isSuccess) {
                onLoginFailed(it.error.message ?: "Incorrect email or password, please try again")
            } else {
                val user = realmApp.currentUser()
                val customUserData : Any? = user?.customData?.get("name")
                Log.i("EXAMPLE", "Fetched custom user data: $customUserData")
                onLoginSuccess()
            }
        }
    }

    // BIOMETRICS SECTION
    @SuppressLint("CommitPrefEdits")
    private fun showBiometricPromptForDecryption() {
        ciphertextWrapper?.let { textWrapper ->
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                    secretKeyName, textWrapper.initializationVector
            )
            biometricPrompt =
                    BiometricPromptUtils.createBiometricPrompt(
                            this,
                            ::decryptPasswordFromStorage
                    )
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun decryptPasswordFromStorage(authResult: BiometricPrompt.AuthenticationResult) {
        ciphertextWrapper?.let { textWrapper ->
            authResult.cryptoObject?.cipher?.let { it ->
                val plaintext =
                        cryptographyManager.decryptPassword(textWrapper.ciphertext, it)
                UserCredentials.password = plaintext

                // Get stored username from Shared Preferences
                val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
                val sharedNameValue = sharedPreferences.getString("username_key","defaultname")

                // Login with stored username and decrypted password
                realmApp.loginAsync(Credentials.emailPassword(sharedNameValue.toString(), UserCredentials.password)){
                    if (!it.isSuccess) {
                        onLoginFailed(it.error.message ?: "Wrong email or password")
                    } else {
                        onLoginSuccess()
                    }
                }
            }
        }
    }
}