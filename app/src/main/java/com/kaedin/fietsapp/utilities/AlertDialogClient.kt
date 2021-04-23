package com.kaedin.fietsapp.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kaedin.fietsapp.R
import com.kaedin.fietsapp.activity.MainActivity
import com.kaedin.fietsapp.models.Bicycle
import com.kaedin.fietsapp.models.Vehicle
import com.kaedin.fietsapp.models.VehicleType
import kotlinx.android.synthetic.main.dialog_vehicle_dropoff.view.*

/**
 * This (controller) class creates an alertDialog and prompt the user to fill in vehicle details (Name, Title, and VehicleType)
 * @param mainActivity needed to inflate the dialog on the [mainActivity]
 * @param layout: Layout int of the dialog view. This layout will be inflated on the [mainActivity]
 * @see AlertDialog
 */
class AlertDialogClient(private val mainActivity: MainActivity, private val layout: Int) {

    private lateinit var dialog: AlertDialog
    private var selected_vehicle_type = VehicleType.BICYCLE

    /**
     * After an instance is created the controller can be started with this function, hence it is public
     */
    fun start() {
        /** Close dialog if it is showing and create a new one */
        closeDialogIfShowing()
        val view = buildNewDialog()

        /** Get user location (if has permission) and set to 0.0 if we dont have permission */
        val location = mainActivity.getLocation()
        val lat = location?.latitude ?: 0.0
        val lon = location?.longitude ?: 0.0

        /** Set buttons behaviour */
        setVehicleSelectorBehaviour(view)
        setDropoffButtonBehaviour(view, lat, lon)
    }

    /**
     * When the user wants to dropoff vehicle (button at the end) this function will run
     * @param view we need the context of the view to set the clicklistener on the button OF the view
     * @param lat: Latitude of the user location
     * @param lon: Longitude of the user location
     */
    private fun setDropoffButtonBehaviour(view : View, lat : Double, lon : Double){
        /** Run this when button is clicked */
        view.button_dropoff.setOnClickListener {
            /** Get input manager and hide the keyboard if it is showing. This is so the user can see any (error) messages if prompted  */
            val imm =
                mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            /**
             * If all the fields are filled in we can create and object
             * Else we prompt the user to fill in all fields
             */
            if (view.dropoff_owner_name.text.isNotBlank() && view.dropoff_vehicle_name.text.isNotBlank()) {
                /**
                 * Create vehicle object and fill it with the name, lat, lon, and type
                 * @see Vehicle
                 */
                val vehicle = Vehicle()
                vehicle.name = view.dropoff_vehicle_name.text.toString()
                vehicle.latitude = lat
                vehicle.longitude = lon
                vehicle.title = view.dropoff_owner_name.text.toString()
                vehicle.type = selected_vehicle_type
                /** Get the local settings instance and pass it on to the Firebase function to insert generated docId into firebase AND device */
                val sharedPreferences =
                    mainActivity.getSharedPreferences("documents", Context.MODE_PRIVATE)
                Firebase.insertVehicle(sharedPreferences, this, mainActivity, vehicle)
            } else {
                /** Prompt the user to fill in all fields */
                Snackbar.make(
                    mainActivity.window.decorView.rootView,
                    "Please fill in all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Set vehicleType slider
     * Change color when tapped and saved which vehicle was tapped last
     * So now the last tapped vehicleType is highlighted and saved in a var
     */
    private fun setVehicleSelectorBehaviour(view: View){
        view.selector_bicycle.setOnClickListener {
            it.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimaryDark))
            view.selector_motor.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
            view.selector_car.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
            selected_vehicle_type = VehicleType.BICYCLE
        }

        view.selector_motor.setOnClickListener {
            it.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimaryDark))
            view.selector_bicycle.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
            view.selector_car.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
            selected_vehicle_type = VehicleType.MOTORBIKE
        }

        view.selector_car.setOnClickListener {
            it.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimaryDark))
            view.selector_motor.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
            view.selector_bicycle.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
            selected_vehicle_type = VehicleType.CAR
        }
    }

    /**
     * Build, inflateLayout, create and show dialog
     * @see AlertDialog
     */
    private fun buildNewDialog() : View{
        val builder = AlertDialog.Builder(mainActivity)
        val inflater = LayoutInflater.from(mainActivity)
        val view = inflater.inflate(layout, null)
        builder.setView(view)
        dialog = builder.create()
        dialog.show()
        return view
    }

    /**
     * Close dialog if showing. This prevents multiple instances from running if any jittering occurs
     * this can happen rarely with low memory, old android or in other strange ways.. This should happen in normal circumstances
     */
    fun closeDialogIfShowing() {
        if (::dialog.isInitialized && dialog.isShowing) {
            Snackbar.make(
                mainActivity.window.decorView.rootView,
                "Vehicle uploaded. See you soon!",
                Snackbar.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }
    }


}