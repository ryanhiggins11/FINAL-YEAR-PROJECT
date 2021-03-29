package com.mongodb.biztech.model

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
* breakfinishtime collection on database
 */
@RequiresApi(Build.VERSION_CODES.O)
val currentBreakFinishTime = LocalTime.now()

open class breakfinishtime(_name: String = "Name") : RealmObject() {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var name: String = _name

    @RequiresApi(Build.VERSION_CODES.O)
    @Required
    var breakFinishTime: String = currentBreakFinishTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
}