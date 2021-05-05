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
/*
* EditEmployeeActivity: Allow manager to edit an
* employees' details
*/
class EditEmployeeActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var firstName: EditText
    private lateinit var secondName: EditText
    private lateinit var dateOfBirth: EditText
    private lateinit var emergencyContact: EditText
    private lateinit var editEmployeeButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_employee)
        username = findViewById(R.id.input_username)
        firstName = findViewById(R.id.input_firstname)
        secondName = findViewById(R.id.input_secondname)
        dateOfBirth = findViewById(R.id.input_dob)
        emergencyContact = findViewById(R.id.input_emergencyContact)
        editEmployeeButton = findViewById(R.id.button_create)

        editEmployeeButton.setOnClickListener {
            editEmployee()
            val intent = Intent(this@EditEmployeeActivity, ManagerActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_task_menu, menu)
        return true
    }

    // Message for when editing an employees' details failed
    private fun onEditEmployeeFailed(errorMsg: String) {
        Log.e(TAG(), errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    // Message for when editing an employees' details succeeded
    private fun onEditEmployeeSuccess(successMsg: String) {
        Log.i(TAG(), successMsg)
        Toast.makeText(baseContext, successMsg, Toast.LENGTH_LONG).show()
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames are not valid
        username.text.toString().isEmpty() -> false
        else -> true
    }

    // Functionality to edit an employees' details
    private fun editEmployee() {
        if (!validateCredentials()) {
            onEditEmployeeFailed("Invalid username")
            return
        }

        // While this operation completes, disable the button to edit employee details
        editEmployeeButton.isEnabled = false

        val username = this.username.text.toString()
        val fName = this.firstName.text.toString()
        val sName = this.secondName.text.toString()
        val dob = this.dateOfBirth.text.toString()
        val eContact = this.emergencyContact.text.toString()

        // Find User collection on mongodb
        val user = realmApp.currentUser()
        val mongoClient =
            user!!.getMongoClient("mongodb-atlas") // service for MongoDB Atlas cluster containing custom user data
        val mongoDatabase =
            mongoClient.getDatabase("tracker")
        val mongoCollection =
            mongoDatabase.getCollection("User")

        // Find users collection that's used for website
        val mongoCollectionUsers =
                mongoDatabase.getCollection("users")

        val queryFilter = Document("name", username)

        mongoCollection.findOne(queryFilter)
            .getAsync { task ->
                if (task.isSuccess) {
                    val id = task.get()["_id"]
                    val partition = task.get()["_partition"]
                    Log.v("EXAMPLE", "successfully found partition key: $partition")

                    // Find document in collection with user's email
                    val queryOne = Document("name", username)
                    // Update document with the user details
                    val employeeDetails = Document("_partition", partition)
                                                .append("_id", id)
                                                .append("name", username)
                                                .append("firstName", fName)
                                                .append("secondName", sName)
                                                .append("dateOfBirth", dob)
                                                .append("emergencyContact", eContact)
                    // Allow upsert (if document not found, create one)
                    val updateOptions = UpdateOptions().upsert(true)
                    // Update/upsert document in User collection
                    mongoCollection?.updateOne(queryOne, employeeDetails, updateOptions)?.getAsync { task ->
                        if (task.isSuccess) {
                            if (task.get().upsertedId != null) {
                                Log.v("EXAMPLE", "upserted document")
                                onEditEmployeeSuccess("Employee details created!")
                            } else {
                                Log.v("EXAMPLE", "updated document")
                                onEditEmployeeSuccess("Employee details updated!")
                            }
                        } else {
                            Log.e("EXAMPLE", "failed to update document with: ${task.error}")
                        }
                    }

                    // Update/upsert document in users collection
                    mongoCollectionUsers?.updateOne(queryOne, employeeDetails, updateOptions)?.getAsync { task ->
                        if (task.isSuccess) {
                            if (task.get().upsertedId != null) {
                                Log.v("EXAMPLE", "upserted document")
                            } else {
                                Log.v("EXAMPLE", "updated document")
                            }
                        } else {
                            Log.e("EXAMPLE", "failed to update document with: ${task.error}")
                        }
                    }
                } else {
                    Log.e("EXAMPLE", "failed to find document with: ${task.error}")
                    onEditEmployeeFailed("Couldn't update employee details")
                }
            }
    }
}
