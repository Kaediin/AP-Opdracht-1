package com.kaedin.fietsapp

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Firebase {
    private val TAG = "Firebase"
    val database = Firebase.firestore

    fun insertBicycle(fiets: Fiets) {
        // Create a new user with a first and last name
        val user = hashMapOf(
            "Fiets naam" to fiets.naam,
            "Fiets merk" to fiets.merk,
            "Latitude" to fiets.latitude,
            "Longitude" to fiets.longitude
        )

        // Add a new document with a generated ID
        database.collection("fietsen")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }
}