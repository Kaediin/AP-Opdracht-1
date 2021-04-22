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
import com.kaedin.fietsapp.R
import com.kaedin.fietsapp.utilities.AlertDialogClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    /**
     * Create activity and set the view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonBehaviour()
        cameFromCollecting()
    }

    /**
     * Set the logic for the 'setOnClickListeners'
     */
    private fun setButtonBehaviour() {
        fiets_afzetten.setOnClickListener {
            /** Create an alertDialogClient instance */
            val alertDialogClient =
                AlertDialogClient(
                    this,
                    R.layout.dialog_vehicle_dropoff
                )
            /** Check if we have permission to get the location of the user */
            checkPermission()
            /** Start client*/
            alertDialogClient.start()

        }

        fiets_ophalen.setOnClickListener {
            /** getSharedPreferences (this is used to store small values like settings or in this case documentIds)
             *  @see getSharedPreferences
             */
            val sharedPreferences = getSharedPreferences("documents", Context.MODE_PRIVATE)

            /** Convert the values of the key 'documentIds' into a list */
            val ids: List<String> =
                sharedPreferences.getStringSet("document_ids", HashSet<String>())!!.toList()
            /** Is the user has ids i.e the user has stores vehicles we can launch the activity
             *   Else we notify the user that there are no stores ids with a Snackbar.
             *   @see Snackbar
             */
            if (ids.isNotEmpty()) {
                /**
                 * Launch intent to new activity
                 * @see Intent
                 */
                val intent = Intent(this, CollectVehicleActivity::class.java)
                intent.putStringArrayListExtra("ids", ArrayList(ids))
                startActivity(intent)
            } else {
                Snackbar.make(
                    window.decorView.rootView,
                    "You have not dropped any vehicles off",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Here we check if the this activity was inflated after a user picked up a vehicle
     * If so we want to notify the user that the vehicle has been removed. This has to be done in the MainActivity
     * because notifying from the previous Activity with the context of this will cause a memory leak.
     */
    private fun cameFromCollecting() {
        val cameFromCollecting = intent.getBooleanExtra("vehicle_picked_up", false)
        if (cameFromCollecting) {
            Snackbar.make(
                window.decorView.rootView,
                "Vehicle successfully removed!",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Get the users location
     * @see Location
     *
     * This function is not private because it is called from a different class.
     * The function needs to be in this class (preferably) becuase of the context of the activity as
     * the LocationManager can only be accessed from an activity extension
     */
    fun getLocation(): Location? {
        var loc: Location?

        /** get location manager instance */
        val mLocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        /** Check through providers to see if any have a lastKnownLocation. Only do this if we have permission ofcourse
         *  @see LocationManager.getProviders
         */
        val providers = mLocationManager.getProviders(true)
        for (provider in providers) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }
            loc = mLocationManager.getLastKnownLocation(provider)
            if (loc != null) return loc
        }
        return null
    }

    /**
     * Check if we have permission to access users location. If not (yet) we ask.
     */
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