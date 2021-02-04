package com.mongodb.biztech

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/*
* TaskActivity: allows a user to view a collection of Tasks, edit the status of those tasks,
* create new tasks, and delete existing tasks from the collection. All tasks are stored in a realm
* and synced across devices using the partition "project=<user id>".
*/
class ClockOutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_out)

        val button = findViewById<Button>(R.id.button_clockout)
        button.setOnClickListener{
            val intent = Intent(this@ClockOutActivity, ClockInActivity::class.java)
            startActivity(intent)
        }
    }
}
