package com.kaedin.fietsapp.models

import android.content.Context
import android.widget.Toast
import java.io.Serializable

/**
 * @param id: documentId for firebase. This is added on last
 * @param name: Vehicle name
 * @param title: Vehicle title (Licence plate, Brand, or Type etc)
 * @param latitude: Lat of the dropped of vehicle. Used for GoogleMaps so user can navigate (default 0.0 if permission is not granted)
 * @param longitude: Lon of the dropped of vehicle. Used for GoogleMaps so user can navigate (default 0.0 if permission is not granted)
 * @param dropoffDate: Dropoff date of the vehicle. This is filled with the Device datetime
 * @param type: VehicleType from the Enums class, see below
 * @see VehicleType
 */
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

    /**
     * @param context is used if showing a Toast message
     * @see Toast
     * @param msg is the message shown in the toast
     * @see Toast.setText
     *
     * This function can be overridden if needed
     */
    override fun greet(context: Context, msg : String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
