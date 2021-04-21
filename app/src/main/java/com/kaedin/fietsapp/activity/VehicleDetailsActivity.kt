package com.kaedin.fietsapp.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.kaedin.fietsapp.utilities.Firebase
import com.kaedin.fietsapp.R
import com.kaedin.fietsapp.models.*
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
        var id = intent.getStringExtra("vehicle_id")
        if (id == null) id = ""
        Firebase.retrieveVehicle(this, id)
    }

    fun setupView(document : DocumentSnapshot) {
        when (document["Type"].toString().toUpperCase(Locale.getDefault())) {
            VehicleType.BICYCLE.toString() -> {
                val bicycle = Gson().fromJson(document["Vehicle_Data"].toString(), Bicycle::class.java)
                mVehicle = bicycle
                bicycle.greet(this, "Hello from you bicycle!")
            }

            VehicleType.MOTORBIKE.toString() -> {
                val motorbike = Gson().fromJson(document["Vehicle_Data"].toString(), Motorbike::class.java)
                mVehicle = motorbike
                motorbike.greet(this, "Your motor greets you!")
            }

            VehicleType.CAR.toString() -> {
                val car = Gson().fromJson(document["Vehicle_Data"].toString(), Car::class.java)
                mVehicle = car
                car.greet(this, "The car says hello!")
            }
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.details_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        details_name.text = mVehicle.name
        details_title.text = mVehicle.title
        details_navigate.setOnClickListener {
            val uri: String =
                String.format(
                    Locale.ENGLISH,
                    "google.navigation:q=%f,%f",
                    mVehicle.latitude,
                    mVehicle.longitude
                )
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }

        pickup_vehicle.setOnClickListener {
            val dialog: DialogInterface.OnClickListener =
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            Firebase.removeVehicle(mVehicle.id)
                            removeIdFromSharedPreferences(mVehicle.id)
                            startActivity(
                                Intent(
                                    this,
                                    MainActivity::class.java
                                ).putExtra("vehicle_picked_up", true)
                            )
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure? Proceeding will delete this marked vehicle")
                .setPositiveButton("Yes", dialog)
                .setNegativeButton("No", dialog).show()
        }

    }

    fun removeIdFromSharedPreferences(id: String) {
        val sharedPreferences = getSharedPreferences("documents", Context.MODE_PRIVATE)
        val ids: MutableList<String> =
            sharedPreferences.getStringSet("document_ids", HashSet<String>())!!.toMutableList()
        ids.remove(id)
        sharedPreferences.edit().putStringSet("document_ids", ids.toSet()).apply()
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