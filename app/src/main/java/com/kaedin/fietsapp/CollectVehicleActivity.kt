package com.kaedin.fietsapp

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CollectVehicleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect)

        start()
    }

    private fun start() {
        val ids = intent.getStringArrayListExtra("ids")
        println(ids)
        if (ids != null) {
            Firebase.retrieveVehicles(this, ids.toSet())
        }
    }

    fun setupRecyclerView(vehicles: ArrayList<Vehicle>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterVehicles(this, vehicles)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}