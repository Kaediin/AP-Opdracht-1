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

    /**
     * fully scoped vars are initialized here. These will be used all over this class
     */
    private lateinit var mVehicle: Vehicle
    private var mBicycle: Bicycle? = null
    private var mMotorbike: Motorbike? = null
    private var mCar: Car? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_details)

        getFirebaseVehicle()
    }

    /**
     * Get the firebase document whose id matches the given id from the intent
     * @see Intent.putExtra
     */
    private fun getFirebaseVehicle() {
        var id = intent.getStringExtra("vehicle_id")
        if (id == null) id = ""
        Firebase.retrieveVehicle(this, id)
    }

    /**
     * @param document is the firebasedocument retrieved
     * @see DocumentSnapshot
     * @see Firebase.retrieveVehicle is where this function is called from
     */
    fun setupView(document : DocumentSnapshot) {
        /** set the globally scoped vars their values  */
        when (document["Type"].toString().toUpperCase(Locale.getDefault())) {
            VehicleType.BICYCLE.toString() -> {
                /**
                 * convert the json to a Bicycle object
                 * @see Bicycle
                 * Set global mVehicle and mBicycle as Bycicle inherits from Vehicle they can both be assigned the same object
                 * @see Vehicle
                 * Set the button text to 'RING' as the bicycle rings as does not honk
                 * Display a customized toast message overridden from the superclass
                 * @see Bicycle.greet
                 */
                val bicycle = Gson().fromJson(document["Vehicle_Data"].toString(), Bicycle::class.java)
                mVehicle = bicycle
                mBicycle = bicycle
                call_vehicle_button.text = "RING"
                bicycle.greet(this, "Hello from you bicycle!")
            }

            VehicleType.MOTORBIKE.toString() -> {
                /**
                 * Does the same as above but converts to Motorbike instead
                 * @see Motorbike
                 */
                val motorbike = Gson().fromJson(document["Vehicle_Data"].toString(), Motorbike::class.java)
                mVehicle = motorbike
                mMotorbike = motorbike
                motorbike.greet(this, "Your motor greets you!")
            }

            VehicleType.CAR.toString() -> {
                /**
                 * Does the same as above but converts to Car instead
                 * @see Car
                 */
                val car = Gson().fromJson(document["Vehicle_Data"].toString(), Car::class.java)
                mVehicle = car
                mCar = car
                car.greet(this, "The car says hello!")
            }
        }

        /**
         * Startup the GoogleMaps instance asynchronous
         * @see SupportMapFragment.getMapAsync
         */
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.details_map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        /** Set the text values of the layout and add a clickListener for the navigation button */
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

        setupCallingButtonBehaviour()
        setupPickupButtonBehaviour()
    }

    /**
     * Call the function depending on the vehicle type
     */
    private fun setupCallingButtonBehaviour(){
        call_vehicle_button.setOnClickListener {
            when {
                mBicycle != null -> {
                    mBicycle?.ring(this)
                }
                mMotorbike != null -> {
                    mMotorbike?.honk(this)
                }
                mCar != null -> {
                    mCar?.honk(this)
                }
            }
        }
    }

    /**
     * Create dialog to ensure user the vehicle will be deleted if accepted.
     */
    private fun setupPickupButtonBehaviour(){
        pickup_vehicle.setOnClickListener {
            /** create dialog */
            val dialog: DialogInterface.OnClickListener =
                DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        /** If the positive button is pressed, call a function to remove the vehicle from Firestore
                         * Launch intent back to the MainActivity
                         */
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
                        /** Do nothing if rejected */
                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
            /** Build dialog and show it */
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure? Proceeding will delete this marked vehicle")
                .setPositiveButton("Yes", dialog)
                .setNegativeButton("No", dialog).show()
        }
    }

    /**
     * Remove id from locally stored data
     */
    private fun removeIdFromSharedPreferences(id: String) {
        val sharedPreferences = getSharedPreferences("documents", Context.MODE_PRIVATE)
        val ids: MutableList<String> =
            sharedPreferences.getStringSet("document_ids", HashSet<String>())!!.toMutableList()
        ids.remove(id)
        sharedPreferences.edit().putStringSet("document_ids", ids.toSet()).apply()
    }

    /**
     * @param googleMap the googlemaps object which we can add settings to
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        /** Get the location from the globally stored vehicle object */
        val location = LatLng(mVehicle.latitude, mVehicle.longitude)
        /** Apply the following settings to the GoogleMaps instance */
        googleMap?.apply {
            /** Add a pin to the location */
            addMarker(MarkerOptions().position(location))
            /** Set the animation and zoom */
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 17.5f))
            /** open google maps if clicked on */
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