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
import com.kaedin.fietsapp.models.VehicleType
import kotlinx.android.synthetic.main.dialog_vehicle_dropoff.view.*

class AlertDialogClient(private val mainActivity: MainActivity, private val layout: Int) {

    private lateinit var dialog: AlertDialog
    private var selected_vehicle_type = VehicleType.BICYCLE

    fun start() {
        closeDialogIfShowing()
        val view = buildNewDialog()

        val location = mainActivity.getLocation()
        val lat = location?.latitude ?: 0.0
        val lon = location?.longitude ?: 0.0

        setVehicleSelectorBehaviour(view)
        setDropoffButtonBehaviour(view, lat, lon)
    }

    private fun setDropoffButtonBehaviour(view : View, lat : Double, lon : Double){
        view.button_dropoff.setOnClickListener {
            val imm =
                mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            if (view.dropoff_owner_name.text.isNotBlank() && view.dropoff_vehicle_name.text.isNotBlank()) {
                val bike = Bicycle()
                bike.name = view.dropoff_vehicle_name.text.toString()
                bike.latitude = lat
                bike.longitude = lon
                bike.title = view.dropoff_owner_name.text.toString()
                bike.type = selected_vehicle_type
                val sharedPreferences =
                    mainActivity.getSharedPreferences("documents", Context.MODE_PRIVATE)
                Firebase.insertVehicle(sharedPreferences, this, mainActivity, bike)
            } else {
                Snackbar.make(
                    mainActivity.window.decorView.rootView,
                    "Please fill in all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

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

    private fun buildNewDialog() : View{
        val builder = AlertDialog.Builder(mainActivity)
        val inflater = LayoutInflater.from(mainActivity)
        val view = inflater.inflate(layout, null)
        builder.setView(view)
        dialog = builder.create()
        dialog.show()
        return view
    }

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