package com.kaedin.fietsapp.utilities

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.kaedin.fietsapp.models.Bicycle
import com.kaedin.fietsapp.activity.CollectVehicleActivity
import com.kaedin.fietsapp.activity.VehicleDetailsActivity
import com.kaedin.fietsapp.models.Vehicle
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object Firebase {
    private val TAG = "Firebase"
    private const val COLLECTION_NAME = "vehicles"

    fun insertVehicle(
        sharedPreferences: SharedPreferences,
        alertDialogClient: AlertDialogClient,
        context: Context,
        vehicle: Vehicle
    ) {
        val database = Firebase.firestore
        val docRef = database.collection(COLLECTION_NAME)
        vehicle.id = docRef.document().id
        vehicle.dropoffDate =
            SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault()).format(Date())

        val map = mapOf<String, Any>(Pair("Vehicle_Data", Gson().toJson(vehicle)), Pair("ID", vehicle.id), Pair("Type", vehicle.type.toString()))

        // Add a new document with a generated ID
        println("Uploading map to Firebase")
        docRef.document(vehicle.id).set(map)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${vehicle.id}")
                val ids = sharedPreferences.getStringSet("document_ids", HashSet<String>())
                val nSet =
                    mergeIds(ids, vehicle.id)
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
            database.collection(COLLECTION_NAME).get()
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
        database.collection(COLLECTION_NAME).document(id).get()
            .addOnSuccessListener { doc ->
                vehicleDetailsActivity.setupView(doc)

            }.addOnFailureListener { exception ->
                println(exception.printStackTrace())
            }
    }

    fun removeVehicle(id : String){
        val database = Firebase.firestore
        database.collection(COLLECTION_NAME).document(id).delete()
    }
}