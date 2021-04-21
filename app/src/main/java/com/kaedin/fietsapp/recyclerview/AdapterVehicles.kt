package com.kaedin.fietsapp.recyclerview

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.fietsapp.models.Vehicle
import com.kaedin.spacex.googlemaps.MapViewHolder
import com.kaedin.spacex.googlemaps.MapViewListItemView

class AdapterVehicles(private val context: Context, private val vehicle: List<Vehicle>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mapViewListItemView = MapViewListItemView(context)
        mapViewListItemView.mapViewOnCreate(null)
        return MapViewHolder(mapViewListItemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mapViewHolder = holder as MapViewHolder
        mapViewHolder.mapViewListItemViewOnResume(vehicle[position])
    }

    override fun getItemCount() = vehicle.size
}

