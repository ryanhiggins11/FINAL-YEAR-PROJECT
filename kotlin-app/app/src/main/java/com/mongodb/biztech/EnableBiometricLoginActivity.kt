/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mongodb.biztech

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.mongodb.biztech.model.UserCredentials
import io.realm.mongodb.Credentials

/*
 * EnableBiometricLoginActivity: Manager and employee can
 * set up biometric authentication here. The activity takes their
 * password and then encrypts and stores it.
 */
class EnableBiometricLoginActivity : AppCompatActivity() {
    private val TAG = "EnableBiometricLogin"
    private lateinit var cryptographyManager: CryptographyManager
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var authorizeButton: Button
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enable_biometric_login)
        username = findViewById(R.id.input_username)
        password = findViewById(R.id.input_password)
        authorizeButton = findViewById(R.id.authorize)

        authorizeButton.setOnClickListener {
            login()
        }
    }

    private fun onLoginSuccess(successMsg: String) {
        Toast.makeText(baseContext, successMsg, Toast.LENGTH_LONG).show()
        val intent = Intent(this@EnableBiometricLoginActivity, ClockInActivity::class.java)
        startActivity(intent)
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

    // Functionality to ensure credentials entered in are correct
    @SuppressLint("CommitPrefEdits")
    private fun login() {
        if (!validateCredentials()) {
            onLoginFailed("Invalid credentials, please try again")
            return
        }

        // Access Shared Preferences
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)

        // while this operation completes, disable the button to login
        authorizeButton.isEnabled = false

        val username = this.username.text.toString()
        val password = this.password.text.toString()

        // log in with the supplied username and password when the "Log in" button is pressed.
        val creds = Credentials.emailPassword(username, password)
        realmApp.loginAsync(creds) {
            // re-enable the buttons after user login returns a result
            authorizeButton.isEnabled = true
            if (!it.isSuccess) {
                onLoginFailed(it.error.message ?: "Incorrect email or password, please try again")
            } else {
                val user = realmApp.currentUser()
                val customUserData : Any? = user?.customData?.get("name")
                Log.i("EXAMPLE", "Fetched custom user data: $customUserData")

                // store username
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                editor.putString("username_key", username)
                editor.apply()
                editor.commit()

                // store and encrypt users password
                UserCredentials.password = password
                showBiometricPromptForEncryption()
            }
        }
    }

    private fun showBiometricPromptForEncryption() {
        val canAuthenticate = BiometricManager.from(applicationContext).canAuthenticate()
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val secretKeyName = getString(R.string.secret_key_name)
            cryptographyManager = CryptographyManager()
            val cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName)
            val biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(this, ::encryptAndStorePassword)
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))

        }
    }

    private fun encryptAndStorePassword(authResult: BiometricPrompt.AuthenticationResult) {
        authResult.cryptoObject?.cipher?.apply {
            UserCredentials.password?.let { token ->
                Log.d(TAG, "The token from server is $token")
                val encryptedServerTokenWrapper = cryptographyManager.encryptPassword(token, this)
                cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                        encryptedServerTokenWrapper,
                        applicationContext,
                        "biometric_prefs",
                        Context.MODE_PRIVATE,
                        "ciphertext_wrapper"
                )
            }
        }
        onLoginSuccess("Successfully enabled biometric authentication!")
    }
}