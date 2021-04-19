package com.kaedin.fietsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_details.*
import java.util.*


class VehicleDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mVehicle: Vehicle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_details)

        getFirebaseVehicle()
    }

    fun getFirebaseVehicle() {
        val id = intent.getStringExtra("vehicle_id")
        Firebase.retrieveVehicle(this, id)
    }

    fun setupView(vehicle: Vehicle) {
        mVehicle = vehicle
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.details_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        details_name.text = vehicle.name
        details_title.text = vehicle.title
        details_navigate.setOnClickListener {
            val uri: String =
                String.format(Locale.ENGLISH, "geo:%f,%f", vehicle.latitude, vehicle.longitude)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val location = LatLng(mVehicle.latitude, mVehicle.longitude)
        googleMap?.apply {
            addMarker(MarkerOptions().position(location))
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5f))
            setOnMapClickListener {
                val gmmIntentUri: Uri =
                    Uri.parse("geo:${mVehicle.latitude},${mVehicle.longitude}?q=")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }
}