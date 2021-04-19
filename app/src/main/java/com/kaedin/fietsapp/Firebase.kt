package com.kaedin.fietsapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object Firebase {
    private val TAG = "Firebase"

    fun insertBicycle(
        sharedPreferences: SharedPreferences,
        alertDialogClient: AlertDialogClient,
        context: Context,
        bike: Bike
    ) {
        val database = Firebase.firestore
        val docRef = database.collection("vehicles")
        bike.id = docRef.document().id
        bike.dropoffDate =
            SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault()).format(Date())

        val map = mapOf<String, Any>(Pair("Vehicle_Data", Gson().toJson(bike)), Pair("ID", bike.id))

        // Add a new document with a generated ID
        println("Uploading map to Firebase")
        docRef.document(bike.id).set(map)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${bike.id}")
                val ids = sharedPreferences.getStringSet("document_ids", HashSet<String>())
                val nSet = mergeIds(ids, bike.id)
                sharedPreferences.edit().putStringSet("document_ids", nSet).apply()
                alertDialogClient.closeDialogIfShowing()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error uploading data. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.w(TAG, "Error adding document", e)
            }
    }

    private fun mergeIds(previousSet: MutableSet<String>?, newId: String): HashSet<String> {
        val nSet = HashSet<String>()
        if (previousSet != null && previousSet.isNotEmpty()) {
            for (id in previousSet) {
                nSet.add(id)
            }
        }
        nSet.add(newId)
        return nSet
    }

    fun retrieveVehicles(collectVehicleActivity: CollectVehicleActivity, ids: Set<String>?) {
        val database = Firebase.firestore
        if (ids != null) {
            database.collection("vehicles").get()
                .addOnSuccessListener { docs ->
                    val validDocs = docs.filter { it["ID"] in ids }
                    val vehicles = ArrayList<Vehicle>()
                    validDocs.forEach { doc ->
                        vehicles.add(
                            Gson().fromJson(
                                doc["Vehicle_Data"].toString(),
                                Vehicle::class.java
                            )
                        )
                    }
                    collectVehicleActivity.setupRecyclerView(vehicles)

                }.addOnFailureListener { exception ->
                    println(exception.printStackTrace())
                }
        } else {
            throw NullPointerException()
        }
    }

    fun retrieveVehicle(vehicleDetailsActivity: VehicleDetailsActivity, id: String) {
        val database = Firebase.firestore
        database.collection("vehicles").document(id).get()
            .addOnSuccessListener { doc ->
                val vehicle = Gson().fromJson(doc["Vehicle_Data"].toString(), Vehicle::class.java)
                vehicleDetailsActivity.setupView(vehicle)

            }.addOnFailureListener { exception ->
                println(exception.printStackTrace())
            }
    }
}