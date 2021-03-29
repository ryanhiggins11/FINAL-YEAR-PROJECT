package com.mongodb.biztech.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.mongodb.biztech.realmApp
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import io.realm.mongodb.User
import org.bson.types.ObjectId

/*
* breakstarttime collection on database
 */

private var user: User? = realmApp.currentUser()

open class breakstarttime(_breakStartTime: String = "BreakStartTime") : RealmObject() {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var name: String = user?.customData?.get("name").toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @Required
    var breakStartTime: String = _breakStartTime
}