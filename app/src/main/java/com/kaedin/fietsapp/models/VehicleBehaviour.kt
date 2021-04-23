package com.kaedin.fietsapp.models

import android.content.Context
import android.widget.Toast
import com.google.type.LatLng

interface VehicleBehaviour {
    /**
     * @param context is used if showing a Toast message
     * @see Toast
     * @param msg is the message shown in the toast
     * @see Toast.setText
     *
     * This function can be overridden from an inherited model
     */
    fun greet(context: Context, msg : String)


}