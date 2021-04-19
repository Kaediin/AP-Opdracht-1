package com.kaedin.fietsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_vehicle_dropoff.view.*

class AlertDialogClient(private val mainActivity: MainActivity, private val layout: Int) {

    private lateinit var dialog: AlertDialog

    fun start() {
        closeDialogIfShowing()
        val builder = AlertDialog.Builder(mainActivity)
        val inflater = LayoutInflater.from(mainActivity)
        val view = inflater.inflate(layout, null)
        builder.setView(view)
        dialog = builder.create()
        dialog.show()

        val location = mainActivity.getLocation()
        val lat = location?.latitude ?: 0.0
        val lon = location?.longitude ?: 0.0

        view.button_dropoff.setOnClickListener {
            val imm =
                mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            if (view.dropoff_owner_name.text.isNotBlank() && view.dropoff_vehicle_name.text.isNotBlank()) {
                val bike = Bike()
                bike.name = view.dropoff_vehicle_name.text.toString()
                bike.latitude = lat
                bike.longitude = lon
                bike.title = view.dropoff_owner_name.text.toString()
                val sharedPreferences =
                    mainActivity.getSharedPreferences("documents", Context.MODE_PRIVATE)
                Firebase.insertBicycle(sharedPreferences, this, mainActivity, bike)
            } else {
                Snackbar.make(
                    mainActivity.window.decorView.rootView,
                    "Please fill in all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
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