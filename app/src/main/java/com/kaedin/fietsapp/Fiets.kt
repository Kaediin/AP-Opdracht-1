package com.kaedin.fietsapp

import android.content.Context
import android.widget.Toast
import com.google.type.LatLng
import java.io.Serializable

class Fiets(
    var context: Context?,
    var naam : String,
    var merk : String,
    var latitude : Double?,
    var longitude : Double?
) : Serializable, Voertuig {
    constructor() : this(null, "", "", 0.0, 0.0)

    override fun bellen() {
        Toast.makeText(context, "TRING TRING..", Toast.LENGTH_SHORT).show()
    }

    override fun alarm() {
        Toast.makeText(context, "BZZZ BZZZ BZZZ...", Toast.LENGTH_SHORT).show()
    }
}