package com.kaedin.fietsapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_fiets_afzetten.*
import kotlinx.android.synthetic.main.dialog_fiets_afzetten.view.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fiets_afzetten.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.dialog_fiets_afzetten, null)
            builder.setView(view)
            val dialog = builder.create()

            ArrayAdapter.createFromResource(
                this,
                R.array.merken_array,
                android.R.layout.simple_spinner_dropdown_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                view.spinner_merk.adapter = arrayAdapter
            }

            dialog.show()

            view.button_afzetten.setOnClickListener {
                checkPermission()
                val location = getLocation()
                val fiets = Fiets()
                fiets.naam = view.afzetten_naam.text.toString()
                fiets.merk = view.spinner_merk.selectedItem.toString()
                fiets.latitude = location?.latitude
                fiets.longitude = location?.longitude
                Firebase.insertBicycle(fiets)
            }

        }


    }

    private fun getLocation(): Location? {
        var loc: Location?
        val mLocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager.getProviders(true)
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null
            }
            loc = mLocationManager.getLastKnownLocation(provider)
            if (loc != null) return loc
        }
        return null

    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
            )
            return
        }
    }
}