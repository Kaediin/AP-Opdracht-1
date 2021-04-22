package com.kaedin.fietsapp.activity

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.fietsapp.utilities.Firebase
import com.kaedin.fietsapp.R
import com.kaedin.fietsapp.models.Vehicle
import com.kaedin.fietsapp.recyclerview.AdapterVehicles

class CollectVehicleActivity : Activity() {

    /**
     * Creates the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        start()
    }

    /**
     * Get locally stored ids and get the matching firebase documents
     */
    private fun start() {
        val ids = intent.getStringArrayListExtra("ids")
        if (ids != null) {
            Firebase.retrieveVehicles(this, ids.toSet())
        }
    }

    /**
     * @param vehicles list of VehicleObjects
     * This function is called when the firebase function has retrieved all document.
     * Only then are we ready to create the recyclerview
     */
    fun setupRecyclerView(vehicles: ArrayList<Vehicle>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterVehicles(this, vehicles)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}