package com.kaedin.fietsapp.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.kaedin.fietsapp.utilities.AlertDialogClient
import com.kaedin.fietsapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonBehaviour()
        cameFromCollecting()
    }

    private fun setButtonBehaviour(){
        fiets_afzetten.setOnClickListener {
            val alertDialogClient =
                AlertDialogClient(
                    this,
                    R.layout.dialog_vehicle_dropoff
                )
            checkPermission()
            alertDialogClient.start()
        }

        fiets_ophalen.setOnClickListener {
            val sharedPreferences = getSharedPreferences("documents", Context.MODE_PRIVATE)
            val ids : List<String> = sharedPreferences.getStringSet("document_ids", HashSet<String>())!!.toList()
            if (ids.isNotEmpty()){
                val intent = Intent(this, CollectVehicleActivity::class.java)
                intent.putStringArrayListExtra("ids", ArrayList(ids))
                startActivity(intent)
            } else {
                Snackbar.make(window.decorView.rootView, "You have not dropped any vehicles off", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun cameFromCollecting(){
        val cameFromCollecting = intent.getBooleanExtra("vehicle_picked_up", false)
        if (cameFromCollecting){
            Snackbar.make(window.decorView.rootView, "Vehicle successfully removed!", Snackbar.LENGTH_SHORT).show()
        }
    }

    fun getLocation(): Location? {
        var loc: Location?
        val mLocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mLocationManager.getProviders(true)
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED){
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