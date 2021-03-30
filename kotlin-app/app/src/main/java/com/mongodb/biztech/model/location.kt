package com.mongodb.biztech.model

import com.mongodb.biztech.realmApp
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import io.realm.mongodb.User
import org.bson.types.ObjectId

/*
* location collection on database
 */
open class location(_latitude: Double = 0.0, _longitude: Double = 0.0) : RealmObject() {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var latitude: Double = _latitude
    var longitude: Double = _longitude
}