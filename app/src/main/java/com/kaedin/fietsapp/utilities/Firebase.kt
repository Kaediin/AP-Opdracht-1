package com.kaedin.fietsapp.utilities

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.kaedin.fietsapp.activity.CollectVehicleActivity
import com.kaedin.fietsapp.activity.VehicleDetailsActivity
import com.kaedin.fietsapp.models.Vehicle
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * Object classes are used as 'util' classes in Kotlin
 * All functions are public (static) and do not require the class to be instanced
 * https://kotlinlang.org/docs/object-declarations.html#object-declarations
 */
object Firebase {
    /**
     * @see COLLECTION_NAME is a const val of the FirebaseCollection housing the vehicles
     */
    private const val COLLECTION_NAME = "vehicles"

    /**
     * @param sharedPreferences: is used to store generated docId in the local storage
     * @param alertDialogClient: is the same client passed on from the main. We close the client when the doc is inserted
     * @param context: we use the context to show a Toast message if uploading failed. We dont close the alertdialog then
     * @param vehicle the object needed to parse to JSON through GSON and upload to Firestore
     * @see Gson.toJson
     */
    fun insertVehicle(
        sharedPreferences: SharedPreferences,
        alertDialogClient: AlertDialogClient,
        context: Context,
        vehicle: Vehicle
    ) {
        /**
         * Create firebase instance and a new document reference. Retrieve a new generated id from the [docRef]
         * Get current device date and add that to the object with the id.
         */
        val database = Firebase.firestore
        val docRef = database.collection(COLLECTION_NAME)
        vehicle.id = docRef.document().id
        vehicle.dropoffDate =
            SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault()).format(Date())

        /**
         * Create a map of:
         * KEY: Vehicle_Data VALUE: vehicle parsed to JSON
         * KEY: ID           VALUE: Doc id. This is lifted to the top for quick access
         * KEY: Type         VALUE: Vehicle type enum. Also lifted for quick access
         */
        val map = mapOf<String, Any>(
            Pair("Vehicle_Data", Gson().toJson(vehicle)),
            Pair("ID", vehicle.id),
            Pair("Type", vehicle.type.toString())
        )

        /** Add a new document with the now stored vehicle id */
        docRef.document(vehicle.id).set(map)
            .addOnSuccessListener {
                /**
                 * get already stored ids (if has any)
                 * Android cannot append an id to the same list which has been put in storage. A new list/set instance MUST be made to insert a new value with a list
                 */
                val ids = sharedPreferences.getStringSet("document_ids", HashSet<String>())

                /** merge previous ids with current id */
                val nSet =
                    mergeIds(ids, vehicle.id)
                /** Store them back in local storage */
                sharedPreferences.edit().putStringSet("document_ids", nSet).apply()
                /** Close dialog as we are done now */
                alertDialogClient.closeDialogIfShowing()
            }
            .addOnFailureListener {
                /** Show Toast message as we encountered an error */
                Toast.makeText(
                    context,
                    "Error uploading data. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    /**
     * This is a function because sets are not mutable as standard and we need to encount for an empty set if user has not stored any ids previously
     * @param previousSet previous ids
     * @param newId new doc id
     * Android cannot append an id to the same list which has been put in storage. A new list/set instance MUST be made to insert a new value with a list
     */
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

    /**
     * @param collectVehicleActivity: the activity from which this function is called. We ask for this so that we have the context from the view/activity
     * @param ids set of document ids that are stored locally
     */
    fun retrieveVehicles(collectVehicleActivity: CollectVehicleActivity, ids: Set<String>?) {
        /** Setup a database connection */
        val database = Firebase.firestore
        /** If the user has ids we get the firebase docs, else we throw a nullpointer which is caught later on */
        if (ids != null) {
            database.collection(COLLECTION_NAME).get()
                .addOnSuccessListener { docs ->
                    /** Get documents and filter on which documentId corresponds with the id stores locally */
                    val validDocs = docs.filter { it["ID"] in ids }
                    val vehicles = ArrayList<Vehicle>()
                    /** Convert all documents to Vehicle objects and add them to a list */
                    validDocs.forEach { doc ->
                        vehicles.add(
                            Gson().fromJson(
                                doc["Vehicle_Data"].toString(),
                                Vehicle::class.java
                            )
                        )
                    }
                    /** Call the function that sets up the view as we have results now */
                    collectVehicleActivity.setupRecyclerView(vehicles)
                }
        } else {
            throw NullPointerException()
        }
    }

    /**
     * Get vehicle document based on id from Firebase
     */
    fun retrieveVehicle(vehicleDetailsActivity: VehicleDetailsActivity, id: String) {
        /** Create firebase instance and get on document(id) */
        val database = Firebase.firestore
        database.collection(COLLECTION_NAME).document(id).get()
            .addOnSuccessListener { doc ->
                /** Call a function to display view with doc. Now we wont show a view with no result.  */
                vehicleDetailsActivity.setupView(doc)

            }.addOnFailureListener { exception ->
                println(exception.printStackTrace())
            }
    }

    /**
     * Remove document from firestore based on id.
     * No rocket science..
     */
    fun removeVehicle(id: String) {
        val database = Firebase.firestore
        database.collection(COLLECTION_NAME).document(id).delete()
    }
}