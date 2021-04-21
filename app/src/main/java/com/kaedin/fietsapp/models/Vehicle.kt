package com.kaedin.fietsapp.models

import android.content.Context
import android.widget.Toast
import java.io.Serializable

open class Vehicle(
    var id : String,
    var name : String,
    var title : String,
    var latitude : Double,
    var longitude : Double,
    var dropoffDate : String,
    var type : VehicleType?
) : VehicleBehaviour, Serializable{
    constructor() : this("", "", "", 0.0, 0.0, "", null)

    override fun greet(context: Context, msg : String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
