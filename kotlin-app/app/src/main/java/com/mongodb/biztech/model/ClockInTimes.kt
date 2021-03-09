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

@RequiresApi(Build.VERSION_CODES.O)
val currentDateTime = LocalTime.now()

open class ClockInTimes(_name: String = "Name") : RealmObject() {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var name: String = _name
    var owner: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @Required
    var clockInTime: String = currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
}
