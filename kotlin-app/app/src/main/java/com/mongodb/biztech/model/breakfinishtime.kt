package com.mongodb.biztech.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.mongodb.biztech.realmApp
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import io.realm.mongodb.User
import org.bson.types.ObjectId
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
* breakfinishtime collection on database
 */

private var user: User? = realmApp.currentUser()

open class breakfinishtime(_breakFinishTime: String = "BreakFinishTime") : RealmObject() {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var name: String = user?.customData?.get("name").toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @Required
    var breakFinishTime: String = _breakFinishTime
}