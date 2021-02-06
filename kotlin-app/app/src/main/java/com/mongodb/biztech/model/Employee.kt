package com.mongodb.biztech.model

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Employee(
    @PrimaryKey var _id: String = "",
    var _partition: String = "",
    var name: String = ""
): RealmObject() {}
