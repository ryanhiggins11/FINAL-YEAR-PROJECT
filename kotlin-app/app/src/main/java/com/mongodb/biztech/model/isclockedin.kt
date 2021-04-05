package com.mongodb.biztech.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

/*
* isclockedin collection on database
 */

open class isclockedin(_clockedIn: Boolean = false) : RealmObject() {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var clockedIn: Boolean = _clockedIn
}