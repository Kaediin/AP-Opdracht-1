package com.kaedin.fietsapp

import java.io.Serializable

open class Vehicle(
    var id : String,
    var name : String,
    var title : String,
    var latitude : Double,
    var longitude : Double,
    var dropoffDate : String
) : VehicleBehaviour, Serializable{
    constructor() : this("", "", "", 0.0, 0.0, "")

    override fun call() {
        println("$name is calling $title!")
    }
}
